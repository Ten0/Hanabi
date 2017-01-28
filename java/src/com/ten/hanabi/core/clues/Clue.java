package com.ten.hanabi.core.clues;

import com.ten.hanabi.core.*;

public abstract class Clue implements Comparable<Clue> {

	@Override
	public abstract String toString();

	public abstract boolean matches(Card c);
}
