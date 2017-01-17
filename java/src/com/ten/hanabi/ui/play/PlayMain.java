package com.ten.hanabi.ui.play;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.clues.*;

public class PlayMain {

	public static void main(String[] args) throws Exception {
		UIPlayManager upm = new UIPlayManager();
		
		PlayFrame pf = new PlayFrame(upm);
		pf.setVisible(true);

		Player[] ps = new Player[4];
		for(int i = 0; i < ps.length; i++) ps[i] = new Player();
		
		Hanabi h = new Hanabi(ps);
		
		for(int i = 0; i < 5; i++) {
			ps[0].clue(ps[1], new ColorClue(Color.RED));
			ps[1].clue(ps[2], new ColorClue(Color.RED));
			ps[2].clue(ps[3], new ColorClue(Color.RED));
			ps[3].clue(ps[0], new ColorClue(Color.RED));
			ps[0].discard(3);
			ps[1].discard(3);
			ps[2].discard(3);
			ps[3].discard(3);
		}
		
		
		upm.loadHanabi(h);
	}

}
