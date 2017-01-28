package com.ten.hanabi.core;

import com.ten.hanabi.core.clues.*;
import com.ten.hanabi.core.plays.*;

public class Player {

	private String name;
	private Hanabi hanabi;
	private int id;

	public Player() {

	}

	public Player(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name == null ? "Player " + (id + 1) : getName();
	}

	void setHanabi(Hanabi hanabi, int id) {
		if(this.hanabi == null) {
			this.hanabi = hanabi;
			this.id = id;
		} else if(this.hanabi != hanabi || this.id != id)
			throw new RuntimeException("You cannot reassociate an already associated player to a game.");
	}

	/*
	 * public Hand getCards() { return getCards(hanabi.getTurn()); } public Hand getCards(int untilTurn) { return new
	 * Hand(this, untilTurn); }
	 * 
	 * public int getNbOfCards() { return getCards().size(); }
	 */

	public Hanabi getHanabi() {
		return hanabi;
	}

	public int getId() {
		return id;
	}

	public void place(int cardId) {
		hanabi.savePlay(new PlacePlay(this, cardId));
	}

	public void discard(int cardId) {
		hanabi.savePlay(new DiscardPlay(this, cardId));
	}

	public void clue(Player toPlayer, Clue clue) {
		hanabi.savePlay(new CluePlay(this, toPlayer, clue));
	}
}
