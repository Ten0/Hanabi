package com.ten.hanabi.core.plays;

import com.ten.hanabi.core.*;

public class PlacePlay extends CardPlay {

	public PlacePlay(Player player, int placement) {
		super(player, placement);
	}

	@Override
	public int getCluesAdded() {
		return 0;
	}

}
