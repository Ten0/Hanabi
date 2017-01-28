package com.ten.hanabi.core.plays;

import com.ten.hanabi.core.*;

public class DiscardPlay extends CardPlay {

	public DiscardPlay(Player player, int placement) {
		super(player, placement);
	}

	@Override
	public int getCluesAdded() {
		return 1;
	}

	@Override
	public String toString() {
		return getPlayer().toString() + " discards card " + getPlacement();
	}

}
