package com.ten.hanabi.ui.play;

import java.util.HashSet;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.exceptions.InvalidPlayException;
import com.ten.hanabi.play.HanabiChangeListener;
import com.ten.hanabi.play.SituationChangeListener;

public class UIPlayManager implements SituationChangeListener, HanabiChangeListener {

	private Hanabi hanabi;
	private Situation situation;

	private final HashSet<SituationChangeListener> situationChangeListeners;
	private final HashSet<HanabiChangeListener> hanabiChangeListeners;

	public UIPlayManager() {
		situationChangeListeners = new HashSet<SituationChangeListener>();
		hanabiChangeListeners = new HashSet<HanabiChangeListener>();
	}

	public void loadHanabi(Hanabi h) throws InvalidPlayException {
		loadHanabi(h, h.getTurn());
	}

	public void loadHanabi(Hanabi h, int turn) throws InvalidPlayException {
		this.hanabi = h;
		this.situation = hanabi.getSituation(turn);
		situationChangeListeners.clear();
		this.notifyHanabiChange();
	}

	void goToTurn(int turn) throws InvalidPlayException {
		this.situation = hanabi.getSituation(turn);
		this.notifySituationChange();
	}

	void registerHanabiChangeListener(HanabiChangeListener hcl) {
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

	void registerSituationChangeListener(SituationChangeListener scl) {
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

	boolean nextTurn() throws InvalidPlayException {
		if(situation.getTurn() < hanabi.getTurn()) {
			goToTurn(situation.getTurn() + 1);
			return true;
		} else
			return false;
	}

	boolean previousTurn() throws InvalidPlayException {
		if(situation.getTurn() > 0) {
			goToTurn(situation.getTurn() - 1);
			return true;
		} else
			return false;
	}

	void goToEnd() throws InvalidPlayException {
		goToTurn(hanabi.getTurn());
	}

	@Override
	public void onHanabiChange(Hanabi hanabi) {
		try {
			loadHanabi(hanabi);
		} catch (InvalidPlayException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onSituationChange(Situation s) {
		try {
			situation = hanabi.getSituation();
		} catch (InvalidPlayException e) {
			throw new RuntimeException(e);
		}
		notifySituationChange();
	}
}
