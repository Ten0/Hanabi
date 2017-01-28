package com.ten.hanabi.core.plays;

import com.ten.hanabi.core.*;

public abstract class CardPlay extends Play {

	private final int placement;

	CardPlay(Player player, int placement) {
		super(player);
		this.placement = placement;
	}

	@Override
	public int getNbCardsPicked() {
		return 1;
	}

	public int getPlacement() {
		return placement;
	}

}
