package com.ten.hanabi.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.ten.hanabi.core.clues.Clue;
import com.ten.hanabi.core.exceptions.UnknownCardException;

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
			Card card = cards.get(i);
			if(card != null) {
				if(c.matches(card))
					ck.knowClue(c);
				else
					ck.knowNegativeClue(c);
			}
		}
	}

	public Card get(int id) {
		return cards.get(id);
	}

	public Collection<CardKnowlege> getKnowleges() {
		return Collections.unmodifiableCollection(cardsKnowlege);
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
			Card c = this.get(i);
			s += c == null ? "??" : c.toString();
			if(i != this.size() - 1)
				s += " ";
		}
		return s;
	}

	public String toDetailedString() {
		String s = "";
		for(int i = 0; i < this.size(); i++) {
			Card c = this.get(i);
			s += (c == null ? "??" : c.toString()) + "(" + getKnowlege(i) + ")";
			if(i != this.size() - 1)
				s += " ";
		}
		return s;
	}

	public int getConcernedCardsCount(Clue c) throws UnknownCardException {
		int count = 0;
		for(int i = 0; i < size(); i++) {
			Card card = cards.get(i);
			if(card != null) {
				if(c.matches(card))
					count++;
			} else
				throw new UnknownCardException();
		}
		return count;
	}
}
