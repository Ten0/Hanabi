package com.ten.hanabi.tests;

import java.io.PrintStream;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.exceptions.InvalidPlayException;

public class GameEngineTest {
	
	private static PrintStream so = System.out;
	
	private static Player p1 = new Player("P1");
	private static Player p2 = new Player("P2");
	private static Player p3 = new Player("P3");
	private static Player[] ps = new Player[]{p1, p2, p3};
	
	private static RuleSet rs = new RuleSet();
	private static Deck d = new Deck(rs, false);
	private static Hanabi h = new Hanabi(rs, d, ps);

	public static void main(String[] args) {
		so.println(h.getDeck());
		
		printState();
		
		p1.discard(0);
		p2.discard(0);
		p3.place(0);
		p1.place(1);
		p2.discard(1);
		p3.place(1);
		
		printState();
	}
	
	public static void printState() {
		try {
			so.println("\nTurn "+h.getTurn());
			Situation s = h.getSituation();
			for(Player p : ps)
				so.println(s.getHand(p));
		} catch (InvalidPlayException e) {
			e.printStackTrace();
		}
	}

}
