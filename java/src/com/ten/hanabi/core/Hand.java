package com.ten.hanabi.core;

import java.util.ArrayList;
import java.util.Iterator;

import com.ten.hanabi.core.plays.*;

public class Hand implements Iterable<Card> {

	private final Hanabi hanabi;
	private final Player player;
	private final int turn;
	private final ArrayList<Card> cards;
	
	private int cardId = 0;
	
	/*Hand(Hand previousHand, int turn) {
		this.player = previousHand.player;
		this.turn = turn;
		this.hanabi = player.getHanabi();
		
		cards = new ArrayList<Card>();
		for(Card c : previousHand) cards.add(c);
		cardId = previousHand.cardId;
		for(int cTurn = previousHand.getTurn(); cTurn < turn; cTurn++) {
			Play p = hanabi.getPlay(cTurn);
			if(p.getPlayer() == player && p instanceof CardPlay) {
				cards.remove(((CardPlay)p).getPlacement());
				cards.add(0, hanabi.getDeck().getCard(cardId));
			}
			cardId += p.getNbCardsPicked();
		}
	}*/
	
	/*Hand(Player player, int turn) {
		this(player, turn, new ArrayList<Card>());
		for(int i = 0; i < hanabi.getNbOfCardsPerPlayer(); i++) {
			cards.add(hanabi.getDeck().getCard(player.getId()+i*hanabi.getPlayerCount()));
		}
		
		cardId = hanabi.getNbOfCardsPerPlayer()*hanabi.getPlayerCount();
		for(int cTurn = 0; cTurn < turn; cTurn++) {
			Play p = hanabi.getPlay(cTurn);
			if(p.getPlayer() == player && p instanceof CardPlay) {
				cards.remove(((CardPlay)p).getPlacement());
				cards.add(0, hanabi.getDeck().getCard(cardId));
			}
			cardId += p.getNbCardsPicked();
		}
	}*/
	
	Hand(Player player, int turn, ArrayList<Card> cards) {
		this.player = player;
		this.turn = turn;
		this.hanabi = player.getHanabi();
		this.cards = cards;
	}
	
	public Card getCard(int id) {
		return cards.get(id);
	}

	@Override
	public Iterator<Card> iterator() {
		return cards.iterator();
	}
	
	public int size() {
		return cards.size();
	}

	public Player getPlayer() {
		return player;
	}

	public int getTurn() {
		return turn;
	}
	
	public Hanabi getHanabi() {
		return hanabi;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int i = 0; i < size(); i++) {
			s += getCard(i);
			if(i != size()-1) s += " ";
		}
		return s;
	}
}
