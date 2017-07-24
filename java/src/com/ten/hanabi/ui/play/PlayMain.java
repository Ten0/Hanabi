package com.ten.hanabi.ui.play;

public class PlayMain {

	public static void main(String[] args) throws Exception {
		UIPlayManager upm = new UIPlayManager();

		PlayFrame pf = new PlayFrame(upm);
		pf.setVisible(true);
	}

}
