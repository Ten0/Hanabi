package com.flecheck.hanabi.bga

import java.util

import com.ten.hanabi.core._
import com.ten.hanabi.core.clues.{ColorClue, NumberClue}
import com.ten.hanabi.core.plays.{DiscardPlay, PlacePlay}

import scala.util.matching.Regex
import scala.collection.JavaConverters._
import scala.collection.immutable.Seq
import scala.collection.mutable


case class BGALoadException (s: String) extends Exception

object BGA {

  var cookies: Map[String, String] = Map("TournoiEnLignelang" -> "fr",
    "TournoiEnLigneuser" -> "84407922",
    "TournoiEnLigneauth" -> "f2256199a3cb5924cac9cd1def02a9ab")

  def getGameById(id: Int): Any = {
    val content1: String = Utils.getUrl("https://fr.boardgamearena.com/gamereview?table=" + id.toString, cookies = cookies)

    val regUrl: Regex = """href="(?<url>.*?)" class="choosePlayerLink""".r
    val regPlays: Regex = """g_gamelogs = (?<json>.*);""".r
    val regSetup: Regex = """completesetup.*"socketio", (?<json>.*}), \{""".r

    val nUrl: String = regUrl findFirstMatchIn content1 match {
      case Some(x) => x.group("url")
      case None => throw BGALoadException("Can't match replay url regex for game " + id)
    }

    val content2 = Utils.getUrl("https://fr.boardgamearena.com/" + nUrl, cookies = cookies)
    val playsS: String = regPlays findFirstMatchIn content2 match {
      case Some(x) => x.group("json")
      case None => throw BGALoadException("Can't match plays regex for game " + id)
    }

    val setup: String = regSetup findFirstMatchIn content2 match {
      case Some(x) => x.group("json")
      case None => throw BGALoadException("Can't match setup regex for game " + id)
    }

    val (playerList,startingPlayer,playerOrder,multi,handCards,deckCards) = JsonParser.jsonToSetup(setup)
    val plays: Seq[Play] = JsonParser.jsonToPlays(playsS)

    val orderedPlayerList = playerOrder.map(j => playerList.find{case (id, _) => id == j}.get)
    val normalizedPlayerList = (orderedPlayerList ::: orderedPlayerList).dropWhile{case (id, _) => id != startingPlayer}.take(playerList.length)
    val startingPlayerPos = (playerOrder,(0 to playerOrder.size).toList).zipped.find{case (x,_) => x == startingPlayer}.get._2


    var playersM: mutable.Map[String, Player] = scala.collection.mutable.Map[String,Player]()

    val players: util.List[Player] = normalizedPlayerList.map{case (id,name) => val p = new Player(name); playersM += (name -> p) ; p }.asJava
    val rs: RuleSet = new RuleSet(multi)
    val deck: Deck = new Deck(rs, false)
    val hanabi: Hanabi = new Hanabi(rs,deck,players)
    val cardPlay: Map[String, (Int, Int)] = plays.filter{_.isInstanceOf[CardInfo]}.asInstanceOf[Seq[CardInfo]].map{ x => (x.card , x.cardInfo)}.toMap


    def fromBGAId(BGAid: String): Int = {
      val id = deck.size() - BGAid.toInt
      val nbCardsDealtAtStart = hanabi.getNbOfCardsPerPlayer*players.size
      var ret = id
      if(id < nbCardsDealtAtStart) {
        import com.flecheck.hanabi.bga.Utils.ExtendedInt
        val decaledHands = (id - (startingPlayerPos * hanabi.getNbOfCardsPerPlayer )) +% players.size * hanabi.getNbOfCardsPerPlayer
        val player = decaledHands / hanabi.getNbOfCardsPerPlayer
        val handPos = decaledHands % hanabi.getNbOfCardsPerPlayer
        ret = player + handPos * players.size
      }
      ret
    }

    def fromBGACard(id: String, colorS: Int, numS: Int): (Int,Int) = {
      val color = colorS.toInt
      val num = numS.toInt
      num match {
          case 6 => {
            val (color, num) = cardPlay(id)
            (color -1 , num)
          }
          case _ => (color -1 , num)
      }
    }

    for {
      (id,colorBGA,numBGA) <- deckCards
      (color,num) = fromBGACard(id,colorBGA,numBGA)
      card = new Card(Color.values()(color),num)
    } yield {
      deck.setCard(fromBGAId(id), card)
    }
    for {
      (id,colorBGA,numBGA) <- handCards
      (color,num) = fromBGACard(id,colorBGA,numBGA)
      card = new Card(Color.values()(color),num)
    } yield {
      deck.setCard(fromBGAId(id), card)
    }
    deck.lock()

    // Mains initiales
    val hands = new java.util.HashMap[Int, java.util.ArrayList[Int]]()
    val nPlayers = players.size
    val nbOfCardsPerPlayer = hanabi.getNbOfCardsPerPlayer
    for {
      playerId <- 0 until nPlayers
      handU = hands.putIfAbsent(playerId, new util.ArrayList[Int]())
      hand = hands.get(playerId)
      i <- 0 until nbOfCardsPerPlayer
    } yield {
      hand.add(0, playerId + i * nPlayers) // Ajoute au début de la liste
    }
    var cardId = 0
    plays.foreach {
      case PlayCard(p, c, _) => {
        val hand = hands.get(playersM(p).getId)
        val cardPos = hand.indexOf(fromBGAId(c))
        hand.remove(cardPos)
        hand.add(cardId)
        cardId += 1

        hanabi.savePlay(new PlacePlay(playersM(p), cardPos))
      }
      case DiscardCard(p, c, _) => {
        val hand = hands.get(playersM(p).getId)
        val cardPos = hand.indexOf(fromBGAId(c))
        hand.remove(cardPos)
        hand.add(cardId)
        cardId += 1

        hanabi.savePlay(new DiscardPlay(playersM(p), cardPos))

      }
      case GiveValue(p, t, v) => {
        playersM(p).clue(playersM(t),new NumberClue(v.toInt))
      }
      case GiveColor(p, t, c) => {
        playersM(p).clue(playersM(t),new ColorClue(Color.values()(c.toInt - 1)))

      }
      case _ =>
    }
    hanabi
  }

}
