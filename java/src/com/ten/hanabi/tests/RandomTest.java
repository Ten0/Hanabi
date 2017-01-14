package com.ten.hanabi.tests;

import java.util.HashSet;

import com.ten.hanabi.core.*;

public class RandomTest {

	public static void main(String[] args) {
		Card c1 = new Card(Color.BLUE, 1);
		Card c2 = new Card(Color.BLUE, 1);
		HashSet<Card> cs = new HashSet<Card>();
		cs.add(c1);
		cs.add(c1);
		cs.add(c2);
		System.out.println(cs.size());
	}

}
