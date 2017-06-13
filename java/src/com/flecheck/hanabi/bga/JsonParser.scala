package com.flecheck.hanabi.bga

import org.json4s._
import org.json4s.native.JsonMethods._

import scala.collection.immutable.Seq

object JsonParser {

  implicit class Regex(sc: StringContext) {
    def r = new util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }


  case class Card(id: String, color: String, num: String)
  def jsonToSetup(raw: String): (List[(String, String)], String, List[String], Boolean, Boolean, List[(String, Int, Int)], List[(String, Int, Int)], List[Int]) = {
    val json = parse(raw)
    implicit val formats = DefaultFormats

    val JObject(player) = json \ "players"
    val playerList = player.map{ case (_,a) => ((a \ "id").extract[String] , (a \ "name").extract[String] )  }

    val JArray(order) = json \ "playerorder"
    val playerOrder = order.map(_.extract[String])

    val hands = json filterField{ case (r"hand\d*",_) => true case _ => false}
    val handCards = for {
      JField(_,JObject(hand)) <- hands
      JField(_,card) <- hand
      id = card \ "id"
      color = card \ "type"
      num = card \ "type_arg"
    } yield { (id.extract[String] ,color.extract[String].toInt ,num.extract[String].toInt )}

    val handFillOrder = for { (name, _) <- hands } yield name.substring(4).toInt;

    val JObject(deck) = json \ "deck"
    val deckCards = for {
      (_,card) <- deck
      id = card \ "id"
      color = card \ "type"
      num = card \ "type_arg"
    } yield { (id.extract[String] ,color.extract[String].toInt ,num.extract[String].toInt )}

    val startingPlayer = (json \ "gamestate" \ "active_player").extract[String]

    val JObject(typeGameI) = (json \ "colors")
    val multi = typeGameI.length match {case 5 => false; case 6 => true}
    val cardNumberVariant = handCards.length/playerList.length match {case 3 => true; case 6 => true; case _ => false}

    (playerList,startingPlayer,playerOrder,multi,cardNumberVariant,handCards,deckCards,handFillOrder)

  }
  def jsonToPlays(raw: String): Seq[Play] = {
    implicit val formats = DefaultFormats

    val playTypeList = List("giveColor","playCard","missCard","giveValue","discardCard","revealCards")

    val JObject(json) = parse(raw) \ "data" \ "data" \\ "data"
    val plays = for {
      (_,JArray(playArr)) <- json
      playObj <- playArr
      playT = (playObj \ "type").extract[String]
      if playTypeList contains playT
      play = playObj \ "args"
      out <- parsePlay(playT,play)


      } yield { out }

  plays
  }

  def parsePlay(playT: String,play: JValue): List[Play] = {
    implicit val formats = DefaultFormats
    playT match {
      case "giveColor" => List(GiveColor((play \ "player_name").extract[String], (play \ "player_id").extract[String].toInt , (play \ "color").extract[String]))
      case "giveValue" => List(GiveValue((play \ "player_name").extract[String], (play \ "player_id").extract[String].toInt , (play \ "value").extract[String]))
      case "playCard" => List(PlayCard((play \ "player_id").extract[String].toInt, (play \ "card_id").extract[String], ((play \ "color").extract[String].toInt, (play \ "value").extract[String].toInt)))
      case "missCard" => List(PlayCard((play \ "player_id").extract[String].toInt, (play \ "card_id").extract[String], ((play \ "color").extract[String].toInt, (play \ "value").extract[String].toInt)))
      case "discardCard" => List(DiscardCard((play \ "player_id").extract[String].toInt, (play \ "card_id").extract[String], ((play \ "color").extract[String].toInt, (play \ "value").extract[String].toInt)))
      case "revealCards" => {
        val JObject(a) = play \ "cards"
        for {
          (_,card)<- a
          id = card \ "id"
          color = card \ "type"
          num = card \ "type_arg"
        } yield { RevealCard(id.extract[String] ,(color.extract[String].toInt , num.extract[String].toInt))}
      }
    }
  }

}
