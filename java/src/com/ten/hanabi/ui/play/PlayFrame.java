package com.ten.hanabi.ui.play;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;

public class PlayFrame {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PlayFrame window = new PlayFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PlayFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setEnabled(false);
		frame.setBounds(100, 100, 1243, 762);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.7);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		PlayersCardsPanel playersCardsPanel = new PlayersCardsPanel();
		splitPane.setRightComponent(playersCardsPanel);
		
		JSplitPane board_options = new JSplitPane();
		board_options.setResizeWeight(0.6);
		board_options.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(board_options);
		
		JSplitPane clues_board = new JSplitPane();
		clues_board.setResizeWeight(0.1);
		clues_board.setOrientation(JSplitPane.VERTICAL_SPLIT);
		board_options.setLeftComponent(clues_board);
		
		JSplitPane board_discard = new JSplitPane();
		board_discard.setResizeWeight(0.3);
		board_discard.setOrientation(JSplitPane.VERTICAL_SPLIT);
		clues_board.setRightComponent(board_discard);
		
		JSplitPane log_options = new JSplitPane();
		log_options.setResizeWeight(0.5);
		board_options.setRightComponent(log_options);
	}
}
