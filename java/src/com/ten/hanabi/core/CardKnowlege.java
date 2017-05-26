package com.ten.hanabi.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

import com.ten.hanabi.core.clues.*;
import com.ten.hanabi.core.exceptions.InvalidClueException;

/** All the knowlege we have on a card related to its clues */
public class CardKnowlege {
	private final int turnEntered;
	private final Situation situation;
	private final TreeSet<Clue> clues;
	private final TreeSet<Clue> negativeClues;

	private final TreeSet<Color> possibleColors;
	private final TreeSet<Integer> possibleNumbers;
	private final HashSet<Card> possibleCards;

	CardKnowlege(Situation situation, int turnEntered) {
		this.situation = situation;
		this.turnEntered = turnEntered;
		clues = new TreeSet<Clue>();
		negativeClues = new TreeSet<Clue>();

		possibleColors = new TreeSet<Color>();
		for(Color c : situation.getHanabi().getRuleSet().getEnabledColors())
			possibleColors.add(c);
		possibleNumbers = new TreeSet<Integer>();
		for(int i = 1; i <= 5; i++)
			possibleNumbers.add(i);
		possibleCards = new HashSet<Card>();
		for(Card c : situation.getHanabi().getDeck())
			possibleCards.add(c);
	}

	void knowClue(Clue clue) {
		clues.add(clue);
		checkCoherence(clue);
	}

	void knowNegativeClue(Clue clue) {
		negativeClues.add(clue);
		checkCoherence(clue);
	}

	private void checkCoherence(Clue clue) {
		if(getPossibleColors().size() == 0 || getPossibleNumbers().size() == 0)
			throw new InvalidClueException(clue, "Data on a card is incoherent, no possibilities left");
	}

	public int getAge() {
		return (situation.getTurn() - turnEntered) / situation.getHanabi().getPlayerCount();
	}

	/** Limited to clues, won't count cards */
	public TreeSet<Color> getPossibleColors() {
		Iterator<Color> it = possibleColors.iterator();
		while(it.hasNext()) {
			Color color = it.next();
			boolean remove = false;
			for(Clue clue : clues) {
				if(clue instanceof ColorClue) {
					ColorClue cClue = (ColorClue) clue;
					if(!cClue.matches(color)) {
						remove = true;
						break;
					}
				}
			}
			if(!remove) {
				for(Clue clue : negativeClues) {
					if(clue instanceof ColorClue) {
						ColorClue cClue = (ColorClue) clue;
						if(cClue.matches(color)) {
							remove = true;
							break;
						}
					}
				}
			}
			if(remove) {
				it.remove();
			}

		}

		return possibleColors;

	}

	/** Limited to clues, won't count cards */
	public Color getColorFromClues() {
		if(getPossibleColors().size() == 1)
			return possibleColors.toArray(new Color[1])[0];
		else
			return null;
	}

	public TreeSet<Integer> getPossibleNumbers() {
		Iterator<Integer> it = possibleNumbers.iterator();
		while(it.hasNext()) {
			int number = it.next();
			boolean remove = false;
			for(Clue clue : clues)
				if(clue instanceof NumberClue) {
					NumberClue nClue = (NumberClue) clue;
					if(nClue.getNumber() != number) {
						remove = true;
						break;
					}
				}
			if(!remove) {
				for(Clue clue : negativeClues)
					if(clue instanceof NumberClue) {
						NumberClue nClue = (NumberClue) clue;
						if(nClue.getNumber() == number) {
							remove = true;
							break;
						}
					}
			}
			if(remove) {
				it.remove();
			}

		}

		return possibleNumbers;
	}

	/** Limited to clues, won't count cards */
	public int getNumberFromClues() {
		if(getPossibleNumbers().size() == 1)
			return possibleNumbers.toArray(new Integer[1])[0];
		else
			return -1;
	}

	/** Also counting cards */
	public HashSet<Card> getPossibleCards() {
		Iterator<Card> it = possibleCards.iterator();
		TreeSet<Integer> pn = getPossibleNumbers();
		TreeSet<Color> pc = getPossibleColors();
		while(it.hasNext()) {
			Card card = it.next();
			if(!pc.contains(card.getColor()) || !pn.contains(card.getNumber()) || situation.isCardUsed(card)) {
				it.remove();
			}
		}

		return possibleCards;
	}

	/** Also counting cards */
	public TreeSet<Card> getPossibleCardsWithoutDupplicates() {
		TreeSet<Card> res = new TreeSet<Card>();
		for(Card c : getPossibleCards())
			res.add(c);
		return res;
	}

	@Override
	public String toString() {
		String r = getAge() + " | ";
		for(int n : getPossibleNumbers()) {
			r += n + ",";
		}
		r = r.substring(0, r.length() - 1) + " | ";
		for(Color c : getPossibleColors()) {
			r += c.smallName() + ",";
		}
		r = r.substring(0, r.length() - 1);
		return r;
	}
}
