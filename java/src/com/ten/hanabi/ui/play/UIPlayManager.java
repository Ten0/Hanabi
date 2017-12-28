package com.ten.hanabi.ui.play;

import java.awt.Component;
import java.util.HashSet;

import javax.swing.JPanel;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.exceptions.InvalidPlayException;
import com.ten.hanabi.play.HanabiChangeListener;
import com.ten.hanabi.play.SituationChangeListener;

public class UIPlayManager implements SituationChangeListener, HanabiChangeListener {

	private Hanabi hanabi;
	private Situation situation;

	/** null if no card is selected */
	private Player selectedCardPlayer;
	/** -1 if no card is selected */
	private int selectedCardIdInHand = -1;

	private final HashSet<SituationChangeListener> situationChangeListeners;
	private final HashSet<HanabiChangeListener> hanabiChangeListeners;
	private final HashSet<SelectedCardChangeListener> selectedCardChangeListeners;

	public enum HiddenCardsMode {
		NONE, ALL, CURRENT, MANUAL
	}

	private HiddenCardsMode hiddenCardsMode = HiddenCardsMode.NONE;

	private Player playerWhoseCardsAreHidden = null; // Cards are hidden in the right hand side UI

	public UIPlayManager() {
		situationChangeListeners = new HashSet<SituationChangeListener>();
		hanabiChangeListeners = new HashSet<HanabiChangeListener>();
		selectedCardChangeListeners = new HashSet<SelectedCardChangeListener>();
	}

	private void setHanabi(Hanabi h, int turn) throws InvalidPlayException {
		this.hanabi = h;
		setSituation(hanabi.getSituation(turn));
	}

	public void loadHanabi(Hanabi h) throws InvalidPlayException {
		loadHanabi(h, h.getTurn());
	}

	public void loadHanabi(Hanabi h, int turn) throws InvalidPlayException {
		setHanabi(h, turn);
		this.notifyHanabiChange();
	}

	private void setSituation(Situation situation) {
		this.situation = situation;
		setSelectedCard(null, -1);
	}

	void goToTurn(int turn) throws InvalidPlayException {
		setSituation(hanabi.getSituation(turn));
		this.notifySituationChange();
	}

	private void setSelectedCard(Player player, int pos) {
		if(pos < 0)
			player = null;
		if(player == null)
			pos = -1;
		selectedCardPlayer = player;
		selectedCardIdInHand = pos;
	}

	public void selectCard(Player player, int pos) {
		setSelectedCard(player, pos);
		notifySelectedCardChange();
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
		this.notifySituationChange();
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
		this.notifySelectedCardChange();
	}

	void registerSelectedCardChangeListener(SelectedCardChangeListener sccl) {
		selectedCardChangeListeners.add(sccl);
		sccl.onSelectedCardChange(selectedCardPlayer, selectedCardIdInHand);
	}

	void unregisterSelectedCardChangeListener(SelectedCardChangeListener sccl) {
		selectedCardChangeListeners.remove(sccl);
	}

	private void notifySelectedCardChange() {
		selectedCardChangeListeners.parallelStream().forEach(sccl -> {
			sccl.onSelectedCardChange(selectedCardPlayer, selectedCardIdInHand);
		});
	}

	boolean nextTurn() throws InvalidPlayException {
		if(hanabi != null && situation.getTurn() < hanabi.getTurn()) {
			goToTurn(situation.getTurn() + 1);
			return true;
		} else
			return false;
	}

	boolean previousTurn() throws InvalidPlayException {
		if(hanabi != null && situation.getTurn() > 0) {
			goToTurn(situation.getTurn() - 1);
			return true;
		} else
			return false;
	}

	void goToEnd() throws InvalidPlayException {
		if(hanabi != null)
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
		if(s != null && s.getHanabi() != hanabi)
			throw new RuntimeException();
		setSituation(s);
		notifySituationChange();
	}

	public Situation getSituation() {
		return situation;
	}

	public Player getSelectedCardPlayer() {
		return selectedCardPlayer;
	}

	public int getSelectedCardIdInHand() {
		return selectedCardIdInHand;
	}

	public void unregister(Component comp) {
		if(comp instanceof JPanel) {
			for(Component comp2 : ((JPanel) comp).getComponents())
				unregister(comp2);
		}
		if(comp instanceof HanabiChangeListener) {
			unregisterHanabiChangeListener((HanabiChangeListener) comp);
		}
		if(comp instanceof SituationChangeListener) {
			unregisterSituationChangeListener((SituationChangeListener) comp);
		}
		if(comp instanceof SelectedCardChangeListener) {
			unregisterSelectedCardChangeListener((SelectedCardChangeListener) comp);
		}
	}

	public Hanabi getHanabi() {
		return hanabi;
	}

	void setHiddenCardsMode(HiddenCardsMode hcm) {
		if(hcm == HiddenCardsMode.MANUAL)
			throw new RuntimeException("setPlayerWhoseCardsAreHidden should be used instead");
		if(hcm != this.hiddenCardsMode) {
			this.hiddenCardsMode = hcm;
			this.playerWhoseCardsAreHidden = null;
			this.notifySituationChange();
		}
	}

	void setPlayerWhoseCardsAreHidden(Player player) {
		if(playerWhoseCardsAreHidden != player) {
			this.hiddenCardsMode = HiddenCardsMode.MANUAL;
			this.playerWhoseCardsAreHidden = player;
			this.notifySituationChange();
		}
	}

	public boolean areCardsHidden(Player p) {
		return hiddenCardsMode == HiddenCardsMode.ALL || p == playerWhoseCardsAreHidden // manual mode implicit
				|| (hiddenCardsMode == HiddenCardsMode.CURRENT && p == situation.getPlayingPlayer());
	}
}
