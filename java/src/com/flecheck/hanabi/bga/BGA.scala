package com.flecheck.hanabi.bga

import scala.util.matching.Regex
import scala.util.matching.Regex.Match

object BGA {

  var cookies: Map[String, String] = Map("_ga" -> "GA1.2.245094703.1485718965", "TournoiEnLigneauth" -> "f2256199a3cb5924cac9cd1def02a9ab", "TournoiEnLigneuser" -> "84407922", "TournoiEnLignelang" -> "fr")

  def getGameById(id: Int): Option[String] = {
    val content1: String = Utils.getUrl("https://fr.boardgamearena.com/#!gamereview?table=" + id.toString, cookies = cookies)
    val reg: Regex = "href=\"(?<url>.*?)\" class=\"choosePlayerLink".r
    val nUrlR: Option[Match] = reg findFirstMatchIn content1
    val nUrl: String = nUrlR match {
      case Some(x) =>
        x.toString()
      case None =>
        println("Error: Can't load game n°" + id)
        println(content1)
        return None
    }
    val content = Utils.getUrl("https://fr.boardgamearena.com/" + nUrl, cookies = cookies)
    Some(nUrl)
  }
}
