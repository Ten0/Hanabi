package com.flecheck.hanabi.bga

trait Play
trait CardInfo extends Play {val card: String ;val cardInfo: (Int,Int)}
case class PlayCard(player: Int ,card: String, cardInfo: (Int, Int)) extends CardInfo
case class GiveColor(player: String, target: Int, color: String) extends Play
case class GiveValue(player: String, target: Int, value: String) extends Play
case class DiscardCard(player: Int ,card: String, cardInfo: (Int, Int)) extends CardInfo
case class RevealCard(card: String, cardInfo: (Int,Int)) extends CardInfo