package com.ten.hanabi.bot;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.clues.*;
import com.ten.hanabi.core.exceptions.InvalidPlayException;
import com.ten.hanabi.core.plays.*;

/** Un bot avec une heuristique très basique pour tester */
public class StupidBot extends Bot {

	@Override
	public void play(Variant v, int turn) throws InvalidPlayException {
		Situation s = v.getSituation(turn);
		Player p = s.getPlayingPlayer();
		Hand h = s.getHand(p);

		// Si on a reçu un indice, on joue la carte la plus à gauche sur laquelle on a un indice pouvant correspondre à
		// une carte jouable
		for(int i = 0; i < h.size(); i++) {
			CardKnowlege ck = h.getKnowlege(i);
			if(ck.getColorFromClues() != null || ck.getNumberFromClues() > 0) {
				for(Card c : ck.getPossibleCards()) {
					if(s.canBePlaced(c)) {
						p.place(i);
						return;
					}
				}
			}
		}

		// Sinon si on peut donner un indice portant uniquement sur des cartes jouables, on le donne
		if(s.getClues() > 0) {
			for(int i = 1; i < hanabi.getPlayerCount(); i++) {
				Player p2 = hanabi.getPlayer((p.getId() + i) % hanabi.getPlayerCount());
				Hand h2 = s.getHand(p2);
				Clue chosenClue = Clue.getPossibleClues(h2, hanabi.getRuleSet())
						.sorted((Clue c1, Clue c2) -> h2.getConcernedCardsCount(c2) - h2.getConcernedCardsCount(c1))
						.filter(clue -> {
							for(Card c : h2) {
								if(clue.matches(c) && !s.canBePlaced(c))
									return false;
							}
							return true;
						}).findFirst().orElse(null);
				if(chosenClue != null) {
					p.clue(p2, chosenClue);
					return;
				}
			}
		}

		// Sinon si on peut défausser, on défausse la carte la plus à droite qui ne peut plus servir
		if(s.getClues() < hanabi.getRuleSet().getMaxNumberOfClues()) {
			for(int i = h.size() - 1; i >= 0; i--) {
				CardKnowlege ck = h.getKnowlege(i);
				boolean canDiscard = true;
				for(Card c : ck.getPossibleCardsWithoutDupplicates()) {
					if(s.canCardBeUseful(c)) {
						canDiscard = false;
						break;
					}
				}
				if(canDiscard) {
					p.discard(i);
					return;
				}
			}
		}

		// Sinon si on peut défausser, on défausse la carte la plus à droite sur laquelle on n'a pas reçu d'indice
		if(s.getClues() < hanabi.getRuleSet().getMaxNumberOfClues()) {
			for(int i = h.size() - 1; i >= 0; i--) {
				CardKnowlege ck = h.getKnowlege(i);
				if(ck.getColorFromClues() == null && ck.getNumberFromClues() <= 0) {
					p.discard(i);
					return;
				}
			}
		}

		// Sinon si on peut donner un indice, on le donne
		if(s.getClues() > 0) {
			Player toPlayer = hanabi.getPlayer((p.getId() + 1) % hanabi.getPlayerCount());
			for(int i = 5; i > 0; i--) {
				Play play = new CluePlay(p, toPlayer, new NumberClue(5));
				if(s.canPlay(play)) { // This is required due to possible empty clues not being allowed
					hanabi.savePlay(play);
					return;
				}
			}
		}

		// Sinon on défausse la carte la plus à droite
		p.discard(h.size() - 1);
	}

}
