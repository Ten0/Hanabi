package com.ten.hanabi.core;

public enum Color {
	RED(java.awt.Color.RED),
	YELLOW(java.awt.Color.YELLOW),
	GREEN(java.awt.Color.GREEN),
	BLUE(java.awt.Color.BLUE),
	WHITE(java.awt.Color.GRAY),
	MULTI(java.awt.Color.MAGENTA);
	
	private java.awt.Color awtColor;

	private Color(java.awt.Color awtColor) {
		this.awtColor = awtColor;
	}
	
	public String smallName() {
		return this.name().substring(0, 1);
	}
	
	public boolean matchesColor(Color color) {
		return this == MULTI || this == color;
	}

	public java.awt.Color getAwtColor() {
		return awtColor;
	}
}