package Monopoly;

import Monopoly.LoggerTools.Logger;
import Monopoly.Tiles.OwnableTile;
import Monopoly.Tiles.Tile;
import Monopoly.Tiles.UtilityTile;

import java.util.ArrayList;

/**
 * The MonopolyGame class is responsible for handling all functions of an
 * instance of a Monopoly game.
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
	 * The maximum turns allowed to be played during a game
	 */
	private final int maxTurnsAllowed = 10000;

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
		return turnsPlayed < maxTurnsAllowed && players.size() > 1;
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
			System.out.println("Every player is now bankrupt.");
			System.exit(0);
		}

		if (players.get(currentPlayer).getBalance() < 0) {
			new Exception().printStackTrace();
		}
	}

	/**
	 * Calls upon a player to play their turn
	 */
	public void playTurn() {
		turnsPlayed++;

		if (turnsPlayed < maxTurnsAllowed) {
			players.get(currentPlayer).playTurn();
		}
	}

	/**
	 * @return The players in the current game
	 */
	public ArrayList<Player> getPlayers() {
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
		Logger.log(
				String.format("%s is paying $%d to %s for rent on %s", payer, tile.getRent(), tile.getOwner(), tile));
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
			Logger.log(String.format("%s payed $%d %s for rent on %s", payer, tile.getRent(), tile.getOwner(), tile));

			payer.deductBalance(tile.getRent());
			tile.getOwner().addBalance(tile.getRent());
		} else {
			System.out.println("Logic Error: Paying rent on non-ownable property");
		}
	}
}
