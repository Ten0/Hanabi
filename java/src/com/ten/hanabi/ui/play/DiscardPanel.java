package com.ten.hanabi.ui.play;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.ten.hanabi.core.Card;
import com.ten.hanabi.core.Color;
import com.ten.hanabi.core.Hanabi;
import com.ten.hanabi.core.Situation;
import com.ten.hanabi.ui.Utils;

public class DiscardPanel extends JPanel implements HanabiChangeListener, SituationChangeListener {

	UIPlayManager uiPlayManager;
	HashMap<Color, JPanel> colorPanels = new HashMap<Color, JPanel>();
	private JPanel cluesStrikesPanel;
	
	public DiscardPanel(UIPlayManager upm) {
		uiPlayManager = upm;
		
		for(Color c : Color.values()) {
			JPanel cColorPanel = new JPanel();
			cColorPanel.setLayout(new FlowLayout());

			colorPanels.put(c, cColorPanel);
		}
		
		cluesStrikesPanel = new JPanel();
		cluesStrikesPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		upm.registerHanabiChangeListener(this);
	}
	
	@Override
	public void onHanabiChange(Hanabi hanabi) {
		removeAll();
		for(Color c : ((hanabi == null) ? Arrays.asList(Color.values()) : hanabi.getRuleSet().getEnabledColors())) {
			add(colorPanels.get(c));
		}
		add(cluesStrikesPanel);
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
		revalidate();
	}

}
