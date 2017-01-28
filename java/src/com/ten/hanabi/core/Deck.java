package com.ten.hanabi.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Deck implements Iterable<Card> {

	private final ArrayList<Card> cards;
	private final RuleSet ruleSet;

	public Deck(RuleSet ruleSet) {
		this.ruleSet = ruleSet;
		cards = new ArrayList<Card>();
		for (Color color : ruleSet.getEnabledColors()) {
			for (int number = 1; number <= 5; number++) {
				int count = number == 1 ? 3 : number == 5 ? 1 : 2;
				for (int i = 0; i < count; i++)
					cards.add(new Card(color, number));
			}
		}
		shuffle();
	}

	public void shuffle() {
		Collections.shuffle(cards);
	}

	public void shuffle(int fromId) {
		if (fromId == 0) {
			shuffle();
		} else {
			ArrayList<Card> cards2 = new ArrayList<Card>();
			for (int i = fromId; i < cards.size(); i++) {
				cards2.add(cards.remove(fromId));
			}
			Collections.shuffle(cards2);
			for (Card c : cards2)
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
		for (int i = 0; i < size(); i++) {
			s += getCard(i);
			if (i != size() - 1)
				s += " ";
		}
		return s;
	}

	public RuleSet getRuleSet() {
		return ruleSet;
	}
}
