package game;

import java.io.Serializable;
import java.util.ArrayList;
import game.mode.Mode;
import main.Player;

/*
 *  See it as follow: the Game class is divided in two classes:
 *  One for handeling the stuff before the game actually begins (for ex: adding players to the game)
 *  One for handeling the stuff that happens in the game (for ex: moving playersprites)
 */
public class PreGame implements Serializable 
{
	private static final long serialVersionUID = 1L;
	public Mode mode; // the game mode
	public final Environment origEv; // the original environment
	public int amountPlayers; // the amount of players
	public int gameId;
	public Environment ev; // the environment will ofc get changed during the game
	public ArrayList<Player> players = new ArrayList<Player>(); // a list of players: also contains all the usernames
	public boolean full = false;
	public boolean loading;
	
	public PreGame(Mode mode, Environment ev, int amountPlayers, int gameId) {
		this.mode = mode;
		this.origEv = ev; this.ev = ev;
		this.amountPlayers = amountPlayers;
		this.gameId = gameId;
		for (int x = 0; x<amountPlayers; x++) // fill the playerlist with nulls: when a player joins, get the next spot that is free and put the player on that spot
		{
			players.add(null);
		}
		// if amountPlayers is 5 then players looks like [null, null, null, null, null]
	}
	
	public void startGame() {
		loading = true; // this way players can see if the game still is loading (were not using it atm)
		full = true;
		this.mode.addPlayerSprites(this); // add the players to the environment
		System.out.println("Game " + gameId + " started");
		loading = false; 
	}

	public int addPlayer(Player player) 
	{
		if (!full) {
			int index = getNextFreeSpot();
			players.set(index, player);
			System.out.println(player.username + " has joined game " + gameId);
			checkIfFull();
			return index;
		}
		return -1;
	}
	
	public void checkIfFull() {
		for (Player p: players) {
			if (p == null) return; // this means the game is not full
		}
		// if the game is full we reach this code
		startGame();
	}
	
	public Player getPlayerByPort(int port) {
		for (Player p: players) {
			if (p.port == port) {return p;}
		}
		return null;
	}
	
	public int getNextFreeSpot() 
	{
		for (int i = 0; i<players.size(); i++) {
	        if (players.get(i) == null) return i; 
	    }
		return -1; // this will never happen because if all the spots are taken the game will start
	}

	public void removePlayerByPort(int port) {
		Player player = getPlayerByPort(port);
		int index = players.indexOf(player);
		players.set(index, null); // make a spot free in the game
		System.out.println(player.username + " has left game " + gameId);
	}

}
