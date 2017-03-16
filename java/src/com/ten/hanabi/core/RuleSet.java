package com.ten.hanabi.core;

import java.util.ArrayList;

public class RuleSet {

	public final boolean multi;

	public RuleSet() {
		multi = false;
	}

	public RuleSet(boolean multi) {
		this.multi = multi;
	}

	public boolean isMultiEnabled() {
		return multi;
	}

	public boolean isColorEnabled(Color c) {
		return multi || c != Color.MULTI;
	}

	public int getNbOfCardsPerPlayer(int playerCount) {
		return playerCount < 4 ? 5 : 4;
	}

	public int getInitialNumberOfClues() {
		return 8;
	}

	public int getMaxNumberOfClues() {
		return getInitialNumberOfClues();
	}

	public int getNbStrikesUntilDeath() {
		return 3;
	}

	public ArrayList<Color> getEnabledColors() {
		ArrayList<Color> enabledColors = new ArrayList<Color>();
		for(Color c : Color.values())
			if(isColorEnabled(c))
				enabledColors.add(c);
		return enabledColors;
	}

	public int getNbTurnsPerPlayerAfterLastCard() {
		return 1;
	}

	public int getNbCardsForNumber(int n) {
		return n == 1 ? 3 : n == 5 ? 1 : 2;
	}

	public int getDeckSize() {
		return isMultiEnabled() ? 60 : 50;
	}

	public boolean canGiveEmptyClues() {
		return true;
	}
}
