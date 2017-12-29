package com.ten.hanabi.play;

import java.util.HashSet;

import com.ten.hanabi.core.Hanabi;
import com.ten.hanabi.core.Situation;
import com.ten.hanabi.core.Variant;
import com.ten.hanabi.core.exceptions.InvalidPlayException;

public class PlayManager {

	private Hanabi hanabi;
	private Situation situation;

	private final HashSet<SituationChangeListener> situationChangeListeners;
	private final HashSet<HanabiChangeListener> hanabiChangeListeners;

	private PlayingEntity[] players;
	private PlayingEntity currentPlayer = null;

	public PlayManager(Hanabi hanabi, PlayingEntity... players) {
		this.hanabi = hanabi;
		try {
			this.situation = hanabi.getVariant().getSituation();
		} catch (InvalidPlayException e) {
			// Should never happen because game shouldn't have started
			throw new RuntimeException(e);
		}
		this.players = players;
		for(PlayingEntity pe : players)
			pe.setPlayManager(this);

		situationChangeListeners = new HashSet<SituationChangeListener>();
		hanabiChangeListeners = new HashSet<HanabiChangeListener>();
	}

	public Hanabi getHanabi() {
		return hanabi;
	}

	public void notifyPlay() throws InvalidPlayException {
		Variant v = hanabi.getVariant();
		situation = v.getSituation();
		notifySituationChange();
		if(!situation.isGameOver()) {
			currentPlayer = players[situation.getPlayingPlayerId()];
			currentPlayer.askPlay();
		} else
			currentPlayer = null;
	}

	public void registerHanabiChangeListener(HanabiChangeListener hcl) {
		hanabiChangeListeners.add(hcl);
		hcl.onHanabiChange(hanabi);
	}

	void unregisterHanabiChangeListener(HanabiChangeListener hcl) {
		hanabiChangeListeners.remove(hcl);
	}

	private void notifyHanabiChange() {
		hanabiChangeListeners.parallelStream().forEach(hcl -> {
			hcl.onHanabiChange(hanabi);
		});
	}

	public void registerSituationChangeListener(SituationChangeListener scl) {
		situationChangeListeners.add(scl);
		scl.onSituationChange(situation);
	}

	void unregisterSituationChangeListener(SituationChangeListener scl) {
		situationChangeListeners.remove(scl);
	}

	private void notifySituationChange() {
		situationChangeListeners.parallelStream().forEach(scl -> {
			scl.onSituationChange(situation);
		});
	}

	public void rollback(int turn) {
		if(currentPlayer != null)
			currentPlayer.cancelPlay();
		getHanabi().getVariant().rollback(turn);
		try {
			notifyPlay(); // Restart playing
		} catch (InvalidPlayException e) {
			throw new RuntimeException(e); // Shouldn't happen after rollback : previous game state should be valid
		}
		notifySituationChange();
	}

	public void rollback() {
		rollback(getHanabi().getVariant().getTurn() - 1);
	}
}
