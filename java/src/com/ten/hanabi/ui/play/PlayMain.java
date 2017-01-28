package com.ten.hanabi.ui.play;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.clues.ColorClue;

public class PlayMain {

	public static void main(String[] args) throws Exception {
		UIPlayManager upm = new UIPlayManager();

		PlayFrame pf = new PlayFrame(upm);
		pf.setVisible(true);

		// Thread.sleep(3000);

		Player[] ps = new Player[4];
		for(int i = 0; i < ps.length; i++)
			ps[i] = new Player();

		RuleSet rs = new RuleSet(false);
		Hanabi h = new Hanabi(rs, ps);

		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < ps.length; j++) {
				ps[j].clue(ps[(j + 1) % ps.length], new ColorClue(Color.RED));
			}
			for(Player p : ps)
				p.discard(3);
		}

		// On joue une carte
		Player p = ps[0];
		Situation s = h.getSituation();
		Hand hand = s.getHand(p);
		for(int i = 0; i < hand.size(); i++) {
			if(s.canBePlaced(hand.get(i))) {
				p.place(i);
				break;
			}
		}

		upm.loadHanabi(h);
	}

}
