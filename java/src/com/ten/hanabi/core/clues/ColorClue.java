package com.ten.hanabi.core.clues;

import com.ten.hanabi.core.*;

public class ColorClue extends Clue {
	
	private final Color color;

	public ColorClue(Color c) {
		color = c;
	}

	public Color getColor() {
		return color;
	}
	
	@Override
	public String toString() {
		return color.name();
	}
}
