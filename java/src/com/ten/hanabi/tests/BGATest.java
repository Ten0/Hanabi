package com.ten.hanabi.tests;

import com.flecheck.hanabi.bga.BGA;
import com.ten.hanabi.core.Hanabi;
import com.ten.hanabi.ui.play.PlayFrame;
import com.ten.hanabi.ui.play.UIPlayManager;

public class BGATest {

	public static void main(String[] args) throws Exception {
		// Hanabi h = BGA.getGameById(28441973);
		Hanabi h = BGA.getGameById(30709275);

		UIPlayManager upm = new UIPlayManager();
		upm.loadHanabi(h, 0);

		PlayFrame pf = new PlayFrame(upm);
		pf.setVisible(true);
	}

}
