package com.ten.hanabi.ui.play;

import java.io.File;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.seen.SeenHanabi;

public class PlayMain {

	public static void main(String[] args) throws Exception {
		// Hanabi h = new Hanabi(new Player(), new Player());
		File seenFile = new File("../games/g4.hanabi");
		Hanabi h = new SeenHanabi(seenFile).getHanabi();

		UIPlayManager upm = new UIPlayManager();
		upm.loadHanabi(h, 0);

		PlayFrame pf = new PlayFrame(upm);
		pf.setVisible(true);

		while(true) {
			Thread.sleep(2000);
			Hanabi h2 = new SeenHanabi(seenFile).getHanabi();
			if(h2.getTurn() > h.getTurn()) {
				h = h2;
				upm.loadHanabi(h);
			}
		}
	}

}
