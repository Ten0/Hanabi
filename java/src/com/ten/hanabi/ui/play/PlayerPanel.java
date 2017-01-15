package com.ten.hanabi.ui.play;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.border.EmptyBorder;

import com.ten.hanabi.ui.Utils;

import java.awt.Font;
import javax.swing.ImageIcon;

public class PlayerPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public PlayerPanel() {
		setPreferredSize(new Dimension(500, 300));
		setLayout(new BorderLayout(0, 0));
		
		JPanel namePanel = new JPanel();
		namePanel.setBorder(new EmptyBorder(5, 5, 3, 5));
		add(namePanel, BorderLayout.NORTH);
		namePanel.setLayout(new BorderLayout(0, 0));
		
		JLabel nameLabel = new JLabel("PlayerName");
		nameLabel.setFont(new Font("Serif", Font.BOLD, 16));
		namePanel.add(nameLabel, BorderLayout.WEST);
		
		JPanel cardsPanel = new JPanel();
		cardsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(cardsPanel, BorderLayout.CENTER);
		cardsPanel.setLayout(new GridLayout(1, 5, 10, 0));
		
		ImageIcon ii = new ImageIcon(PlayerPanel.class.getResource("/com/ten/hanabi/img/cards.png"));
		ii = new ImageIcon(Utils.getScaledImage(ii.getImage(), 81, 125, 0, 0, 65, 100));
		JLabel card0 = new JLabel(ii);
		cardsPanel.add(card0);

	}

}
