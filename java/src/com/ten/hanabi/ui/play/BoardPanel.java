package com.ten.hanabi.ui.play;

import javax.swing.JPanel;

import com.ten.hanabi.core.*;
import com.ten.hanabi.play.HanabiChangeListener;
import com.ten.hanabi.play.SituationChangeListener;
import com.ten.hanabi.ui.Utils;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class BoardPanel extends JPanel implements HanabiChangeListener, SituationChangeListener {

	HashMap<Color, JPanel> colorPanels = new HashMap<Color, JPanel>();
	private JLabel nbClues;
	private JLabel nbStrikes;
	private JPanel cluesStrikesPanel;
	private JLabel deckLabel;
	private JPanel deckPanel;

	/**
	 * Create the panel.
	 */
	public BoardPanel(UIPlayManager upm) {
		setLayout(new GridLayout(1, Color.values().length + 2, 5, 0));

		for(Color c : Color.values()) {
			JPanel cColorPanel = new JPanel();
			// cColorPanel.setLayout(new BoxLayout(cColorPanel, BoxLayout.X_AXIS));
			cColorPanel.setLayout(new FlowLayout());

			colorPanels.put(c, cColorPanel);
		}

		cluesStrikesPanel = new JPanel();
		cluesStrikesPanel.setLayout(new GridLayout(2, 1, 0, 0));

		Font cluesStrikesFont = new Font("Lato Black", Font.BOLD, (int) (Utils.RESCALE * 40));
		nbClues = new JLabel();
		nbClues.setFont(cluesStrikesFont);
		nbClues.setIcon(new ImageIcon(Utils.rescaleBasic(Utils.getImageForFile("/com/ten/hanabi/ui/img/clue.png"))));
		cluesStrikesPanel.add(nbClues);

		nbStrikes = new JLabel();
		nbStrikes.setFont(cluesStrikesFont);
		nbStrikes.setIcon(new ImageIcon(Utils.rescaleBasic(Utils.getImageForFile("/com/ten/hanabi/ui/img/miss.png"))));
		cluesStrikesPanel.add(nbStrikes);

		deckPanel = new JPanel();

		deckLabel = new JLabel(new ImageIcon(Utils.getCardBackImage()));
		Font deckSizeFont = new Font("Lato Black", Font.BOLD, (int) (Utils.RESCALE * 50));
		deckLabel.setFont(deckSizeFont);
		deckLabel.setForeground(new java.awt.Color(0xea, 0xef, 0xe6));
		deckLabel.setHorizontalTextPosition(JLabel.CENTER); // Text on image
		deckLabel.setBorder(new LineBorder(java.awt.Color.BLACK, 3, true));
		deckPanel.add(deckLabel);

		upm.registerHanabiChangeListener(this);
		upm.registerSituationChangeListener(this);
	}

	@Override
	public void onHanabiChange(Hanabi hanabi) {
		removeAll();
		for(Color c : ((hanabi == null) ? new RuleSet(true, false, false, false) : hanabi.getRuleSet())
				.getEnabledColors()) {
			add(colorPanels.get(c));
		}
		add(cluesStrikesPanel);
		add(deckPanel);
	}

	@Override
	public void onSituationChange(Situation s) {
		for(Color c : ((s == null) ? new RuleSet(true, false, false, false) : s.getHanabi().getRuleSet())
				.getEnabledColors()) {
			JPanel cColorPanel = colorPanels.get(c);
			cColorPanel.removeAll();

			JLabel cColorLabel = new JLabel(new ImageIcon(Utils.getCardImage(
					s == null || s.getNumberAtColor(c) == 0 ? null : new Card(c, s.getNumberAtColor(c)))));
			cColorPanel.add(cColorLabel);
			cColorLabel.setBorder(new LineBorder(c.getAwtColor(), 3, true));
			colorPanels.put(c, cColorPanel);
		}
		if(s == null) {
			nbClues.setText("");
			nbStrikes.setText("");
			deckLabel.setText("X");
		} else {
			nbClues.setText(Integer.toString(s.getClues()));
			nbStrikes.setText(Integer.toString(s.getStrikes()));
			deckLabel.setText(Integer.toString(s.getNbCardsLeftInDeck()));
		}
		revalidate();
	}

}
