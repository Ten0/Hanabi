package com.ten.hanabi.ui.play;

import java.io.File;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.seen.SeenHanabi;

public class PlayMain {

	public static void main(String[] args) throws Exception {
		// Hanabi h = new Hanabi(new Player(), new Player());
		Hanabi h = new SeenHanabi(new File("../games/g1.hanabi")).getFinalHanabi();
		h.getDeck().checkCoherence();

		UIPlayManager upm = new UIPlayManager();
		upm.loadHanabi(h, 0);

		PlayFrame pf = new PlayFrame(upm);
		pf.setVisible(true);
	}

}
