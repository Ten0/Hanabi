package com.ten.hanabi.ui.play;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import com.flecheck.hanabi.bga.BGA;
import com.ten.hanabi.core.Hanabi;
import com.ten.hanabi.serialization.XMLSerializer;
import com.ten.hanabi.ui.ExceptionDialog;

import javax.swing.JButton;

public class OpenFrame extends JDialog implements ChangeListener, ActionListener, KeyListener {

	private UIPlayManager uiPlayManager;
	private JTabbedPane tabbedPane;
	private JFormattedTextField gameIdField;
	private JButton openButton;
	private JButton cancelButton;
	private JFileChooser fileChooser;

	public OpenFrame(Window parent, UIPlayManager upm) {
		super(parent);
		this.uiPlayManager = upm;
		setTitle("Open game");

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(this);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		fileChooser = new JFileChooser();
		fileChooser.addActionListener(this);
		fileChooser.setControlButtonsAreShown(false);
		tabbedPane.add("From file", fileChooser);

		JPanel bgaChooser = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tabbedPane.addTab("BGA", null, bgaChooser, null);

		JLabel gameIdLabel = new JLabel("Game ID: ");
		bgaChooser.add(gameIdLabel);

		NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(false);
		// If you want the value to be committed on each keystroke instead of focus lost
		formatter.setCommitsOnValidEdit(true);
		gameIdField = new JFormattedTextField(formatter);
		gameIdField.addKeyListener(this);
		bgaChooser.add(gameIdField);
		gameIdField.setColumns(10);

		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(controlPanel, BorderLayout.SOUTH);

		openButton = new JButton("Open");
		openButton.addActionListener(this);
		controlPanel.add(openButton);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		controlPanel.add(cancelButton);

		pack();
		setLocationRelativeTo(parent);
	}

	public void setTab(int tab) {
		tabbedPane.setSelectedIndex(tab);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(tabbedPane.getSelectedIndex() == 1)
			gameIdField.requestFocusInWindow();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == openButton || e.getSource() == fileChooser || e.getSource() == gameIdField) {
			openGame();
		} else {
			this.dispose();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			openGame();
		}
	}

	private void openGame() {
		try {
			Hanabi h = null;
			if(tabbedPane.getSelectedIndex() == 0) {
				if(fileChooser.getSelectedFile() != null)
					h = XMLSerializer.loadHanabi(fileChooser.getSelectedFile());
			} else { // BGA
				h = BGA.getGameById(Integer.parseInt(gameIdField.getText().replaceAll("[^0-9]", "")));
			}
			if(h != null)
				uiPlayManager.loadHanabi(h, 0); // 1er tour
		} catch (Exception ex) {
			new ExceptionDialog(this, "Error while loading game", "Please make sure the game you are loading is valid",
					ex).setVisible(true);
		}
		this.dispose();
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
