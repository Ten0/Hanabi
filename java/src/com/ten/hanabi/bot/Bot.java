package com.ten.hanabi.bot;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.exceptions.InvalidPlayException;
import com.ten.hanabi.play.PlayingEntity;

public abstract class Bot extends PlayingEntity {

	@Override
	protected void doAskPlay(Variant v, int turn) throws InvalidPlayException {
		play(v, turn);
		notifyPlay();
	}

	protected abstract void play(Variant v, int turn) throws InvalidPlayException;

	@Override
	public void cancelPlay() {
		// Nothing to do for simple bots
	}
}
