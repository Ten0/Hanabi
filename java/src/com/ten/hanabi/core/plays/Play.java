package com.ten.hanabi.core.plays;

import com.ten.hanabi.core.*;

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
}
