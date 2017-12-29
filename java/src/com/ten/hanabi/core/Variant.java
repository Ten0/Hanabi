package com.ten.hanabi.core;

import java.util.ArrayList;
import java.util.TreeMap;

import com.ten.hanabi.core.exceptions.InvalidPlayException;
import com.ten.hanabi.core.plays.Play;

public class Variant {
	private final Hanabi hanabi;
	private ArrayList<Play> plays = new ArrayList<Play>();
	private TreeMap<Integer, Situation> situationDP;

	public Variant(Hanabi hanabi) {
		this.hanabi = hanabi;
	}

	public Hanabi getHanabi() {
		return hanabi;
	}

	public Play getPlay(int turn) {
		if(turn == 0) { return null; }
		return plays.get(turn - 1);
	}

	public int getTurn() {
		return plays.size();
	}

	public Situation getSituation() throws InvalidPlayException {
		return getSituation(getTurn());
	}

	public Situation getSituation(int turn) throws InvalidPlayException {
		if(hanabi.getDeck().isLocked()) {
			if(situationDP == null)
				situationDP = new TreeMap<Integer, Situation>();
			if(!situationDP.containsKey(turn)) {
				situationDP.put(turn, new Situation(this, turn));
			}
			return situationDP.get(turn);
		} else {
			return new Situation(this, turn);
		}
	}

	public void savePlay(Play play) throws InvalidPlayException {
		getSituation().checkPlayValidity(play);
		plays.add(play);
	}

	public int getPlayingPlayerId() {
		return hanabi.getPlayingPlayerId(getTurn());
	}

	public Player getPlayingPlayer() {
		return hanabi.getPlayingPlayer(getTurn());
	}
}
