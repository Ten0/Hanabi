package com.ten.hanabi.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import com.ten.hanabi.core.exceptions.InvalidPlayException;
import com.ten.hanabi.core.plays.*;

public class Situation {

	private final Hanabi hanabi;
	private final Variant variant;
	private int turn;

	private int clues;
	private int strikes;
	private final HashMap<Color, Integer> placedOnColor;
	private final ArrayList<Card> placedCards;
	private final ArrayList<Card> discardedCards;
	private final ArrayList<Hand> hands;

	/** The id of the next card to be picked in the deck */
	private int cardId = 0;
	/**
	 * The turn at which the last card was picked if it happened, -1 otherwise
	 */
	private int lastCardPicked = -1;

	Situation(Variant variant) {
		// Instanciation
		this.hanabi = variant.getHanabi();
		this.variant = variant;
		placedCards = new ArrayList<Card>();
		discardedCards = new ArrayList<Card>();
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
				hand.pick(this, hanabi.getDeck().getCard(player.getId() + i * hanabi.getPlayerCount()));
			}
			hands.add(hand);
		}
		this.clues = hanabi.getRuleSet().getInitialNumberOfClues();
		this.strikes = 0;
		cardId = hanabi.getNbOfCardsPerPlayer() * hanabi.getPlayerCount();
	}

	Situation(Variant variant, int endTurn) throws InvalidPlayException {
		this(variant);

		// Run all turns and update
		while(turn < endTurn) {
			nextTurn();
		}
	}

	private void nextTurn() throws InvalidPlayException {
		Play p = variant.getPlay(turn + 1);
		checkPlayValidity(p);
		turn++;
		if(p instanceof CardPlay) {
			// Remove card from hand
			Hand hand = hands.get(p.getPlayer().getId());
			Card playedCard = hand.play(((CardPlay) p).getPlacement());

			// Place it where it belongs
			if(p instanceof DiscardPlay) {
				discardedCards.add(playedCard);
			} else if(p instanceof PlacePlay) {
				if(this.canBePlaced(playedCard)) {
					placedOnColor.put(playedCard.getColor(), placedOnColor.get(playedCard.getColor()) + 1);
					placedCards.add(playedCard);
					clues += playedCard.getCluesAddedOnPlay();
				} else {
					strikes++;
					discardedCards.add(playedCard);
				}
			}

			// Pick a new card if deck is not empty
			if(cardId < hanabi.getDeck().size())
				hand.pick(this, hanabi.getDeck().getCard(cardId));
		} else if(p instanceof CluePlay) {
			CluePlay cPlay = (CluePlay) p;
			// Update info known on cards in that hand
			hands.get(cPlay.getReceiver().getId()).receiveClue(cPlay.getClue());
		}
		clues += p.getCluesAdded();
		cardId += p.getNbCardsPicked();
		if(cardId >= hanabi.getDeck().size())
			lastCardPicked = turn;
	}

	public int getPlayingPlayerId(int turn) {
		return turn % hanabi.getPlayerCount();
	}

	public int getPlayingPlayerId() {
		return getPlayingPlayerId(getTurn());
	}

	public Player getPlayingPlayer(int turn) {
		return hanabi.getPlayer(getPlayingPlayerId(turn));
	}

	public Player getPlayingPlayer() {
		return getPlayingPlayer(getTurn());
	}

	public boolean canPlay(Play play) {
		if(getPlayingPlayerId() != play.getPlayer().getId())
			return false; // Not this player's turn
		int newClues = clues + play.getCluesAdded();
		if(newClues < 0 || newClues > hanabi.getRuleSet().getMaxNumberOfClues())
			return false; // Invalid clue count
		if(strikes >= hanabi.getRuleSet().getNbStrikesUntilDeath())
			return false; // Game is already over
		return true;
	}

	public void checkPlayValidity(Play play) throws InvalidPlayException {
		if(getPlayingPlayerId() != play.getPlayer().getId())
			throw new InvalidPlayException(play, "Not this player's turn");
		int newClues = clues + play.getCluesAdded();
		if(newClues < 0)
			throw new InvalidPlayException(play, "clues<0");
		int maxClues = hanabi.getRuleSet().getMaxNumberOfClues();
		if(newClues > maxClues)
			throw new InvalidPlayException(play, "clues>" + maxClues);
		if(isGameOver())
			throw new InvalidPlayException(play, "Game is already over");
	}

	public boolean canBePlaced(Card playedCard) {
		return placedOnColor.get(playedCard.getColor()) == playedCard.getNumber() - 1;
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
		return isGameLost() || (lastCardPicked >= 0 && (getTurn() - lastCardPicked) > hanabi.getPlayerCount()
				* hanabi.getRuleSet().getNbTurnsPerPlayerAfterLastCard());
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

	public boolean canCardBeUseful(Card card) {
		return card.getNumber() > getNumberAtColor(card.getColor());
	}

	public boolean isCardUsed(Card card) {
		return isCardPlaced(card) || isCardDiscarded(card);
	}

	@Override
	public String toString() {
		String ret = "\nTurn " + getTurn() + " - " + getScore();
		for(Player p : hanabi.getPlayers())
			ret += "\n" + getHand(p).toDetailedString();
		return ret;
	}

	public int getNumberAtColor(Color c) {
		return placedOnColor.get(c);
	}

	public Collection<Card> getPlacedCards() {
		return Collections.unmodifiableCollection(placedCards);
	}

	public Collection<Card> getDiscardedCards() {
		return Collections.unmodifiableCollection(discardedCards);
	}

	public Variant getVariant() {
		return variant;
	}
}
