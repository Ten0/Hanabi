package com.ten.hanabi.core;

public class Card implements Comparable<Card> {

	private final Color color;
	private final int number;

	public Card(Color color, int number) {
		if(number < 1 || number > 5) { throw new RuntimeException("A card cannot have number " + number); }
		this.color = color;
		this.number = number;
	}

	public Color getColor() {
		return this.color;
	}

	public int getNumber() {
		return this.number;
	}

	public boolean matchesColor(Color color) {
		return this.color.matchesColor(color);
	}

	@Override
	public String toString() {
		return "" + getNumber() + getColor().smallName();
	}

	@Override
	public int compareTo(Card other) {
		int compareColor = this.getColor().compareTo(other.getColor());
		return compareColor != 0 ? compareColor : this.getNumber() - other.getNumber();
	}
}
