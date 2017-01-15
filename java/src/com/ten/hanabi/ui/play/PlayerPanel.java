package com.ten.hanabi.ui.play;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.border.EmptyBorder;

import com.ten.hanabi.core.*;
import com.ten.hanabi.ui.Utils;

import java.awt.Font;

public class PlayerPanel extends JPanel implements SituationChangeListener {
	private Player player;
	
	private JPanel cardsPanel;
	private JLabel nameLabel;

	/**
	 * Create the panel.
	 */
	public PlayerPanel(UIPlayManager upm, Player p) {
		setLayout(new BorderLayout(0, 0));
		
		JPanel namePanel = new JPanel();
		namePanel.setBorder(new EmptyBorder(5, 5, 3, 5));
		add(namePanel, BorderLayout.NORTH);
		namePanel.setLayout(new BorderLayout(0, 0));
		
		nameLabel = new JLabel(p == null ? "<Player name>" : p.toString());
		nameLabel.setFont(new Font("Serif", Font.BOLD, 16));
		namePanel.add(nameLabel, BorderLayout.WEST);
		
		cardsPanel = new JPanel();
		cardsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(cardsPanel, BorderLayout.CENTER);
		cardsPanel.setLayout(new GridLayout(1, 5, 10, 0));
		
		player = p;
		
		upm.registerSituationChangeListener(this);
	}

	@Override
	public void onSituationChange(Situation s) {
		cardsPanel.removeAll();
		if(s != null) {
			for(Card c : s.getHand(player)) {
				cardsPanel.add(new JLabel(new ImageIcon(Utils.getCardImage(c))));
			}
		}
		else {
			for(int i = 0; i < 4; i++) {
				cardsPanel.add(new JLabel(new ImageIcon(Utils.getCardBackImage())));
			}
		}
		cardsPanel.revalidate();
	}
	
	

}
