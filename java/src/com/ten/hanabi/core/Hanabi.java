package com.ten.hanabi.core;

import java.util.ArrayList;
import java.util.List;

import com.ten.hanabi.core.exceptions.InvalidPlayException;
import com.ten.hanabi.core.plays.*;

public class Hanabi {

	private final RuleSet ruleSet;
	private final ArrayList<Player> players;
	private final Deck deck;
	private Variant variant = new Variant(this);

	public Hanabi(Player... players) {
		this(new RuleSet(), players);
	}

	public Hanabi(List<Player> players) {
		this(players.toArray(new Player[players.size()]));
	}

	public Hanabi(RuleSet ruleSet, List<Player> players) {
		this(ruleSet, new Deck(ruleSet), players.toArray(new Player[players.size()]));
	}

	public Hanabi(RuleSet ruleSet, Player... players) {
		this(ruleSet, new Deck(ruleSet), players);
	}

	public Hanabi(RuleSet ruleSet, Deck deck, List<Player> players) {
		this(ruleSet, deck, players.toArray(new Player[players.size()]));
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
		return getVariant().getPlay(turn);
	}

	public int getTurn() {
		return getVariant().getTurn();
	}

	public RuleSet getRuleSet() {
		return ruleSet;
	}

	public Situation getSituation() throws InvalidPlayException {
		return getSituation(getTurn());
	}

	public Situation getSituation(int turn) throws InvalidPlayException {
		return getVariant().getSituation(turn);
	}

	public void savePlay(Play play) throws InvalidPlayException {
		getVariant().savePlay(play);
	}

	public Variant getVariant() {
		return variant;
	}

	public void setVariant(Variant variant) {
		this.variant = variant;
	}

	public int getPlayingPlayerId(int turn) {
		return turn % getPlayerCount();
	}

	public Player getPlayingPlayer(int turn) {
		return getPlayer(getPlayingPlayerId(turn));
	}
}
