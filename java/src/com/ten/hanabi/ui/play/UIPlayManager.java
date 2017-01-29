package com.ten.hanabi.ui.play;

import java.util.HashSet;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.exceptions.InvalidPlayException;

public class UIPlayManager {

	Hanabi hanabi;
	Situation situation;

	private final HashSet<SituationChangeListener> situationChangeListeners;
	private final HashSet<HanabiChangeListener> hanabiChangeListeners;

	UIPlayManager() {
		situationChangeListeners = new HashSet<SituationChangeListener>();
		hanabiChangeListeners = new HashSet<HanabiChangeListener>();
	}

	void loadHanabi(Hanabi h) throws InvalidPlayException {
		loadHanabi(h, h.getTurn());
	}

	void loadHanabi(Hanabi h, int turn) throws InvalidPlayException {
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
}