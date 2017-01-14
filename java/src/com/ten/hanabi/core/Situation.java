package com.ten.hanabi.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.ten.hanabi.core.plays.*;

public class Situation {

	private final Hanabi hanabi;
	private final int turn;
	
	private int clues;
	private int strikes;
	private final HashMap<Color, Integer> placedOnColor;
	private final HashSet<Card> placedCards;
	private final HashSet<Card> discardedCards;
	private final ArrayList<Hand> playersHands;
	
	
	private int cardId = 0;
	
	Situation(Hanabi hanabi, int turn) {
		// Instanciation
		this.hanabi = hanabi;
		this.turn = turn;
		placedOnColor = new HashMap<Color, Integer>();
		for(Color color : Color.values()) if (hanabi.getRuleSet().isColorEnabled(color)) {
			placedOnColor.put(color, 0);
		}
		
		// Situation in the beginning of the game
		ArrayList<ArrayList<Card>> hands = new ArrayList<ArrayList<Card>>();
		for(Player player : hanabi.getPlayers()) {
			ArrayList<Card> cards = new ArrayList<Card>();
			for(int i = 0; i < hanabi.getNbOfCardsPerPlayer(); i++) {
				cards.add(hanabi.getDeck().getCard(player.getId()+i*hanabi.getPlayerCount()));
			}
			hands.add(cards);
		}
		this.clues = hanabi.getRuleSet().getInitialNumberOfClues();
		this.strikes = 0;
		cardId = hanabi.getNbOfCardsPerPlayer()*hanabi.getPlayerCount();
		
		// Run all turns and update
		for(int cTurn = 0; cTurn < turn; cTurn++) {
			Play p = hanabi.getPlay(cTurn);
			if(p instanceof CardPlay) {
				// Remove card from hand
				ArrayList<Card> cards = hands.get(cTurn%hanabi.getPlayerCount());
				Card playedCard = cards.remove(((CardPlay)p).getPlacement());
				
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
				
				// Pick a new card
				cards.add(0, hanabi.getDeck().getCard(cardId));
			}
			else if(p instanceof CluePlay) {
				
			}
			clues += p.getCluesAdded();
			cardId += p.getNbCardsPicked();
		}
		
		this.playersHands = new ArrayList<Hand>();
		for(int playerId = 0; playerId < hanabi.getPlayerCount(); playerId++) {
			playersHands.add(new Hand(hanabi.getPlayer(playerId), turn, hands.get(playerId)));
		}
	}
}
