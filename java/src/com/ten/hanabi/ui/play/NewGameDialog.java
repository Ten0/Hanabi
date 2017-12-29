package com.ten.hanabi.ui.play;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ten.hanabi.core.*;
import com.ten.hanabi.play.PlayManager;
import com.ten.hanabi.ui.ExceptionDialog;

import java.awt.FlowLayout;

public class NewGameDialog extends JDialog implements ActionListener, KeyListener {

	private UIPlayManager uiPlayManager;
	private JButton createButton;
	private JButton cancelButton;
	private JTextField playersTextField;
	private JCheckBox multiCheckBox;
	private JCheckBox multiNormalColorCheckBox;
	private JCheckBox cardNumberVariantCheckBox;
	private JCheckBox emptyCluesAllowedCheckBox;

	public NewGameDialog(Window parent, UIPlayManager upm) {
		super(parent);
		this.uiPlayManager = upm;
		setTitle("Create new IRL game");
		setPreferredSize(new Dimension(400, 200));

		JPanel paramsPanel = new JPanel();
		paramsPanel.setLayout(new BoxLayout(paramsPanel, BoxLayout.Y_AXIS));

		JLabel playersInfoLabel = new JLabel("Players (separated by ,) :");
		paramsPanel.add(playersInfoLabel);

		playersTextField = new JTextField();
		playersTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, playersTextField.getPreferredSize().height));
		playersTextField.setPreferredSize(playersTextField.getMaximumSize());
		playersTextField.addActionListener(this);
		paramsPanel.add(playersTextField);

		multiCheckBox = new JCheckBox("Multicolor");
		paramsPanel.add(multiCheckBox);

		multiNormalColorCheckBox = new JCheckBox("Multicolor is a 5th color");
		paramsPanel.add(multiNormalColorCheckBox);

		cardNumberVariantCheckBox = new JCheckBox("Card number variant");
		paramsPanel.add(cardNumberVariantCheckBox);

		emptyCluesAllowedCheckBox = new JCheckBox("Empty clues allowed");
		paramsPanel.add(emptyCluesAllowedCheckBox);

		getContentPane().add(paramsPanel, BorderLayout.CENTER);

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		createButton = new JButton("Create");
		createButton.addActionListener(this);
		buttonsPanel.add(createButton);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		buttonsPanel.add(cancelButton);

		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(parent);
	}

	private void createGame() {
		try {
			String[] playerNames = playersTextField.getText().split(",");
			Player[] players = new Player[playerNames.length];
			RealPlayer[] realPlayers = new RealPlayer[players.length];
			for(int i = 0; i < playerNames.length; i++) {
				playerNames[i] = playerNames[i].trim();
				players[i] = new Player(playerNames[i]);
				realPlayers[i] = new RealPlayer(uiPlayManager);
			}

			RuleSet ruleSet = new RuleSet(multiCheckBox.isSelected(), multiNormalColorCheckBox.isSelected(),
					cardNumberVariantCheckBox.isSelected(), emptyCluesAllowedCheckBox.isSelected());
			Deck deck = new Deck(ruleSet, false);
			Hanabi h = new Hanabi(ruleSet, deck, players);
			PlayManager pm = new PlayManager(h, realPlayers);
			pm.notifyPlay();
			uiPlayManager.setPlayManager(pm);
			this.dispose();
		} catch (Exception ex) {
			new ExceptionDialog(this, "Could not setup game", ex.getMessage(), ex).setVisible(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createButton || e.getSource() == playersTextField) {
			createGame();
		} else { // Cancel button
			this.dispose();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			createGame();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
