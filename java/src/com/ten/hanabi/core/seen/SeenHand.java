package com.ten.hanabi.core.seen;

import java.util.ArrayList;

import com.ten.hanabi.core.*;

public class SeenHand {

	private final Player player;
	private final ArrayList<Integer> cardsPos;
	private final SeenHanabi seenHanabi;

	SeenHand(SeenHanabi seenHanabi, Player player) {
		this.seenHanabi = seenHanabi;
		this.player = player;
		this.cardsPos = new ArrayList<Integer>();
	}

	void pick(int position) {
		cardsPos.add(0, position);
	}

	int play(int id, Card c) {
		seenHanabi.getDeck().setCard(cardsPos.get(id), c);
		return cardsPos.remove(id);
	}

	public int size() {
		return cardsPos.size();
	}

	public Player getPlayer() {
		return player;
	}
}
