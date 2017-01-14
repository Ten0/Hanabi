package com.ten.hanabi.core;

public class Card {
	
	private final Color color;
	private final int number;
	
	public Card(Color color, int number) {
		if(number < 1 || number > 5) {
			throw new RuntimeException("A card cannot have number "+number);
		}
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
		return ""+getNumber()+getColor().smallName();
	}
}
