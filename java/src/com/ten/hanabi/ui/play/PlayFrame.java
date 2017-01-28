package com.ten.hanabi.ui.play;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

public class PlayFrame extends JFrame {

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

		getContentPane().setEnabled(false);
		setBounds(100, 100, 1243, 762);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.8);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		PlayersCardsPanel playersCardsPanel = new PlayersCardsPanel(upm);
		playersCardsPanel.setMinimumSize(new Dimension(300, 400));
		splitPane.setRightComponent(playersCardsPanel);

		JSplitPane board_options = new JSplitPane();
		board_options.setResizeWeight(0.6);
		board_options.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(board_options);

		JSplitPane clues_board = new JSplitPane();
		clues_board.setResizeWeight(0.1);
		clues_board.setOrientation(JSplitPane.VERTICAL_SPLIT);
		clues_board.setMinimumSize(new Dimension(800, 400));
		board_options.setLeftComponent(clues_board);

		JSplitPane board_discard = new JSplitPane();
		board_discard.setResizeWeight(0.3);
		board_discard.setOrientation(JSplitPane.VERTICAL_SPLIT);
		clues_board.setRightComponent(board_discard);

		JPanel boardPanel = new BoardPanel(upm);
		boardPanel.setBorder(new EmptyBorder(20, 10, 10, 20));
		board_discard.setLeftComponent(boardPanel);

		JPanel discardPanel = new DiscardPanel(upm);
		board_discard.setRightComponent(discardPanel);

		JSplitPane log_options = new JSplitPane();
		log_options.setResizeWeight(0.5);
		board_options.setRightComponent(log_options);
	}

}
