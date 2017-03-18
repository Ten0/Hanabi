package com.ten.hanabi.tests;

import com.ten.hanabi.bot.*;
import com.ten.hanabi.core.*;
import com.ten.hanabi.play.PlayManager;
import com.ten.hanabi.ui.play.PlayFrame;
import com.ten.hanabi.ui.play.UIPlayManager;

public class BotTest {

	public static void main(String[] args) throws Exception {
		Player[] players = new Player[4];
		Bot[] bots = new Bot[players.length];
		for(int i = 0; i < players.length; i++) {
			players[i] = new Player("Bot " + (i + 1));
			bots[i] = new StupidBot();
		}

		Hanabi h = new Hanabi(players);
		PlayManager pm = new PlayManager(h, bots);

		UIPlayManager upm = new UIPlayManager();
		pm.registerHanabiChangeListener(upm);
		pm.registerSituationChangeListener(upm);

		PlayFrame pf = new PlayFrame(upm);
		pf.setVisible(true);

		pm.notifyPlay();
	}

}
