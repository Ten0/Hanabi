package com.ten.hanabi.tests;

import com.ten.hanabi.core.*;
import com.ten.hanabi.play.PlayManager;
import com.ten.hanabi.ui.play.*;

public class SeenGameTest {

	public static void main(String[] args) throws Exception {
		String[] playerNames = new String[] { "Thomas", "Alain", "Anne" };
		Player[] players = new Player[playerNames.length];
		UIPlayManager upm = new UIPlayManager();
		RealPlayer[] realPlayers = new RealPlayer[playerNames.length];
		for(int i = 0; i < playerNames.length; i++) {
			players[i] = new Player(playerNames[i]);
			realPlayers[i] = new RealPlayer(upm);
		}

		RuleSet ruleSet = new RuleSet();
		Deck deck = new Deck(ruleSet, false);
		Hanabi h = new Hanabi(ruleSet, deck, players);
		PlayManager pm = new PlayManager(h, realPlayers);

		pm.notifyPlay(); // Let's start ! :)

		// Display game
		pm.registerHanabiChangeListener(upm);
		pm.registerSituationChangeListener(upm);

		PlayFrame pf = new PlayFrame(upm);
		pf.setVisible(true);
	}
}
