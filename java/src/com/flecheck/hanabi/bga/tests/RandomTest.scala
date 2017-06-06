package com.flecheck.hanabi.bga.tests

import com.flecheck.hanabi.bga.BGA
import com.ten.hanabi.ui.play.UIPlayManager

object RandomTest extends App{
  //val t = BGA.getGameById(27317631)
  //val t = BGA.getGameById(28015531)
  //val h = BGA.getGameById(28441973)
  
  //val h = BGA.getGameById(30709275)
  val h = BGA.getGameById(30820282)
  println(h)

  var upm: UIPlayManager = new UIPlayManager()
  upm.loadHanabi(h, 0)
}
