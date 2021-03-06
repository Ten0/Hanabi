package com.ten.hanabi.ui.play;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import com.ten.hanabi.core.Card;
import com.ten.hanabi.core.Deck;
import com.ten.hanabi.core.Hanabi;
import com.ten.hanabi.core.Situation;
import com.ten.hanabi.core.clues.Clue;
import com.ten.hanabi.core.exceptions.InvalidDeckException;
import com.ten.hanabi.core.exceptions.InvalidPlayException;
import com.ten.hanabi.play.PlayManager;
import com.ten.hanabi.serialization.XMLSerializer;
import com.ten.hanabi.ui.ExceptionDialog;

import javax.swing.JTabbedPane;

public class PlayFrame extends JFrame implements KeyListener {

	UIPlayManager uiPlayManager;

	/**
	 * Create the frame.
	 *
	 * @param uiPlayManager
	 */
	public PlayFrame(UIPlayManager upm) {
		super();
		setTitle("Hanabi - Game");
		uiPlayManager = upm;
		upm.setPlayFrame(this);

		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		getContentPane().setEnabled(false);
		setSize(1243, 762);
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.7);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		PlayersCardsPanel playersCardsPanel = new PlayersCardsPanel(upm);
		playersCardsPanel.setMinimumSize(new Dimension(300, 400));
		splitPane.setRightComponent(playersCardsPanel);

		JSplitPane board_options = new JSplitPane();
		board_options.setResizeWeight(0.4);
		board_options.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(board_options);

		JSplitPane clues_board = new JSplitPane();
		clues_board.setResizeWeight(0.1);
		clues_board.setOrientation(JSplitPane.VERTICAL_SPLIT);
		clues_board.setMinimumSize(new Dimension(800, 400));
		board_options.setLeftComponent(clues_board);

		CluesPanel cluesPanel = new CluesPanel(upm);
		clues_board.setLeftComponent(cluesPanel);

		JPanel board_discard = new JPanel(new BorderLayout());
		clues_board.setRightComponent(board_discard);

		JPanel boardPanel = new BoardPanel(upm);
		boardPanel.setBorder(new EmptyBorder(20, 10, 10, 20));
		board_discard.add(boardPanel, BorderLayout.NORTH);

		JPanel discardPanel = new DiscardPanel(upm);
		board_discard.add(discardPanel, BorderLayout.CENTER);

		JSplitPane log_options = new JSplitPane();
		log_options.setResizeWeight(0.5);
		board_options.setRightComponent(log_options);

		LogPanel logPanel = new LogPanel(upm);
		log_options.setLeftComponent(logPanel);

		JTabbedPane infos_options = new JTabbedPane(JTabbedPane.TOP);
		infos_options.setFocusable(false);
		log_options.setRightComponent(infos_options);

		CardInfoPanel cardInfoPanel = new CardInfoPanel(upm);
		infos_options.addTab("Card info", null, cardInfoPanel, null);

		OptionsPanel optionsPanel = new OptionsPanel(upm);
		infos_options.addTab("Options", null, optionsPanel, null);

		pack();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		try {
			if(e.getKeyCode() == KeyEvent.VK_HOME) {
				uiPlayManager.goToTurn(0);
			} else if(e.getKeyCode() == KeyEvent.VK_END) {
				uiPlayManager.goToEnd();
			} else if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_DOWN) {
				uiPlayManager.nextTurn();
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_UP) {
				uiPlayManager.previousTurn();
			} else if(e.getKeyChar() == 'n') {
				new NewGameDialog(this, uiPlayManager).setVisible(true);
			} else if(e.getKeyChar() == 'o' || e.getKeyChar() == 'O') {
				OpenFrame openFrame = new OpenFrame(this, uiPlayManager);
				if(e.getKeyChar() == 'O') // BGA
					openFrame.setTab(1);
				openFrame.setVisible(true);
			} else if(e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
				boolean doSave = true;
				if(e.getKeyChar() == 'S') {
					try {
						Deck deck = uiPlayManager.getHanabi().getDeck();
						if(uiPlayManager.getSituation().isGameOver())
							deck.autoComplete(true);
						else
							deck.lock();
					} catch (InvalidDeckException ex) {
						new ExceptionDialog(this, "Could not lock deck", ex.getMessage(), ex).setVisible(true);
						doSave = false;
					}
				}
				if(doSave) {
					JFileChooser chooser = new JFileChooser();
					int retrival = chooser.showSaveDialog(null);
					if(retrival == JFileChooser.APPROVE_OPTION) {
						try {
							XMLSerializer.toXML(uiPlayManager.getHanabi(), chooser.getSelectedFile().getAbsolutePath());
						} catch (Exception ex) {
							new ExceptionDialog(this, "Error while saving game",
									"Make sure the location you are writing to is valid", ex).setVisible(true);
						}
					}
				}
			} else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				PlayManager playManager = uiPlayManager.getPlayManager();
				if(playManager != null) {
					Hanabi h = uiPlayManager.getHanabi();
					if(h != null && h.getVariant().getTurn() > 0)
						playManager.rollback();
				}
			} else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				if(!uiPlayManager.getHanabi().getDeck().isLocked()) {
					int selectedCard = uiPlayManager.getSelectedCardIdInHand();
					if(selectedCard >= 0) {
						int cardIdInDeck = uiPlayManager.getSituation().getHand(uiPlayManager.getSelectedCardPlayer())
								.getIdInDeck(selectedCard);
						final Deck deck = uiPlayManager.getHanabi().getDeck();
						final Card oldCardValue = deck.getCard(cardIdInDeck);
						new AskCardValueDialog(this, deck, cardIdInDeck, true).setVisible(true); // Will handle the card
																									// value change
						Situation currS = uiPlayManager.getSituation();
						try {
							currS.getVariant().getSituation();
							uiPlayManager.onSituationChange(currS.getVariant().getSituation(currS.getTurn()));
						} catch (InvalidPlayException ex) {
							try {
								deck.setCard(cardIdInDeck, oldCardValue); // rollback
							} catch (InvalidDeckException e1) {
								throw new RuntimeException(e1); // Should never happen, old deck was valid
							}
							new ExceptionDialog(this, "Error while changing card value", ex.getMessage(), ex)
									.setVisible(true);
						}

					}
				}
			} else {
				try {
					Clue c = Clue.fromSmall(Character.toString(e.getKeyChar()));
					uiPlayManager.selectClue(c.compareTo(uiPlayManager.getSelectedClue()) == 0 ? null : c);
				} catch (Exception ex) { // Character doesn't match any clue
					// It's ok, just drop the key press
				}
			}
		} catch (InvalidPlayException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

}
