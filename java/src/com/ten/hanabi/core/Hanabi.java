package com.ten.hanabi.core;

import java.util.ArrayList;

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
	public Hanabi(RuleSet ruleSet, Deck deck, Player...players) {
		this.ruleSet = ruleSet;
		this.deck = deck;
		if(deck.getRuleSet() != ruleSet) throw new RuntimeException("Deck was created with a different ruleset.");
		
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
	
	Play getPlay(int id) {
		return plays.get(id);
	}
	
	public int getTurn() {
		return plays.size();
	}
	public RuleSet getRuleSet() {
		return ruleSet;
	}
	
	public void savePlay(Play play) {
		if(getTurn()%getPlayerCount() != play.getPlayer().getId())
			throw new RuntimeException("It isn't your turn!");
		plays.add(play);
	}
	
	/*public int getNbClues() {
		return getNbClues(getTurn());
	}
	
	public int getNbClues(int turn) {
		int maxClues = ruleSet.getInitialNumberOfClues();
		int nClues = maxClues;
		for(int cTurn = 0; cTurn < turn; cTurn++) {
			nClues += getPlay(cTurn).getCluesAdded();
			if(nClues > maxClues || nClues < 0) {
				throw new RuntimeException("Clue count went over or under limits at turn "+cTurn+": "+nClues);
			}
		}
		return nClues;
	}*/
}
