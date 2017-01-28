package com.ten.hanabi.core.clues;

import com.ten.hanabi.core.*;

public class ColorClue extends Clue {

	private final Color color;

	public ColorClue(Color color) {
		this.color = color;
		if(color == Color.MULTI) throw new RuntimeException("You cannot give a MULTI clue.");
	}

	public Color getColor() {
		return color;
	}

	@Override
	public String toString() {
		return color.name();
	}

	@Override
	public int compareTo(Clue other) {
		if(other instanceof ColorClue)
			return getColor().ordinal() - ((ColorClue) other).getColor().ordinal();
		else
			return 1;
	}

	@Override
	public boolean matches(Card card) {
		return matches(card.getColor());
	}

	public boolean matches(Color c) {
		if(c == null) throw new RuntimeException("Color shouldn't be null");
		return c == color || c == Color.MULTI;
	}
}
