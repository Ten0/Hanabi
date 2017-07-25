package com.ten.hanabi.ui.play;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ten.hanabi.core.Color;
import com.ten.hanabi.core.Hanabi;
import com.ten.hanabi.play.HanabiChangeListener;
import com.ten.hanabi.ui.Utils;

public class CluesPanel extends JPanel implements HanabiChangeListener {

	private UIPlayManager uiPlayManager;

	public CluesPanel(UIPlayManager upm) {
		super(new GridLayout(1, 2, 10, 0));
		uiPlayManager = upm;
		upm.registerHanabiChangeListener(this);
	}

	@Override
	public void onHanabiChange(Hanabi hanabi) {
		this.removeAll();
		JPanel colorPanel = new JPanel(new FlowLayout());
		if(hanabi == null) {
			for(Color c : Color.values())
				addToken(colorPanel, c);
		} else {
			for(Color c : hanabi.getRuleSet().getEnabledColors())
				addToken(colorPanel, c);
		}
		JPanel numberPanel = new JPanel(new FlowLayout());
		for(int n = 1; n <= 5; n++) {
			addToken(numberPanel, n);
		}
		this.add(colorPanel);
		this.add(numberPanel);
		this.revalidate();
		this.repaint();
	}

	private void addToken(JPanel panel, Color c) {
		addToken(panel, Utils.getTokenImage(c));
	}

	private void addToken(JPanel panel, int n) {
		addToken(panel, Utils.getTokenImage(n));
	}

	private void addToken(JPanel panel, Image image) {
		JLabel cardP = new JLabel(new ImageIcon(Utils.getScaledImage(image, 75, 75, 0, 0, -1, -1)));
		this.add(cardP);
	}

}
