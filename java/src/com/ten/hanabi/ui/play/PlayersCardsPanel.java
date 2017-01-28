package com.ten.hanabi.ui.play;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.FlowLayout;

import com.ten.hanabi.core.Hanabi;
import com.ten.hanabi.core.Player;
import com.ten.hanabi.core.Situation;
import javax.swing.BoxLayout;
import javax.swing.ScrollPaneConstants;

public class PlayersCardsPanel extends JPanel implements HanabiChangeListener {

	UIPlayManager uiPlayManager;
	private JPanel panel;

	/**
	 * Create the panel.
	 * 
	 * @param uiPlayManager
	 */
	public PlayersCardsPanel(UIPlayManager upm) {
		uiPlayManager = upm;
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		upm.registerHanabiChangeListener(this);
	}

	@Override
	public void onHanabiChange(Hanabi hanabi) {
		panel.removeAll();
		if(hanabi != null) {
			for(Player p : hanabi.getPlayers()) {
				PlayerPanel playerPanel = new PlayerPanel(uiPlayManager, p);
				panel.add(playerPanel);
			}
		} else {
			for(int i = 0; i < 4; i++) {
				PlayerPanel playerPanel = new PlayerPanel(uiPlayManager, null);
				panel.add(playerPanel);
			}
		}
	}

}
