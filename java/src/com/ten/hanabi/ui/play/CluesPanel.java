package com.ten.hanabi.ui.play;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.ten.hanabi.core.Color;
import com.ten.hanabi.core.Hanabi;
import com.ten.hanabi.core.RuleSet;
import com.ten.hanabi.core.clues.*;
import com.ten.hanabi.play.HanabiChangeListener;
import com.ten.hanabi.ui.Utils;

public class CluesPanel extends JPanel implements HanabiChangeListener, SelectedClueChangeListener {

	private final UIPlayManager uiPlayManager;

	private Clue clueSelectedHere = null;

	private TreeMap<Clue, JLabel> clueLabel = new TreeMap<>();

	public CluesPanel(UIPlayManager upm) {
		super(new GridLayout(1, 2, 10, 0));
		uiPlayManager = upm;
		upm.registerHanabiChangeListener(this);
		upm.registerSelectedClueChangeListener(this);
	}

	@Override
	public void onHanabiChange(Hanabi hanabi) {
		this.removeAll();
		JPanel colorPanel = new JPanel(new FlowLayout());
		for(Color c : (hanabi == null ? new RuleSet(true, false, false, false) : hanabi.getRuleSet())
				.getEnabledColors()) {
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
		addToken(panel, Utils.getTokenImage(c), new ColorClue(c));
	}

	private void addToken(JPanel panel, int n) {
		addToken(panel, Utils.getTokenImage(n), new NumberClue(n));
	}

	private void addToken(JPanel panel, Image image, Clue c) {
		JLabel cardP = new JLabel(new ImageIcon(Utils.getScaledImage(image, 75, 75, 0, 0, -1, -1)));
		clueLabel.put(c, cardP);
		clueBorder(cardP, c);

		JPanel jpanel = new JPanel(); // Required for inside borders
		jpanel.add(cardP);

		final Clue clue = c;
		jpanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(uiPlayManager.getSelectedClue() == clue)
					uiPlayManager.selectClue(null);
				else
					uiPlayManager.selectClue(clue);
			}
		});

		this.add(jpanel);
	}

	private void clueBorder(Clue c) {
		if(c == null)
			return;
		clueBorder(clueLabel.get(c), c);
		this.revalidate();
	}

	private void clueBorder(JLabel cardP, Clue clue) {
		java.awt.Color color = java.awt.Color.GRAY;
		if(clueSelectedHere == clue)
			color = java.awt.Color.ORANGE;
		cardP.setBorder(new LineBorder(color, 3, true));
	}

	@Override
	public void onSelectedClueChange(Clue c) {
		if(clueSelectedHere == c)
			return;
		if(clueSelectedHere != null) {
			Clue previousSelectedClue = clueSelectedHere;
			clueSelectedHere = null;
			clueBorder(previousSelectedClue);
		}
		this.clueSelectedHere = c;
		clueBorder(c);
	}

}
