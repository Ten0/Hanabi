package com.ten.hanabi.core;

import java.util.ArrayList;

import com.ten.hanabi.core.exceptions.InvalidPlayException;
import com.ten.hanabi.core.plays.*;

public class Hanabi {

	private final Deck deck;
	private final RuleSet ruleSet;
	private final ArrayList<Player> players;

	private ArrayList<Play> plays = new ArrayList<Play>();

	public Hanabi(Player... players) {
		this(new RuleSet(), players);
	}

	public Hanabi(RuleSet ruleSet, Player... players) {
		this(ruleSet, new Deck(ruleSet), players);
	}

	public Hanabi(RuleSet ruleSet, Deck deck, Player... players) {
		this.ruleSet = ruleSet;
		this.deck = deck;
		if(deck.getRuleSet() != ruleSet)
			throw new RuntimeException("Deck was created with a different ruleset.");

		this.players = new ArrayList<Player>();
		int playerId = 0;
		for(Player p : players) {
			p.setHanabi(this, playerId++);
			this.players.add(p);
		}

		if(getPlayerCount() < 2)
			throw new RuntimeException("Too few players.");
	}

	public int getPlayerCount() {
		return players.size();
	}

	public Player getPlayer(int id) {
		return players.get(id);
	}

	public Player[] getPlayers() {
		return players.toArray(new Player[players.size()]);
	}

	public Deck getDeck() {
		return deck;
	}

	public int getNbOfCardsPerPlayer() {
		return ruleSet.getNbOfCardsPerPlayer(getPlayerCount());
	}

	public Play getPlay(int turn) {
		return plays.get(turn - 1);
	}

	public int getTurn() {
		return plays.size();
	}

	public RuleSet getRuleSet() {
		return ruleSet;
	}

	public Situation getSituation() throws InvalidPlayException {
		return getSituation(getTurn());
	}

	public Situation getSituation(int turn) throws InvalidPlayException {
		return new Situation(this, turn);
	}

	public boolean savePlay(Play play) {
		boolean valid = false;
		try {
			if(getSituation().canPlay(play))
				valid = true;
		} catch (InvalidPlayException e) {
		}

		plays.add(play);
		return valid;
	}
}
