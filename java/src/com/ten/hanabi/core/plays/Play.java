package com.ten.hanabi.core.plays;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.exceptions.*;

public abstract class Play {
	
	protected final Player player;
	
	Play(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

	public abstract int getNbCardsPicked();
	
	/** Negative when consuming clues, positive when restoring them. */
	public abstract int getCluesAdded();
	
	@Override
	public abstract String toString();
	
	public final boolean isValid(Situation s) {
		try {
			checkValidity(s);
			return true;
		} catch (InvalidPlayException e) {
			return false;
		}
	}
	
	public void checkValidity(Situation s) throws InvalidPlayException {
		if(s.getHanabi() != player.getHanabi())
			throw new InvalidPlayException(this, "Hanabi instance is not the same");
		if(s.getTurn()%s.getHanabi().getPlayerCount() != this.getPlayer().getId())
			throw new InvalidPlayException(this, "Not this player's turn");
		int newClues = s.getClues() + this.getCluesAdded();
		if(newClues < 0) throw new InvalidPlayException(this, "clues<0");
		int maxClues = s.getHanabi().getRuleSet().getMaxNumberOfClues();
		if(s.getClues() > maxClues) throw new InvalidPlayException(this, "clues>"+maxClues);
		if(s.isGameOver()) throw new InvalidPlayException(this, "Game is already over");
	}
}
