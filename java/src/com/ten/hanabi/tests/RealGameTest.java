package com.ten.hanabi.tests;

import com.ten.hanabi.core.Hanabi;
import com.ten.hanabi.core.Player;
import com.ten.hanabi.play.PlayManager;
import com.ten.hanabi.ui.play.PlayFrame;
import com.ten.hanabi.ui.play.RealPlayer;
import com.ten.hanabi.ui.play.UIPlayManager;

public class RealGameTest {

	public static void main(String[] args) throws Exception {
		String[] playerNames = new String[] { "Thomas", "Alain", "Anne" };
		Player[] players = new Player[playerNames.length];
		UIPlayManager upm = new UIPlayManager();
		RealPlayer[] realPlayers = new RealPlayer[playerNames.length];
		for(int i = 0; i < playerNames.length; i++) {
			players[i] = new Player(playerNames[i]);
			realPlayers[i] = new RealPlayer(upm);
		}

		Hanabi h = new Hanabi(players);
		PlayManager pm = new PlayManager(h, realPlayers);

		pm.notifyPlay(); // Let's start ! :)

		// Display game
		pm.registerHanabiChangeListener(upm);
		pm.registerSituationChangeListener(upm);

		PlayFrame pf = new PlayFrame(upm);
		pf.setVisible(true);
	}
}
