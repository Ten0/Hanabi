package com.ten.hanabi.ui.play;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.ten.hanabi.core.*;
import com.ten.hanabi.play.HanabiChangeListener;
import com.ten.hanabi.play.SituationChangeListener;
import com.ten.hanabi.ui.Utils;
import com.ten.hanabi.ui.WrapLayout;

public class DiscardPanel extends JPanel implements HanabiChangeListener, SituationChangeListener {

	UIPlayManager uiPlayManager;
	HashMap<Color, JPanel> colorPanels = new HashMap<Color, JPanel>();
	private JPanel cluesStrikesPanel;
	private JPanel panel;

	public DiscardPanel(UIPlayManager upm) {
		uiPlayManager = upm;
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);

		panel = new JPanel();
		panel.setLayout(new GridLayout(1, Color.values().length + 1, 5, 0));
		scrollPane.setViewportView(panel);

		for(Color c : Color.values()) {
			JPanel cColorPanel = new JPanel();
			cColorPanel.setLayout(new WrapLayout());

			colorPanels.put(c, cColorPanel);
		}

		cluesStrikesPanel = new JPanel();
		cluesStrikesPanel.setLayout(new GridLayout(2, 1, 0, 0));

		upm.registerHanabiChangeListener(this);
	}

	@Override
	public void onHanabiChange(Hanabi hanabi) {
		panel.removeAll();
		for(Color c : ((hanabi == null) ? Arrays.asList(Color.values()) : hanabi.getRuleSet().getEnabledColors())) {
			panel.add(colorPanels.get(c));
		}
		panel.add(cluesStrikesPanel);

		uiPlayManager.registerSituationChangeListener(this); // triggers onSituationChange thus revalidate
	}

	@Override
	public void onSituationChange(Situation s) {
		for(Color c : ((s == null) ? Arrays.asList(Color.values()) : s.getHanabi().getRuleSet().getEnabledColors())) {
			JPanel cColorPanel = colorPanels.get(c);
			cColorPanel.removeAll();
			colorPanels.put(c, cColorPanel);
		}
		if(s != null) {
			for(Card c : s.getDiscardedCards()) {
				JLabel cColorLabel = new JLabel(new ImageIcon(Utils.getCardSmallImage(c)));
				colorPanels.get(c.getColor()).add(cColorLabel);
			}
		}
		panel.revalidate();
		repaint();
	}

}
