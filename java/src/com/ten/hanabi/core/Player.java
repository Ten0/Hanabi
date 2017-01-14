package com.ten.hanabi.core;

public class Player {

	private String name;
	private Hanabi hanabi;
	
	public Player(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	void setHanabi(Hanabi hanabi) {
		this.hanabi = hanabi;
	}
}
