package com.ten.hanabi.ui.play;

import com.ten.hanabi.core.Hanabi;
import com.ten.hanabi.core.Player;
import com.ten.hanabi.core.Situation;
import com.ten.hanabi.core.Variant;
import com.ten.hanabi.core.clues.Clue;
import com.ten.hanabi.core.exceptions.InvalidPlayException;
import com.ten.hanabi.core.plays.*;
import com.ten.hanabi.play.PlayingEntity;
import com.ten.hanabi.play.SituationChangeListener;
import com.ten.hanabi.ui.ExceptionDialog;
import com.ten.hanabi.ui.play.UIPlayManager.PanelClick;

public class RealPlayer extends PlayingEntity
		implements SituationChangeListener, SelectedCardChangeListener, SelectedClueChangeListener, PanelClickListener {

	private final UIPlayManager upm;
	private Variant currVariant;
	private int currTurn;
	private Player currPlayer;

	public RealPlayer(UIPlayManager upm) {
		this.upm = upm;
	}

	@Override
	protected void doAskPlay(Variant v, int turn) throws InvalidPlayException {
		// Make sure UI is on the right variant and turn
		currVariant = v;
		currTurn = turn;
		Hanabi h = v.getHanabi();
		currPlayer = v.getPlayingPlayer();
		h.setVariant(v);
		upm.loadHanabi(h, turn);

		// Setup UI for play mode
		upm.selectClue(null);
		upm.selectCard(null, -1);

		upm.registerSituationChangeListener(this);
	}

	@Override
	public void onSituationChange(Situation s) {
		if(s.getVariant() != this.currVariant || s.getTurn() != currTurn) {
			upm.unregisterSelectedCardChangeListener(this);
			upm.unregisterSelectedClueChangeListener(this);
			upm.unregisterPanelClickListener(this);
		} else {
			upm.registerSelectedCardChangeListener(this);
			upm.registerSelectedClueChangeListener(this);
			upm.registerPanelClickListener(this);
		}
	}

	private void tryPlay(Play play) {
		try {
			currVariant.savePlay(play);
			notifyPlay();
			upm.unregister(this);
		} catch (InvalidPlayException ex) {
			new ExceptionDialog(upm.getPlayFrame(), "Invalid play", ex.getMessage(), ex).setVisible(true);
		}
	}

	@Override
	public void onPanelClick(PanelClick panel) {
		if(upm.getSelectedCardPlayer() != currPlayer)
			return;
		int cardId = upm.getSelectedCardIdInHand();

		Play play = null;
		if(panel == UIPlayManager.PanelClick.BOARD) {
			play = new PlacePlay(currPlayer, cardId);
		} else if(panel == UIPlayManager.PanelClick.DISCARD) {
			play = new DiscardPlay(currPlayer, cardId);
		}
		tryPlay(play);
	}

	@Override
	public void onSelectedClueChange(Clue clue) {
		if(clue != null) {
			Player selectedPlayer = upm.getSelectedCardPlayer();
			if(selectedPlayer != null && selectedPlayer != currPlayer)
				tryPlay(new CluePlay(currPlayer, selectedPlayer, clue));
		}
	}

	@Override
	public void onSelectedCardChange(Player player, int pos) {
		if(player != null && player != currPlayer) {
			Clue selectedClue = upm.getSelectedClue();
			if(selectedClue != null)
				tryPlay(new CluePlay(currPlayer, player, selectedClue));
		}
	}

}
