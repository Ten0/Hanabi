package com.ten.hanabi.tests;

import com.flecheck.hanabi.bga.BGA;
import com.ten.hanabi.core.Hanabi;
import com.ten.hanabi.serialization.XMLSerializer;
import com.ten.hanabi.ui.play.PlayFrame;
import com.ten.hanabi.ui.play.UIPlayManager;

public class SerializationTest {

	public static void main(String[] args) throws Exception {
		/*
		 * Hanabi h = BGA.getGameById(28441973);
		 * 
		 * XMLSerializer.toXML(h, "../games/G1.hnb");
		 */

		Hanabi h = XMLSerializer.loadHanabi("../games/G1.hnb");

		UIPlayManager upm = new UIPlayManager();
		upm.loadHanabi(h, 0);

		PlayFrame pf = new PlayFrame(upm);
		pf.setVisible(true);

	}

}
