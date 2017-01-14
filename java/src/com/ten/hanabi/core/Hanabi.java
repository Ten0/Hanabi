package com.ten.hanabi.core;

import java.util.ArrayList;

public class Hanabi {
	
	private final Deck deck;
	private final ArrayList<Player> players;
	
	public Hanabi(Player... players) {
		this(new Deck(), players);
	}
	public Hanabi(Deck deck, Player...players) {
		this.deck = deck;
		
		this.players = new ArrayList<Player>();
		for(Player p : players) {
			p.setHanabi(this);
			this.players.add(p);
		}
		
		if(getPlayerCount() < 2)
			throw new RuntimeException("Too few players.");
	}
	
	public int getPlayerCount() {
		return players.size();
	}
	
	public Deck getDeck() {
		return deck;
	}
	
	public int getNbOfCardsPerPlayer() {
		return getPlayerCount() < 4 ? 5 : 4;
	}
}
