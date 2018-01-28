package com.ten.hanabi.ui.play;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.JPanel;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.exceptions.InvalidPlayException;
import com.ten.hanabi.core.plays.*;
import com.ten.hanabi.play.SituationChangeListener;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

public class LogPanel extends JPanel implements SituationChangeListener {
	private JTextArea textArea;

	/**
	 * Create the panel.
	 */
	public LogPanel(UIPlayManager upm) {
		setLayout(new BorderLayout(0, 0));

		textArea = new JTextArea();
		textArea.setFocusable(false);

		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane);

		upm.registerSituationChangeListener(this);
	}

	@Override
	public void onSituationChange(Situation s) {
		if(s == null) {
			textArea.setText("");
		} else {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			for(int i = 1; i <= s.getTurn(); i++) {
				Play p = s.getVariant().getPlay(i);
				ps.print(i);
				ps.print(": ");
				ps.print(p);
				if(p instanceof CardPlay) {
					CardPlay cp = (CardPlay) p;
					try {
						Situation prevSituation = s.getVariant().getSituation(i - 1);
						Card playedCard = prevSituation.getHand(cp.getPlayer()).get(cp.getPlacement());
						ps.print(" -> " + playedCard);
						if(p instanceof PlacePlay) {
							if(!prevSituation.canBePlaced(playedCard))
								ps.print(" -> Strike!");
						}
					} catch (InvalidPlayException e) {
						throw new RuntimeException(e);
					}
				}
				ps.println();
			}
			String content = new String(baos.toByteArray());
			textArea.setText(content);
			textArea.setCaretPosition(textArea.getDocument().getLength()); // Scroll to the bottom
		}
	}

}
