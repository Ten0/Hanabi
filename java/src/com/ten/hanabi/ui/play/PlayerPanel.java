package com.ten.hanabi.ui.play;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.util.Timer;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.plays.*;
import com.ten.hanabi.play.SituationChangeListener;
import com.ten.hanabi.ui.Utils;

import java.awt.Font;

public class PlayerPanel extends JPanel implements SituationChangeListener {
	private Player player;

	private JPanel cardsPanel;
	private JLabel nameLabel;

	private Situation situation;

	private Timer timer;

	/**
	 * Create the panel.
	 */
	public PlayerPanel(UIPlayManager upm, Player p) {
		setLayout(new BorderLayout(0, 0));
		setBorder(false);

		JPanel namePanel = new JPanel();
		namePanel.setBorder(new EmptyBorder(5, 5, 3, 5));
		add(namePanel, BorderLayout.NORTH);
		namePanel.setLayout(new BorderLayout(0, 0));

		nameLabel = new JLabel(p == null ? "<Player name>" : p.toString());
		nameLabel.setFont(new Font("Lato Black", Font.BOLD, 18));
		namePanel.add(nameLabel, BorderLayout.WEST);

		cardsPanel = new JPanel();
		cardsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(cardsPanel, BorderLayout.CENTER);
		cardsPanel.setLayout(new GridLayout(1, 5, 5, 0));

		player = p;

		upm.registerSituationChangeListener(this);
	}

	private void setCards(Situation s) {
		cardsPanel.removeAll();
		if(s != null) {
			Hand h = s.getHand(player);
			for(int i = 0; i < h.size(); i++) {
				Card c = h.get(i);
				CardKnowlege ck = h.getKnowlege(i);
				JLabel cardP = new JLabel(new ImageIcon(Utils.getCardImage(c, ck)));
				cardP.setBorder(new LineBorder(java.awt.Color.GRAY, 3, true));
				cardsPanel.add(cardP);
			}
		} else {
			for(int i = 0; i < 4; i++) {
				JLabel cardP = new JLabel(new ImageIcon(Utils.getCardBackImage()));
				cardP.setBorder(new LineBorder(java.awt.Color.GRAY, 3, true));
				cardsPanel.add(cardP);
			}
		}
		cardsPanel.revalidate();
	}

	private void border(int cardId, java.awt.Color color) {
		((JLabel) cardsPanel.getComponent(cardId)).setBorder(new LineBorder(color, 3, true));
		cardsPanel.revalidate();
	}

	private void setBorder(boolean myTurn) {
		Border currentBorder = this.getBorder();
		if((currentBorder == null || currentBorder instanceof EmptyBorder) && myTurn) {
			this.setBorder(new LineBorder(java.awt.Color.DARK_GRAY, 3, true));
		} else if((currentBorder == null || currentBorder instanceof LineBorder) && !myTurn) {
			this.setBorder(new EmptyBorder(3, 3, 3, 3));
		}
	}

	@Override
	public void onSituationChange(Situation s) {
		if(timer != null) {
			timer.cancel();
			timer = null;
			setCards(situation);
		}
		if(s == null) {
			setCards(null);
		} else {
			this.setBorder(s.getPlayingPlayer() == player);
			if(situation != null && s.getVariant() == situation.getVariant()) {
				if(s.getTurn() == situation.getTurn() + 1) {
					if(situation.getPlayingPlayer() == player) {
						situation = s;
						// C'était le tour de ce joueur
						Play p = s.getVariant().getPlay(situation.getTurn());
						if(p instanceof CardPlay) {
							// On fait l'animation
							CardPlay cp = (CardPlay) p;
							border(cp.getPlacement(),
									(cp instanceof PlacePlay ? java.awt.Color.GREEN : java.awt.Color.RED));

							this.timer = new Timer();
							timer.schedule(new java.util.TimerTask() {
								@Override
								public void run() {
									setCards(s);
									timer = null;
								}
							}, 2500);
						} else {
							setCards(s);
						}
						return;
					} else {
						setCards(s); // Necessary because we also draw clues information
					}
				} else {
					setCards(s);
				}
			} else {
				setCards(s);
			}
		}
		situation = s;

	}

}
