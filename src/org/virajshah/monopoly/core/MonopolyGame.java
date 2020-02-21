package org.virajshah.monopoly.core;

import java.util.ArrayList;
import java.util.List;

import org.virajshah.monopoly.logs.Logger;
import org.virajshah.monopoly.tiles.OwnableTile;
import org.virajshah.monopoly.tiles.Tile;
import org.virajshah.monopoly.tiles.UtilityTile;

/**
 * The MonopolyGame class is responsible for handling all functions of an
 * instance of a Monopoly game.
 * 
 * @author Viraj Shah
 */
public class MonopolyGame {
	/**
	 * An array of tiles representing the 40 tiles on a monopoly game board
	 */
	private Tile[] board;

	/**
	 * An ArrayList containing all the players in the current game
	 */
	private ArrayList<Player> players;

	/**
	 * A pointer to an element in MonopolyGame.players; a symbol for who's turn it
	 * is next
	 */
	private int currentPlayer;

	/**
	 * A counter for the number of turns played
	 */
	private int turnsPlayed;
	
	/**
	 * The logger tool for this monopoly game
	 */
	private Logger logger = new Logger(true);

	/**
	 * The maximum turns allowed to be played during a game
	 */
	private static final int MAX_TURNS_ALLOWED = 10000;

	/**
	 * Initializes a new monopoly game with a new board, and four players.
	 */
	public MonopolyGame() {
		board = Tile.buildBoard();

		players = new ArrayList<>();
		players.add(new Player("North", this));
		players.add(new Player("East", this));
		players.add(new Player("South", this));
		players.add(new Player("West", this));

		currentPlayer = -1;
		turnsPlayed = 0;
	}

	/**
	 * Checks if the game is still in a playable state
	 * 
	 * @return True if the game is still in a playable state, false otherwise
	 */
	public boolean isRunning() {
		return turnsPlayed < MAX_TURNS_ALLOWED && players.size() > 1;
	}

	/**
	 * Chance the current player to the next player which is still in the game
	 */
	public void nextPlayer() {
		currentPlayer++;

		if (currentPlayer >= players.size())
			currentPlayer = 0;

		try {
			if (players.get(currentPlayer).isBankrupt())
				nextPlayer();
		} catch (StackOverflowError e) {
			logger.error("Every player is now bankrupt.");
			System.exit(0);
		}
	}

	/**
	 * Calls upon a player to play their turn
	 */
	public void playTurn() {
		turnsPlayed++;

		if (turnsPlayed < MAX_TURNS_ALLOWED) {
			players.get(currentPlayer).playTurn();
		}
	}

	/**
	 * Returns the percentage chance of a player winning, based on their assets and
	 * balance
	 * 
	 * @param player The player to measure percent chance of winning
	 * @return The percent change of a the player winning
	 */
	public double chanceOfWinning(Player player) {
		int capitalization = 0;
		int playersValue = 0;
		int currValue;
		for (Player p : getPlayers()) {
			TradeBroker currBroker = new TradeBroker(p);
			currValue = 0;
			for (OwnableTile asset : p.getAssets())
				currValue += currBroker.valueToClient(asset);

			capitalization += currValue;

			if (player == p)
				playersValue = currValue;
		}
		return capitalization != 0 ? (double) playersValue / capitalization : 0;

	}

	/**
	 * @return The players in the current game
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Get the game tile (Tile) at the specified index
	 *
	 * @param index The index of the specified tile
	 * @return The tile at the specified index
	 */
	public Tile tileAt(int index) {
		return board[index];
	}

	/**
	 * Pay rent on a property (overloaded method for rent on utilities)
	 *
	 * @param payer The player object paying the rent
	 * @param tile  The tile to pay rent on
	 * @param roll  The dice roll
	 */
	public void payRent(Player payer, OwnableTile tile, int roll) {
		if (tile.getType() == Tile.TileType.UTILITY)
			((UtilityTile) tile).setLastDiceRoll(roll);
		payer.payTo(tile.getOwner(), tile.getRent());
	}

	/**
	 * Pay rent on a property
	 *
	 * @param payer The player object paying the rent
	 * @param tile  The tile to pay rent on
	 */
	public void payRent(Player payer, OwnableTile tile) {
		if (tile.getType() == Tile.TileType.PROPERTY || tile.getType() == Tile.TileType.RAILROAD) {

			payer.deductBalance(tile.getRent());
			tile.getOwner().addBalance(tile.getRent());
		} else {
			logger.error("Logic Error: Paying rent on non-ownable property");
		}
	}
	
	/**
	 * 
	 * @return The logger associated with the monopoly game
	 */
	public Logger getLogger() {
		return logger;
	}
}
