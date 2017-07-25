package com.ten.hanabi.ui.play;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import com.ten.hanabi.core.exceptions.InvalidPlayException;
import javax.swing.JTabbedPane;

public class PlayFrame extends JFrame implements KeyListener {

	UIPlayManager uiPlayManager;
	private JSplitPane splitPane;

	/**
	 * Create the frame.
	 *
	 * @param uiPlayManager
	 */
	public PlayFrame(UIPlayManager upm) {
		super();
		setTitle("Hanabi - Game");
		uiPlayManager = upm;

		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		getContentPane().setEnabled(false);
		setSize(1243, 762);
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));

		splitPane = new JSplitPane();
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
		log_options.setRightComponent(infos_options);

		CardInfoPanel cardInfoPanel = new CardInfoPanel(upm);
		infos_options.addTab("Card info", null, cardInfoPanel, null);

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
			} else if(e.getKeyChar() == 'o' || e.getKeyChar() == 'O') {
				OpenFrame openFrame = new OpenFrame(this, uiPlayManager);
				if(e.getKeyChar() == 'O') // BGA
					openFrame.setTab(1);
				openFrame.setVisible(true);
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
