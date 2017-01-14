package com.ten.hanabi.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import com.ten.hanabi.core.exceptions.*;
import com.ten.hanabi.core.plays.*;

public class Situation {

	private final Hanabi hanabi;
	private int turn;
	
	private int clues;
	private int strikes;
	private final HashMap<Color, Integer> placedOnColor;
	private final TreeSet<Card> placedCards;
	private final TreeSet<Card> discardedCards;
	private final ArrayList<Hand> hands;
	
	
	private int cardId = 0;
	
	Situation(Hanabi hanabi, int endTurn) throws InvalidPlayException {
		// Instanciation
		this.hanabi = hanabi;
		placedCards = new TreeSet<Card>();
		discardedCards = new TreeSet<Card>();
		placedOnColor = new HashMap<Color, Integer>();
		for(Color color : Color.values()) if (hanabi.getRuleSet().isColorEnabled(color)) {
			placedOnColor.put(color, 0);
		}
		
		// Situation in the beginning of the game
		this.hands = new ArrayList<Hand>();
		for(Player player : hanabi.getPlayers()) {
			Hand hand = new Hand(player);
			for(int i = 0; i < hanabi.getNbOfCardsPerPlayer(); i++) {
				hand.pick(hanabi.getDeck().getCard(player.getId()+i*hanabi.getPlayerCount()));
			}
			hands.add(hand);
		}
		this.clues = hanabi.getRuleSet().getInitialNumberOfClues();
		this.strikes = 0;
		cardId = hanabi.getNbOfCardsPerPlayer()*hanabi.getPlayerCount();
		
		// Run all turns and update
		for(turn = 0; turn < endTurn; turn++) {
			Play p = hanabi.getPlay(turn);
			p.checkValidity(this);
			if(p instanceof CardPlay) {
				// Remove card from hand
				Hand hand = hands.get(turn%hanabi.getPlayerCount());
				Card playedCard = hand.play(((CardPlay)p).getPlacement());
				
				// Place it where it belongs
				if(p instanceof DiscardPlay) {
					discardedCards.add(playedCard);
				}
				else if(p instanceof PlacePlay) {
					if(this.canBePlaced(playedCard)) {
						placedOnColor.put(playedCard.getColor(), placedOnColor.get(playedCard.getColor())+1);
						placedCards.add(playedCard);
					}
					else {
						strikes++;
						discardedCards.add(playedCard);
					}
				}
				
				// Pick a new card if deck is not empty
				if(cardId < hanabi.getDeck().size())
					hand.pick(hanabi.getDeck().getCard(cardId));
			}
			clues += p.getCluesAdded();
			cardId += p.getNbCardsPicked();
		}
	}
	
	public boolean canPlay(Play play) {
		if(getTurn()%hanabi.getPlayerCount() != play.getPlayer().getId()) return false; // Not this player's turn
		int newClues = clues + play.getCluesAdded();
		if(newClues < 0 || newClues > hanabi.getRuleSet().getMaxNumberOfClues()) return false; // Invalid clue count
		if(strikes >= hanabi.getRuleSet().getNbStrikesUntilDeath()) return false; // Game is already over
		return true;
	}
	
	public void checkPlayValidity(Play play) throws InvalidPlayException {
		if(getTurn()%hanabi.getPlayerCount() != play.getPlayer().getId()) throw new InvalidPlayException(play, "Not this player's turn");
		int newClues = clues + play.getCluesAdded();
		if(newClues < 0) throw new InvalidPlayException(play, "clues<0");
		int maxClues = hanabi.getRuleSet().getMaxNumberOfClues();
		if(clues > maxClues) throw new InvalidPlayException(play, "clues>"+maxClues);
		if(strikes >= hanabi.getRuleSet().getNbStrikesUntilDeath()) throw new InvalidPlayException(play, "Game is already over");
	}

	public boolean canBePlaced(Card playedCard) {
		return placedOnColor.get(playedCard.getColor()) == playedCard.getNumber()-1;
	}
	
	public int getClues() {
		return clues;
	}
	
	public int getStrikes() {
		return strikes;
	}
	
	public Hand getHand(Player p) {
		return getHand(p.getId());
	}
	
	public Hand getHand(int playerId) {
		return hands.get(playerId);
	}

	public Hanabi getHanabi() {
		return hanabi;
	}

	public int getTurn() {
		return turn;
	}

	public boolean isGameOver() {
		return strikes >= hanabi.getRuleSet().getNbStrikesUntilDeath();
	}
}
