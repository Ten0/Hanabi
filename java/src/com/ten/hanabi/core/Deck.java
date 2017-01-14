package com.ten.hanabi.core;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {

	public ArrayList<Card> cards;
	
	public Deck() {
		cards = new ArrayList<Card>();
		for(Color color : Color.values()) {
			for(int number = 1; number <= 5; number++) {
				int count = number == 1 ? 3 : number == 5 ? 1 : 2;
				for(int i = 0; i < count; i++)
					cards.add(new Card(color, number));
			}
		}
		shuffle();
	}
	
	public void shuffle() {
		Collections.shuffle(cards);
	}
	public void shuffle(int fromId) {
		if(fromId == 0) {
			shuffle();
		}
		else {
			ArrayList<Card> cards2 = new ArrayList<Card>();
			for(int i = fromId; i < cards.size(); i++) {
				cards2.add(cards.remove(fromId));
			}
			Collections.shuffle(cards2);
			for(Card c : cards2) cards.add(c);
		}
	}
}
