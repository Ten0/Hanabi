package com.ten.hanabi.core;

import java.util.ArrayList;

public class RuleSet {

	private final boolean multi;
	private final boolean multiIsNormalColor;
	private final boolean cardNumberVariant;
	private final boolean emptyCluesAllowed;

	public RuleSet() {
		multi = false;
		multiIsNormalColor = false;
		cardNumberVariant = false;
		emptyCluesAllowed = false;
	}

	public RuleSet(boolean multi, boolean multiIsNormalColor, boolean cardNumberVariant, boolean emptyCluesAllowed) {
		this.multi = multi;
		this.multiIsNormalColor = multiIsNormalColor;
		this.cardNumberVariant = cardNumberVariant;
		this.emptyCluesAllowed = emptyCluesAllowed;
	}

	public boolean isMultiEnabled() {
		return multi;
	}

	public boolean isColorEnabled(Color c) {
		return (c != Color.MULTI && c != Color.MULTI_6TH_COLOR) || (multi
				&& ((!multiIsNormalColor && c == Color.MULTI) || (multiIsNormalColor && c == Color.MULTI_6TH_COLOR)));
	}

	public int getNbOfCardsPerPlayer(int playerCount) {
		if(cardNumberVariant)
			return 8 - playerCount;
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
		return emptyCluesAllowed;
	}
}
