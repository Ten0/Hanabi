package com.ten.hanabi.core.clues;

import com.ten.hanabi.core.Card;

public class NumberClue extends Clue {

	private final int number;

	public NumberClue(int n) {
		number = n;
		if(n < 1 || n > 5)
			throw new RuntimeException("You cannot give a number " + n + " clue.");
	}

	public int getNumber() {
		return number;
	}

	@Override
	public String toString() {
		return Integer.toString(number);
	}

	@Override
	public int compareTo(Clue o) {
		if(o instanceof NumberClue)
			return this.getNumber() - ((NumberClue) o).getNumber();
		else
			return -1;
	}

	@Override
	public boolean matches(Card c) {
		return c.getNumber() == number;
	}
}
