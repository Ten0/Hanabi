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
	+ Create real time game with *real players* (non-locked deck)
	+ Edit non-locked decks

## Shortcuts
### General shortcuts
- **n:** New game
- **o:** Open game (**O** to directly open BGA tab)
- **s:** Save game (**S** checks deck coherence and locks it if not done yet)
- **UP/LEFT DOWN/RIGHT arrows:** Move from play to play in the game
- **HOME/END:** Jump to beginning/end of game

### Real-time game shortcuts
- Click to select card, and click on board or discard pile to choose where to play
- Click to select clue, and click another player's card to give clue
- **r,y,g,b,w,m:** Select red, yellow, green, blue, white, multi clues
- **1,2,3,4,5:** Select 1, 2, 3, 4, 5 clues
- **BACKSPACE:** Cancel play (when playing a real time game)
- **SPACEBAR:** Change card value (only with non-locked deck) (close window to set to unknown card)
