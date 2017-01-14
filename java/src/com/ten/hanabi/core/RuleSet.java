package com.ten.hanabi.core;

public class RuleSet {

	public boolean multi = false;
	
	public RuleSet() {
		
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
}
