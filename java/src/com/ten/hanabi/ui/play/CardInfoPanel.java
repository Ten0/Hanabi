package com.ten.hanabi.ui.play;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

import com.ten.hanabi.core.*;
import com.ten.hanabi.play.SituationChangeListener;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

public class CardInfoPanel extends JPanel implements SituationChangeListener, SelectedCardChangeListener {
	private JEditorPane infoTextZone;
	private UIPlayManager uiPlayManager;

	/**
	 * Create the panel.
	 */
	public CardInfoPanel(UIPlayManager upm) {
		this.uiPlayManager = upm;
		setLayout(new BorderLayout(0, 0));

		infoTextZone = new JEditorPane();
		infoTextZone.setFocusable(false);
		infoTextZone.setContentType("text/html");

		JScrollPane scrollPane = new JScrollPane(infoTextZone);
		add(scrollPane);

		upm.registerSituationChangeListener(this);
		upm.registerSelectedCardChangeListener(this);
	}

	@Override
	public void onSelectedCardChange(Player player, int pos) {
		update();
	}

	@Override
	public void onSituationChange(Situation s) {
		update();
	}

	private void update() {
		Situation situation = uiPlayManager.getSituation();
		Player player = uiPlayManager.getSelectedCardPlayer();
		int cardIdInHand = uiPlayManager.getSelectedCardIdInHand();
		if(situation == null) {
			infoTextZone.setText("");
		} else if(player == null || cardIdInHand < 0) {
			infoTextZone.setText("<html><i>No card selected</i></html>");
		} else {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			ps.println("<html>");
			Hand h = situation.getHand(player);
			Card c = h.get(cardIdInHand);
			CardKnowlege ck = h.getKnowlege(cardIdInHand);

			// Card basic info
			ps.println("<strong>Age:</strong> " + ck.getAge() + " (Entered turn " + ck.getTurnEntered()
					+ ")</strong><br/>");

			// Print possible colors
			ps.print("<strong>Possible colors:</strong>");
			for(Color color : ck.getPossibleColors()) {
				ps.print(" " + color.smallName());
			}
			ps.println("<br/>");

			// Print possible number values
			ps.print("<strong>Possible numbers:</strong>");
			for(int i : ck.getPossibleNumbers()) {
				ps.print(" " + i);
			}
			ps.println("<br/><br/>");

			// Print all possible card values
			ps.print("<strong>Possible cards:</strong>");
			for(Card pc : ck.getPossibleCardsWithoutDupplicates()) {
				ps.print(" " + pc);
			}
			ps.println("<br/><br/>");

			ps.println("</html>");
			String content = new String(baos.toByteArray());
			infoTextZone.setText(content);
			infoTextZone.setCaretPosition(infoTextZone.getDocument().getLength()); // Scroll to the bottom
		}
	}

}
