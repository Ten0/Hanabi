package com.ten.hanabi.core.plays;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.clues.Clue;

public class CluePlay extends Play {
	
	private final Player toPlayer;
	private final Clue clue;

	public CluePlay(Player player, Player toPlayer, Clue clue) {
		super(player);
		this.toPlayer = toPlayer;
		this.clue = clue;
	}

	@Override
	public int getNbCardsPicked() {
		return 0;
	}

	@Override
	public int getCluesAdded() {
		return -1;
	}
	
	public Player getGiver() {
		return getPlayer();
	}

	public Player getReceiver() {
		return toPlayer;
	}

	public Clue getClue() {
		return clue;
	}

}
