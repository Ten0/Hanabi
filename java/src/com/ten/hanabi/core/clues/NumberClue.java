package com.ten.hanabi.core.clues;

public class NumberClue extends Clue {

	private final int number;
	
	public NumberClue(int n) {
		number = n;
	}

	public int getNumber() {
		return number;
	}

	@Override
	public String toString() {
		return Integer.toString(number);
	}
}
