package com.ten.hanabi.core;

public enum Color {
	RED, YELLOW, GREEN, BLUE, WHITE, MULTI;
	
	public String smallName() {
		return this.name().substring(0, 1);
	}
	
	public boolean matchesColor(Color color) {
		return this == MULTI || this == color;
	}
}