package com.ten.hanabi.ui.play;

import com.ten.hanabi.core.*;

public class PlayMain {

	public static void main(String[] args) throws Exception {
		UIPlayManager upm = new UIPlayManager();
		
		PlayFrame pf = new PlayFrame(upm);
		pf.setVisible(true);

		Hanabi h = new Hanabi(new Player(), new Player(), new Player(), new Player());
		upm.loadHanabi(h);
	}

}
