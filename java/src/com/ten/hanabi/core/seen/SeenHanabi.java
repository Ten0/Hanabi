package com.ten.hanabi.core.seen;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.clues.Clue;
import com.ten.hanabi.core.exceptions.InvalidDeckException;
import com.ten.hanabi.core.exceptions.InvalidPlayException;
import com.ten.hanabi.core.plays.*;

public class SeenHanabi {

	private final ArrayList<Player> players = new ArrayList<Player>();
	private final ArrayList<SeenHand> hands = new ArrayList<SeenHand>();
	private final Deck deck;
	private final RuleSet ruleSet;
	private final Hanabi hanabi;
	private final Variant variant;
	private int cardId = 0;

	public SeenHanabi(Player... players) {
		ruleSet = new RuleSet();
		deck = new Deck(ruleSet, false);
		for(Player p : players) {
			addPlayer(p);
		}
		hanabi = new Hanabi(ruleSet, deck, players);
		variant = hanabi.getVariant();
		generateInitialHands();
	}

	public SeenHanabi(File gameFile) throws InvalidSeenException {
		ruleSet = new RuleSet();
		deck = new Deck(ruleSet, false);

		Scanner scanner = null;
		try {
			scanner = new Scanner(gameFile);
			String line = scanner.nextLine();
			while(line != null && !line.equals("")) {
				addPlayer(new Player(line));
				line = scanner.nextLine();
			}
			hanabi = new Hanabi(ruleSet, deck, players.toArray(new Player[players.size()]));
			variant = hanabi.getVariant();

			generateInitialHands();

			// Game running
			line = scanner.nextLine();
			while(line != null && !line.equals("")) {
				String[] lineSplit = line.split(" ");
				int pId = Integer.parseInt(lineSplit[0]);
				Player player = players.get(pId);
				char type = lineSplit[1].charAt(0);
				if(type == 'C') {
					Clue clue = Clue.fromSmall(lineSplit[2]);
					int pId2 = Integer.parseInt(lineSplit[3]);
					addPlay(pId, new CluePlay(player, players.get(pId2), clue), null);
				} else if(type == 'P' || type == 'D') {
					int cId = Integer.parseInt(lineSplit[2]);
					Card card = Card.fromSmall(lineSplit[3]);
					addPlay(pId, type == 'P' ? new PlacePlay(player, cId) : new DiscardPlay(player, cId), card);
				} else {
					throw new InvalidSeenException();
				}
				if(scanner.hasNext())
					line = scanner.nextLine();
				else
					line = null;
			}

			int pId = 0;
			while(scanner.hasNext()) {
				SeenHand hand = hands.get(pId);
				for(String s : scanner.nextLine().split(" ")) {
					hand.play(0, Card.fromSmall(s));
				}
				pId++;
			}

		} catch (Exception e) {
			throw new InvalidSeenException(e);
		} finally {
			if(scanner != null)
				scanner.close();
		}

	}

	public Hanabi getFinalHanabi() throws InvalidDeckException {
		deck.autoComplete(true);
		return getHanabi();
	}

	public Hanabi getHanabi() {
		return hanabi;
	}

	private void addPlay(int pId, Play play, Card card) throws InvalidPlayException, InvalidDeckException {
		if(play instanceof CardPlay) {
			SeenHand hand = hands.get(pId);
			hand.play(((CardPlay) play).getPlacement(), card);
			if(cardId < ruleSet.getDeckSize()) {
				hand.pick(cardId++);
			}
		}
		variant.savePlay(play);
	}

	private void addPlayer(Player p) {
		players.add(p);
		hands.add(new SeenHand(this, p));
	}

	private void generateInitialHands() {
		int nPlayers = players.size();
		int nbOfCardsPerPlayer = ruleSet.getNbOfCardsPerPlayer(nPlayers);
		for(int playerId = 0; playerId < nPlayers; playerId++) {
			SeenHand hand = hands.get(playerId);
			for(int i = 0; i < nbOfCardsPerPlayer; i++) {
				hand.pick(playerId + i * nPlayers);
			}
		}
		cardId = nbOfCardsPerPlayer * nPlayers;
	}

	public Deck getDeck() {
		return deck;
	}
}
