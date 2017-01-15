package com.ten.hanabi.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.ten.hanabi.core.exceptions.*;
import com.ten.hanabi.core.plays.*;

public class Situation {

	private final Hanabi hanabi;
	private int turn;
	
	private int clues;
	private int strikes;
	private final HashMap<Color, Integer> placedOnColor;
	private final HashSet<Card> placedCards;
	private final HashSet<Card> discardedCards;
	private final ArrayList<Hand> hands;
	
	/** The id of the next card to be picked in the deck */
	private int cardId = 0;
	/** The turn at which the last card was picked if it happened, -1 otherwise */
	private int lastCardPicked = -1;
	
	Situation(Hanabi hanabi) {
		// Instanciation
		this.hanabi = hanabi;
		placedCards = new HashSet<Card>();
		discardedCards = new HashSet<Card>();
		placedOnColor = new HashMap<Color, Integer>();
		for(Color color : hanabi.getRuleSet().getEnabledColors()) {
			placedOnColor.put(color, 0);
		}
		this.turn = 0;
		
		// Situation in the beginning of the game
		this.hands = new ArrayList<Hand>();
		for(Player player : hanabi.getPlayers()) {
			Hand hand = new Hand(player);
			for(int i = 0; i < hanabi.getNbOfCardsPerPlayer(); i++) {
				hand.pick(this, hanabi.getDeck().getCard(player.getId()+i*hanabi.getPlayerCount()));
			}
			hands.add(hand);
		}
		this.clues = hanabi.getRuleSet().getInitialNumberOfClues();
		this.strikes = 0;
		cardId = hanabi.getNbOfCardsPerPlayer()*hanabi.getPlayerCount();
	}
	Situation(Hanabi hanabi, int endTurn) throws InvalidPlayException {
		this(hanabi);
		
		// Run all turns and update
		while(turn < endTurn) {
			nextTurn();
		}
	}
	private void nextTurn() throws InvalidPlayException {
		turn++;
		Play p = hanabi.getPlay(turn);
		checkPlayValidity(p);
		if(p instanceof CardPlay) {
			// Remove card from hand
			Hand hand = hands.get(getPlayingPlayerId());
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
				hand.pick(this, hanabi.getDeck().getCard(cardId));
		}
		else if(p instanceof CluePlay) {
			CluePlay cPlay = (CluePlay)p;
			// Update info known on cards in that hand
			hands.get(cPlay.getReceiver().getId()).receiveClue(cPlay.getClue());
		}
		clues += p.getCluesAdded();
		cardId += p.getNbCardsPicked();
		if(cardId >= hanabi.getDeck().size()) lastCardPicked = turn;
	}
	
	private int getPlayingPlayerId() {
		return (getTurn()-1)%hanabi.getPlayerCount();
	}
	
	public boolean canPlay(Play play) {
		if(getPlayingPlayerId() != play.getPlayer().getId()) return false; // Not this player's turn
		int newClues = clues + play.getCluesAdded();
		if(newClues < 0 || newClues > hanabi.getRuleSet().getMaxNumberOfClues()) return false; // Invalid clue count
		if(strikes >= hanabi.getRuleSet().getNbStrikesUntilDeath()) return false; // Game is already over
		return true;
	}
	
	public void checkPlayValidity(Play play) throws InvalidPlayException {
		if(getPlayingPlayerId() != play.getPlayer().getId()) throw new InvalidPlayException(play, "Not this player's turn");
		int newClues = clues + play.getCluesAdded();
		if(newClues < 0) throw new InvalidPlayException(play, "clues<0");
		int maxClues = hanabi.getRuleSet().getMaxNumberOfClues();
		if(newClues > maxClues) throw new InvalidPlayException(play, "clues>"+maxClues);
		if(isGameOver()) throw new InvalidPlayException(play, "Game is already over");
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

	public boolean isGameLost() {
		return strikes >= hanabi.getRuleSet().getNbStrikesUntilDeath();
	}
	
	public boolean isGameOver() {
		return isGameLost() || (lastCardPicked >= 0 &&
			(getTurn() - lastCardPicked) > hanabi.getPlayerCount()*hanabi.getRuleSet().getNbTurnsPerPlayerAfterLastCard());
	}
	
	public int getScore() {
		return placedCards.size();
	}
	
	public boolean isCardPlaced(Card card) {
		return placedCards.contains(card);
	}
	
	public boolean isCardDiscarded(Card card) {
		return discardedCards.contains(card);
	}
	
	public boolean isCardUsed(Card card) {
		return isCardPlaced(card) || isCardDiscarded(card);
	}
	
	@Override
	public String toString() {
		String ret = "\nTurn "+getTurn()+" - "+getScore();
		for(Player p : hanabi.getPlayers())
			ret += "\n"+getHand(p).toDetailedString();
		return ret;
	}
}
