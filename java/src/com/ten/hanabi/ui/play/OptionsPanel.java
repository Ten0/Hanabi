package com.ten.hanabi.ui.play;

import javax.swing.JPanel;

import com.ten.hanabi.core.*;
import com.ten.hanabi.play.HanabiChangeListener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import java.awt.FlowLayout;

public class OptionsPanel extends JPanel implements HanabiChangeListener, ItemListener {
	private UIPlayManager uiPlayManager;
	private JComboBox<String> hideCardsDropdown;

	public OptionsPanel(UIPlayManager upm) {
		uiPlayManager = upm;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel hideCardsPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) hideCardsPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(hideCardsPanel);

		JLabel hideCardsLabel = new JLabel("Hide cards :");
		hideCardsPanel.add(hideCardsLabel);

		hideCardsDropdown = new JComboBox<>();
		hideCardsDropdown.addItemListener(this);
		hideCardsDropdown.setFocusable(false);
		hideCardsPanel.add(hideCardsDropdown);

		upm.registerHanabiChangeListener(this);
	}

	@Override
	public void onHanabiChange(Hanabi hanabi) {
		if(hanabi == null) {
			hideCardsDropdown.setModel(new DefaultComboBoxModel<>(new String[] { "No game loaded..." }));
		} else {
			String[] dropdownValues = new String[2 + hanabi.getPlayerCount()];
			dropdownValues[0] = "None";
			dropdownValues[1] = "Current player";
			int i = 2;
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
				uiPlayManager.setHiddenCardsMode(UIPlayManager.HiddenCardsMode.CURRENT);
			else
				uiPlayManager.setPlayerWhoseCardsAreHidden(uiPlayManager.getHanabi().getPlayer(sel - 2));
		}
	}
}
