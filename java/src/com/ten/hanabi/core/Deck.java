package com.ten.hanabi.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.ten.hanabi.core.exceptions.InvalidDeckException;

public class Deck implements Iterable<Card> {

	private final ArrayList<Card> cards;
	private final RuleSet ruleSet;

	private boolean locked = false;

	public Deck(RuleSet ruleSet) {
		this(ruleSet, true);
	}

	public Deck(RuleSet ruleSet, boolean lock) {
		this.ruleSet = ruleSet;
		this.cards = new ArrayList<Card>();
		if(lock) {
			try {
				autoComplete(true);
			} catch (InvalidDeckException e) {
				// Impossible puisque le jeu est vide à la base
				throw new RuntimeException(e);
			}
		}
	}

	public void lock() throws InvalidDeckException {
		checkCoherence();
		locked = true;
	}

	private void checkUnlocked() {
		if(locked)
			throw new RuntimeException("Trying to edit a locked deck.");
	}

	private void checkCoherence() throws InvalidDeckException {
		checkCoherence(false);
	}

	public void autoComplete() throws InvalidDeckException {
		autoComplete(false);
	}

	public void autoComplete(boolean lock) throws InvalidDeckException {
		checkUnlocked();
		checkCoherence(true);
		if(lock)
			locked = true;
	}

	private void checkCoherence(boolean autoComplete) throws InvalidDeckException {
		int startSize = this.size();
		HashMap<Color, HashMap<Integer, Integer>> counts = new HashMap<Color, HashMap<Integer, Integer>>();
		for(Card c : this) {
			if(counts.get(c.getColor()) == null)
				counts.put(c.getColor(), new HashMap<Integer, Integer>());
			HashMap<Integer, Integer> cc = counts.get(c.getColor());
			cc.put(c.getNumber(), cc.getOrDefault(c.getNumber(), 0) + 1);
		}
		int neededCardCount = 0;
		for(Color color : ruleSet.getEnabledColors()) {
			HashMap<Integer, Integer> cc = counts.getOrDefault(color, new HashMap<Integer, Integer>());
			for(int number = 1; number <= 5; number++) {
				int neededCount = ruleSet.getNbCardsForNumber(number);
				int count = cc.getOrDefault(number, 0);
				neededCardCount += neededCount;
				if(autoComplete && count < neededCount) {
					while(count < neededCount) {
						this.add(new Card(color, number));
						count++;
					}
				} else if(count != neededCount)
					throw new InvalidDeckException();
			}
		}
		if(autoComplete)
			this.shuffle(startSize);
		if(neededCardCount != this.size())
			throw new InvalidDeckException();
	}

	/**
	 *
	 * @param id
	 *            The place to put the card
	 * @param card
	 *            The card you want to put at that place
	 * @return Whether there was no card at that position (false if it replaced a card)
	 */
	public boolean setCard(int id, Card card) {
		checkUnlocked();
		while(cards.size() <= id)
			cards.add(null);
		return cards.set(id, card) == null;
	}

	public void add(Card card) {
		checkUnlocked();
		cards.add(card);
	}

	public void shuffle() {
		checkUnlocked();
		Collections.shuffle(cards);
	}

	public void shuffle(int fromId) {
		if(fromId == this.size()) {
			return;
		} else if(fromId == 0) {
			shuffle();
		} else {
			checkUnlocked();
			ArrayList<Card> cards2 = new ArrayList<Card>();
			for(int i = fromId; i < cards.size(); i++) {
				cards2.add(cards.remove(fromId));
			}
			Collections.shuffle(cards2);
			for(Card c : cards2)
				cards.add(c);
		}
	}

	public Card getCard(int id) {
		return cards.get(id);
	}

	public int size() {
		return cards.size();
	}

	@Override
	public Iterator<Card> iterator() {
		return Collections.unmodifiableCollection(cards).iterator();
	}

	@Override
	public String toString() {
		String s = "";
		for(int i = 0; i < size(); i++) {
			s += getCard(i);
			if(i != size() - 1)
				s += " ";
		}
		return s;
	}

	public RuleSet getRuleSet() {
		return ruleSet;
	}
}
