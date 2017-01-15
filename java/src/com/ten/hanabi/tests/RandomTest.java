package com.ten.hanabi.tests;

import java.util.*;

import com.ten.hanabi.core.*;

public class RandomTest {

	public static void main(String[] args) {
		HashSet<Color> cs = new HashSet<Color>();
		cs.add(Color.values()[1]);
		cs.add(Color.values()[1]);
		cs.remove(Color.values()[1]);
		System.out.println(cs.size());
	}

}
