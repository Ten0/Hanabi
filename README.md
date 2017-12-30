# Hanabi
This software aims to provide a game engine and an interface for analyzing (and maybe playing) Hanabi games.

## Contents
It comes with :
- Core engine : *Stores games informations, links between plays and situations, checks for coherence and respect of the rules*
- Serialization : *Saves and loads games to/from files (XML)*
- Play engine : *Provides base classes to manage real time games, as they are being played*
- Bots : *Bots that can be plugged on the play engine*
- BGA loader : *Allows loading games from boardgamearena.com*
- User interface :
	+ Visualize games
	+ Open games from file (locked/non-locked deck) or BGA (locked deck)
	+ Save game to files
	+ *Real player* that can be plugged on the play engine
	+ Create real time game with *real players* (starts with a non-locked deck)
	+ Edit non-locked decks

## How to use
### General controls
- **n:** Create new real time game
- **o:** Open game (**O** to directly open BGA tab)
- **s:** Save game (**S** also check deck coherence and lock it before saving)
- **UP/LEFT DOWN/RIGHT arrows:** Move from play to play in the game
- **HOME/END:** Jump to beginning/end of game

### Real-time game controls
- Click a card to select it, then click on the board or on the discard area (just under the board) to try placing a card or discarding it
- Click a clue (top left) to select it, then click another player's card to give him that clue (also works in the opposite order)
- **r,y,g,b,w,m:** Select red, yellow, green, blue, white, multi clues
- **1,2,3,4,5:** Select 1, 2, 3, 4, 5 clues
- **BACKSPACE:** Rollback last play (when playing a real time game)
- **SPACEBAR:** Change card value (only with non-locked deck) (close window to set to unknown card)

#### Notes
- You don't need to give the value of the cards as they are picked : the card value will be asked when required, that is once the card is either played or discarded.
- Before trying to "lock deck and save game" (**S**), you should enter the values of the last cards the players have in hand for the deck to be completely known and checked. Otherwise, the check will obviously fail (missing cards).
