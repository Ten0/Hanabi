package com.ten.hanabi.core;

import java.util.ArrayList;
import java.util.Iterator;

import com.ten.hanabi.core.clues.Clue;

public class Hand implements Iterable<Card> {

	private final Hanabi hanabi;
	private final Player player;
	private final ArrayList<Card> cards;

	private final ArrayList<CardKnowlege> cardsKnowlege;

	Hand(Player player) {
		this.player = player;
		this.hanabi = player.getHanabi();
		this.cards = new ArrayList<Card>();
		this.cardsKnowlege = new ArrayList<CardKnowlege>();
	}

	void pick(Situation s, Card c) {
		cards.add(0, c);
		cardsKnowlege.add(0, new CardKnowlege(s, s.getTurn()));
	}

	Card play(int id) {
		cardsKnowlege.remove(id);
		return cards.remove(id);
	}

	void receiveClue(Clue c) {
		for(int i = 0; i < size(); i++) {
			CardKnowlege ck = cardsKnowlege.get(i);
			if(c.matches(cards.get(i)))
				ck.knowClue(c);
			else
				ck.knowNegativeClue(c);
		}
	}

	public Card get(int id) {
		return cards.get(id);
	}

	public CardKnowlege getKnowlege(int id) {
		return cardsKnowlege.get(id);
	}

	@Override
	public Iterator<Card> iterator() {
		return cards.iterator();
	}

	public int size() {
		return cards.size();
	}

	public Player getPlayer() {
		return player;
	}

	public Hanabi getHanabi() {
		return hanabi;
	}

	@Override
	public String toString() {
		String s = "";
		for(int i = 0; i < this.size(); i++) {
			s += this.get(i);
			if(i != this.size() - 1)
				s += " ";
		}
		return s;
	}

	public String toDetailedString() {
		String s = "";
		for(int i = 0; i < this.size(); i++) {
			s += this.get(i) + "(" + getKnowlege(i) + ")";
			if(i != this.size() - 1)
				s += " ";
		}
		return s;
	}
}
