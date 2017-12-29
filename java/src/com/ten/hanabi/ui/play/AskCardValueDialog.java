package com.ten.hanabi.ui.play;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.exceptions.InvalidDeckException;
import com.ten.hanabi.ui.ExceptionDialog;
import com.ten.hanabi.ui.Utils;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class AskCardValueDialog extends JDialog {
	public AskCardValueDialog(final Window parent, final Deck deck, final int idInDeck, final boolean allowClose) {
		super(parent, "Select card value", JDialog.ModalityType.DOCUMENT_MODAL);

		setResizable(false);
		// Block closing
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent ev) {
				if(allowClose) { // Set card to null
					try {
						deck.setCard(idInDeck, null);
						AskCardValueDialog.this.setVisible(false);
						AskCardValueDialog.this.dispose();
					} catch (InvalidDeckException e) {
						throw new RuntimeException(e); // Shouldn't happen : null is always valid
					}
				} // Otherwise, just prevent closing by not doing anything
			}
		});

		getContentPane().setLayout(new BorderLayout(0, 0));

		RuleSet rs = deck.getRuleSet();
		ArrayList<Color> enabledColors = rs.getEnabledColors();
		JPanel cardsPanel = new JPanel(new GridLayout(5, enabledColors.size(), 0, 0));

		for(Color c : enabledColors) {
			for(int n = 1; n <= 5; n++) {
				final Card card = new Card(c, n);
				JLabel cardL = new JLabel(new ImageIcon(Utils.getCardSmallImage(card)));
				cardL.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						try {
							deck.setCard(idInDeck, card);
							AskCardValueDialog.this.setVisible(false);
							AskCardValueDialog.this.dispose();
						} catch (InvalidDeckException ex) {
							new ExceptionDialog(AskCardValueDialog.this, "Invalid card", ex.getMessage(), ex)
									.setVisible(true);
						}
					}
				});
				cardsPanel.add(cardL);
			}
		}

		getContentPane().add(cardsPanel, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(parent);
	}

	public static void setIfRequired(PlayFrame playFrame, Deck deck, int idInDeck) {
		if(deck.getCard(idInDeck) != null)
			return;
		AskCardValueDialog dialog = new AskCardValueDialog(playFrame, deck, idInDeck, false);
		dialog.setVisible(true);
	}

}
