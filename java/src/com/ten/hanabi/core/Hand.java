package com.ten.hanabi.core;

import java.util.ArrayList;
import java.util.Iterator;

import com.ten.hanabi.core.plays.*;

public class Hand implements Iterable<Card> {

	private final Hanabi hanabi;
	private final Player player;
	private final ArrayList<Card> cards;
	
	Hand(Player player) {
		this.player = player;
		this.hanabi = player.getHanabi();
		this.cards = new ArrayList<Card>();
	}
	
	void pick(Card c) {
		if(c == null) return;
		cards.add(0, c);
	}
	
	Card play(int id) {
		return cards.remove(id);
	}
	
	public Card get(int id) {
		return cards.get(id);
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
			if(i != this.size()-1) s += " ";
		}
		return s;
	}
}
