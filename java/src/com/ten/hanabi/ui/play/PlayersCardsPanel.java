package com.ten.hanabi.ui.play;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.Dimension;

public class PlayersCardsPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public PlayersCardsPanel() {
		setPreferredSize(new Dimension(400, 750));
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		PlayerPanel playerPanel = new PlayerPanel();
		scrollPane.setViewportView(playerPanel);

	}

}
