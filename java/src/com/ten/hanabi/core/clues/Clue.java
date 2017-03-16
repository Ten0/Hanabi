package com.ten.hanabi.core.clues;

import java.util.ArrayList;
import java.util.stream.Stream;

import com.ten.hanabi.core.*;

public abstract class Clue implements Comparable<Clue> {

	@Override
	public abstract String toString();

	public abstract boolean matches(Card c);

	public static Clue fromSmall(String s) throws Exception {
		for(Color c : Color.values()) {
			if(c.smallName().equals(s))
				return new ColorClue(c);
		}
		for(int i = 1; i <= 5; i++) {
			if(Integer.toString(i).equals(s))
				return new NumberClue(i);
		}
		throw new Exception("Invalid string to generate clue:" + s);
	}

	public static Stream<Clue> getPossibleClues(Hand h, RuleSet ruleSet) {
		ArrayList<Clue> al = new ArrayList<Clue>();
		for(Color c : ruleSet.getEnabledColors())
			if(c != Color.MULTI)
				al.add(new ColorClue(c));
		for(int i = 1; i <= 5; i++)
			al.add(new NumberClue(i));

		Stream<Clue> ret = al.stream();
		if(!ruleSet.canGiveEmptyClues())
			ret = ret.filter(c -> h.getConcernedCardsCount(c) > 0);
		return ret;
	}
}
