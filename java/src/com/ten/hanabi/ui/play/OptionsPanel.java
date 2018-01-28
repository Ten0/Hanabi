package com.ten.hanabi.ui.play;

import javax.swing.JPanel;

import com.ten.hanabi.core.*;
import com.ten.hanabi.play.HanabiChangeListener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class OptionsPanel extends JPanel implements HanabiChangeListener, ItemListener, ChangeListener {

	private UIPlayManager uiPlayManager;
	private JComboBox<String> hideCardsDropdown;
	private JSlider playDelaySlider;

	public OptionsPanel(UIPlayManager upm) {
		uiPlayManager = upm;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel hideCardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JLabel hideCardsLabel = new JLabel("Hide cards :");
		hideCardsPanel.add(hideCardsLabel);

		hideCardsDropdown = new JComboBox<>();
		hideCardsDropdown.addItemListener(this);
		hideCardsDropdown.setFocusable(false);
		hideCardsPanel.add(hideCardsDropdown);

		add(hideCardsPanel);

		JPanel playDelayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JLabel playDelayLabel = new JLabel("Play delay in players panel");
		playDelayPanel.add(playDelayLabel);

		playDelaySlider = new JSlider(0, 5000, 2500);
		playDelaySlider.addChangeListener(this);
		playDelaySlider.setFocusable(false);
		playDelaySlider.setPaintTicks(true);
		playDelaySlider.setMinorTickSpacing(500);
		playDelaySlider.setMajorTickSpacing(1000);
		playDelaySlider.setSnapToTicks(true);

		Dictionary<Integer, Component> labelTable = new Hashtable<Integer, Component>();
		labelTable.put(0, new JLabel("0s"));
		labelTable.put(2500, new JLabel("2.5s"));
		labelTable.put(5000, new JLabel("5s"));
		playDelaySlider.setLabelTable(labelTable);
		playDelaySlider.setPaintLabels(true);

		playDelayPanel.add(playDelaySlider);
		uiPlayManager.setPlayDelay(playDelaySlider.getValue());

		add(playDelayPanel);

		upm.registerHanabiChangeListener(this);
	}

	@Override
	public void onHanabiChange(Hanabi hanabi) {
		if(hanabi == null) {
			hideCardsDropdown.setModel(new DefaultComboBoxModel<>(new String[] { "No game loaded..." }));
		} else {
			String[] dropdownValues = new String[3 + hanabi.getPlayerCount()];
			dropdownValues[0] = "None";
			dropdownValues[1] = "Everyone";
			dropdownValues[2] = "Current player";
			int i = 3;
			for(Player p : hanabi.getPlayers()) {
				dropdownValues[i++] = p.getName();
			}
			hideCardsDropdown.setModel(new DefaultComboBoxModel<>(dropdownValues));
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == hideCardsDropdown && e.getStateChange() == ItemEvent.SELECTED) {
			int sel = hideCardsDropdown.getSelectedIndex();
			if(sel == 0)
				uiPlayManager.setHiddenCardsMode(UIPlayManager.HiddenCardsMode.NONE);
			else if(sel == 1)
				uiPlayManager.setHiddenCardsMode(UIPlayManager.HiddenCardsMode.ALL);
			else if(sel == 2)
				uiPlayManager.setHiddenCardsMode(UIPlayManager.HiddenCardsMode.CURRENT);
			else
				uiPlayManager.setPlayerWhoseCardsAreHidden(uiPlayManager.getHanabi().getPlayer(sel - 3));
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == playDelaySlider) {
			uiPlayManager.setPlayDelay(playDelaySlider.getValue());
		}
	}
}
