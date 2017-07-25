package com.flecheck.hanabi.bga

import java.util

import com.ten.hanabi.core._
import com.ten.hanabi.core.clues.{ColorClue, NumberClue}
import com.ten.hanabi.core.plays.{DiscardPlay, PlacePlay}

import scala.util.matching.Regex
import scala.collection.JavaConverters._
import scala.collection.immutable.Seq
import scala.collection.mutable


case class BGALoadException (s: String) extends Exception(s)

object BGA {

  var cookies: Map[String, String] = Map("TournoiEnLignelang" -> "fr",
    "TournoiEnLigneuser" -> "84407922",
    "TournoiEnLigneauth" -> "f2256199a3cb5924cac9cd1def02a9ab")

  def getGameById(id: Int): Hanabi = {
    val content1: String = Utils.getUrl("https://fr.boardgamearena.com/gamereview?table=" + id.toString, cookies = cookies)
    
    val regUrl: Regex = """href="(?<url>.*?)" class="choosePlayerLink""".r
    val regPlays: Regex = """g_gamelogs = (?<json>.*);""".r
    val regSetup: Regex = """completesetup.*"socketio", (?<json>.*}), \{""".r

    val nUrl: String = regUrl findFirstMatchIn content1 match {
      case Some(x) => x.subgroups.head
      case None => throw BGALoadException("Can't match replay url regex for game " + id)
    }

    val content2 = Utils.getUrl("https://fr.boardgamearena.com/" + nUrl, cookies = cookies)
    val playsS: String = regPlays findFirstMatchIn content2 match {
      case Some(x) => x.subgroups.head
      case None => throw BGALoadException("Can't match plays regex for game " + id)
    }

    val setup: String = regSetup findFirstMatchIn content2 match {
      case Some(x) => x.subgroups.head
      case None => throw BGALoadException("Can't match setup regex for game " + id)
    }

    val (playerList,startingPlayer,playerOrder,multi,cardNumberVariant,handCards,deckCards,handFillOrder) = JsonParser.jsonToSetup(setup)
    val plays: Seq[Play] = JsonParser.jsonToPlays(playsS)

    val orderedPlayerList = playerOrder.map(j => playerList.find{case (id, _) => id == j}.get)
    val normalizedPlayerList = (orderedPlayerList ::: orderedPlayerList).dropWhile{case (id, _) => id != startingPlayer}.take(playerList.length)
    val startingPlayerPos = (playerOrder,(0 to playerOrder.size).toList).zipped.find{case (x,_) => x == startingPlayer}.get._2


    var playersM: mutable.Map[String, Player] = scala.collection.mutable.Map[String,Player]()
    var playersIdM: mutable.Map[Int, Player] = scala.collection.mutable.Map[Int,Player]()

    val players: util.List[Player] = normalizedPlayerList.map{ case (id,name) =>
      val p = new Player(name)
      playersM += (name -> p)
      playersIdM += (id.toInt -> p)
      p
    }.asJava
    val rs: RuleSet = new RuleSet(multi, cardNumberVariant, true)
    val deck: Deck = new Deck(rs, false)
    val hanabi: Hanabi = new Hanabi(rs,deck,players)
    val cardPlay: Map[String, (Int, Int)] = plays.filter{_.isInstanceOf[CardInfo]}.asInstanceOf[Seq[CardInfo]].map{ x => (x.card , x.cardInfo)}.toMap

    def fromBGAId(BGAid: String): Int = {
      val id = deck.size() - BGAid.toInt
      val nbCardsDealtAtStart = hanabi.getNbOfCardsPerPlayer*players.size
      var ret = id
      if(id < nbCardsDealtAtStart) {
        import com.flecheck.hanabi.bga.Utils.ExtendedInt
        val bgaPlayer = id / hanabi.getNbOfCardsPerPlayer // joueur 0,1,2... dans l'ordre des hand002 hand003 hand001
        val player = playersIdM(handFillOrder(bgaPlayer)).getId
        val handPos = id % hanabi.getNbOfCardsPerPlayer
        ret = player + handPos * players.size
      }
      ret
    }

    def fromBGACard(id: String, colorS: Int, numS: Int): (Card) = {
      var color = colorS.toInt - 1
      var num = numS.toInt
      if (num == 6) {
            val (color2, num2) = cardPlay.getOrElse(id, (-1,-1))
            color = color2-1; num = num2;
      }
      num match {
        case -1 =>
          null
        case _ =>
          new Card(Color.values()(color), num)
      }
    }

    for {
      (id,colorBGA,numBGA) <- deckCards
      card = fromBGACard(id,colorBGA,numBGA)
    } yield {
      deck.setCard(fromBGAId(id), card)
    }
    for {
      (id,colorBGA,numBGA) <- handCards
      card = fromBGACard(id,colorBGA,numBGA)
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
    var cardId = nbOfCardsPerPlayer*nPlayers
    try{
    plays.foreach {
      case PlayCard(p, c, _) => {
        val hand = hands.get(playersIdM(p).getId)
        val cardPos = hand.indexOf(fromBGAId(c))
        hand.remove(cardPos)
        hand.add(0, cardId)
        cardId += 1

        hanabi.savePlay(new PlacePlay(playersIdM(p), cardPos))
      }
      case DiscardCard(p, c, _) => {
        val hand = hands.get(playersIdM(p).getId)
        val cardPos = hand.indexOf(fromBGAId(c))
        hand.remove(cardPos)
        hand.add(0, cardId)
        cardId += 1

        hanabi.savePlay(new DiscardPlay(playersIdM(p), cardPos))

      }
      case GiveValue(p, t, v) => {
        playersM(p).clue(playersIdM(t),new NumberClue(v.toInt))
      }
      case GiveColor(p, t, c) => {
        playersM(p).clue(playersIdM(t),new ColorClue(Color.values()(c.toInt - 1)))

      }
      case _ =>
    }
    }catch {case e : Throwable => e.printStackTrace()}
    hanabi
  }

}
