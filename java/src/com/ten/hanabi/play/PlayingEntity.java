package com.ten.hanabi.play;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.exceptions.InvalidPlayException;

public abstract class PlayingEntity {
	protected Hanabi hanabi;
	protected PlayManager playManager;

	void setPlayManager(PlayManager pm) {
		assert (playManager == null);
		playManager = pm;
		hanabi = pm.getHanabi();
	}

	public void askPlay(Variant v, int turn) throws InvalidPlayException {
		assert (v.getHanabi() == hanabi);
		doAskPlay(v, turn);
	}

	public void askPlay() throws InvalidPlayException {
		askPlay(hanabi.getVariant(), hanabi.getVariant().getTurn());
	}

	public void askPlay(Variant v) throws InvalidPlayException {
		askPlay(v, v.getTurn());
	}

	public void askPlay(int turn) throws InvalidPlayException {
		askPlay(hanabi.getVariant(), turn);
	}

	protected abstract void doAskPlay(Variant v, int turn) throws InvalidPlayException;

	public void notifyPlay() throws InvalidPlayException {
		playManager.notifyPlay();
	}

	/** What to do when the play manager asks to stop searching for something to play */
	public abstract void cancelPlay();

}
