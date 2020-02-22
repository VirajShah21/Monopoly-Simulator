package org.virajshah.monopoly.core;

import java.util.ArrayList;
import java.util.Random;
import org.virajshah.monopoly.tiles.FreeParkingTile;
import org.virajshah.monopoly.tiles.OwnableTile;
import org.virajshah.monopoly.tiles.PropertyTile;
import org.virajshah.monopoly.tiles.RailroadTile;
import org.virajshah.monopoly.tiles.Tile;
import org.virajshah.monopoly.tiles.UtilityTile;
import org.virajshah.monopoly.logs.Logger;

/**
 * The Card class provides a container to hold data about Chance and Community
 * Chest cards, and allows for operations on to be done on a Player object.
 * 
 * @author Viraj Shah
 */
public class Card {
	/**
	 * Instead of duplicating the literal "pay 50;" 3 times... SonarLint
	 */
	private static final String PAY_50 = "pay 50;";

	/**
	 * Logger (just for printing errors)
	 */
	private static Logger logger = new Logger(true);

	/**
	 * The random number generator object
	 */
	private static final Random randomGenerator = new Random();

	/**
	 * The message read on the card
	 */
	private String message;

	/**
	 * The calls to action for when a user picks up the card
	 */
	private String call;

	/**
	 * Initialize a new Card object
	 *
	 * @param message The message which is read on the card
	 * @param call    The actions to call when a player picks up the card
	 */
	public Card(String message, String call) {
		this.message = message;
		this.call = call;
	}

	/**
	 * @return The message read on the chance/community chest card
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return The call to actions to be processed when a player picks up the card
	 */
	public String getCall() {
		return call;
	}

	/**
	 * Helper method for pickup(Player). It cleans the commands listed in the card
	 * info.
	 * 
	 * @param commands The array of commands to clean
	 */
	private void cleanCommands(String[] commands) {
		for (int i = 0; i < commands.length; i++) {
			String currCall = commands[i];
			while (currCall.charAt(0) == ' ')
				currCall = currCall.substring(1);

			while (currCall.charAt(currCall.length() - 1) == ' ')
				currCall = currCall.substring(0, currCall.length() - 1);

			commands[i] = currCall;
		}
	}

	/**
	 * Helper method for pickup(Player). It advances a player to the specified tile
	 * index
	 * 
	 * @param player   The player to be moved
	 * @param location The tile index to move Player to
	 */
	private static void advancePlayerTo(Player player, int location) {
		int advanceTo = location;

		if (player.getPosition() > advanceTo)
			player.addBalance(200); // For passing GO

		player.setPosition(advanceTo);
	}

	/**
	 * Helper method for pickup(Player). It advances a player to the nearest
	 * railroad/utility
	 * 
	 * @param player The player to moved
	 * @param place  'u' = nearest utility, 'r' = nearest railroad
	 */
	private static void advancePlayerTo(Player player, char place) {
		int playerPos = player.getPosition();

		if (place == 'r') {
			while (!(playerPos % 10 != 0 && playerPos % 5 == 0)) {
				playerPos++;
				if (playerPos > 39)
					playerPos = 0;
			}
			player.setPosition(playerPos);
		} else if (place == 'u') {
			while (!(playerPos == 12 || playerPos == 28)) {
				playerPos++;
				if (playerPos > 39)
					playerPos = 0;
			}
			player.setPosition(playerPos);
		} else {
			logger.error("Cannot advance to nearest: \"" + place + "\"");
		}
	}

	/**
	 * Counts the total number of hotels a specified player has
	 * 
	 * @param player The player to evaluate
	 * @return The number of hotels the player has
	 */
	private static int getPlayerTotalHotels(Player player) {
		int total = 0;
		for (OwnableTile asset : player.getAssets())
			if (asset.getType() == Tile.TileType.PROPERTY && ((PropertyTile) asset).hasHotel())
				total++;
		return total;
	}

	/**
	 * Count the total number of houses a specified player has
	 * 
	 * @param player The player to evaluate
	 * @return The number of houses the player has
	 */
	private static int getPlayerTotalHouses(Player player) {
		int total = 0;
		for (OwnableTile asset : player.getAssets())
			if (asset.getType() == Tile.TileType.PROPERTY && !((PropertyTile) asset).hasHotel())
				total += player.getNumberOfHouses();
		return total;
	}

	/**
	 * If the player must engage in a transaction (paying/getting money), do it
	 * 
	 * @param player The player in respect to the transaction
	 * @param args   The call string
	 */
	private static void calledTransaction(Player player, String[] args) {
		if (args[0].equals("earn")) {
			if (args[1].equals("from-all")) {
				ArrayList<Player> players = (ArrayList<Player>) (player.getGame().getPlayers());

				int amount = Integer.parseInt(args[2]);
				for (int i = 0; i < players.size(); i++) {
					players.get(i).deductBalance(amount);
				}

				player.addBalance(amount * players.size());
			} else {
				player.addBalance(Integer.parseInt(args[1]));
			}
		} else if (args[0].equals("pay")) {
			if (args[1].equals("all")) { // Money is distributed to each player
				// The logic in this block works out perfectly
				// 1.
				int amount = Integer.parseInt(args[2]);
				ArrayList<Player> otherPlayers = (ArrayList<Player>) (player.getGame().getPlayers());
				player.deductBalance(amount * otherPlayers.size());
				for (Player otherPlayer : otherPlayers) {
					otherPlayer.addBalance(amount);
				}
			} else if (args[1].equals("buildings")) { // Money for houses and
				// hotels; also goes to
				// free parking.
				int houseAmount = Integer.parseInt(args[2]) * getPlayerTotalHouses(player);
				int hotelAmount = Integer.parseInt(args[3]) * getPlayerTotalHotels(player);
				int totalFee = houseAmount + hotelAmount;

				// Deduct balance from user
				player.deductBalance(totalFee);
				// Add money to the free parking tile
				((FreeParkingTile) player.getGame().tileAt(20)).addToPool(totalFee);
			} else { // The money goes to free parking
				int amount = Integer.parseInt(args[1]);
				player.deductBalance(amount);
				((FreeParkingTile) player.getGame().tileAt(20)).addToPool(amount);
				// Index 20 is the free parking tile on the board ^
			}
		}
	}

	/**
	 * Call a jackpot (double rent) on a utility or railroad
	 * 
	 * @param player The player who landed on the jackpot
	 * @param args   The call string
	 */
	private static void callJackpot(Player player, String[] args) {
		if (args[0].equals("utility-jackpot")) {
			UtilityTile utility = (UtilityTile) player.getGame().tileAt(player.getPosition());
			if (utility.isOwned() && utility.getOwner() != player) {
				int[] rolls = Dice.roll2();
				int total = rolls[0] + rolls[1];
				int amountPaid = total * 10;
				player.deductBalance(amountPaid);
				utility.getOwner().addBalance(amountPaid);
			}
		} else if (args[0].equals("railroad-jackpot")) {
			RailroadTile railroad = (RailroadTile) player.getGame().tileAt(player.getPosition());
			if (railroad.isOwned() && railroad.getOwner() != player) {
				int amountPaid = railroad.getRent() * 2;
				player.deductBalance(amountPaid);
				railroad.getOwner().addBalance(amountPaid);
			}
		}
	}

	/**
	 * Handles all the calls which can be linked to a community chest/chance card
	 * 
	 * @param player The player who picked up the card
	 * @param call   The call string
	 */
	private static void handleCall(Player player, String call) {
		String[] words = call.split(" ");

		if (words[0].equals("advance")) {
			if (words[1].equals("nearest"))
				advancePlayerTo(player, words[2].charAt(0));
			else
				advancePlayerTo(player, Integer.parseInt(words[1]));
		} else if (words[0].equals("goto")) {
			player.setPosition(Integer.parseInt(words[1]));
		} else if (words[0].equals("earn") || words[0].equals("pay")) {
			calledTransaction(player, words);
		} else if (words[0].equals("get-out-of-jail")) {
			player.addGetOutOfJailCard();
		} else if (words[0].equals("go-to-jail")) {
			player.setPosition(10);
			player.goToJail();
		} else if (words[0].indexOf("-jackpot") >= 0) {
			callJackpot(player, words);
		} else if (words[0].equals("move")) {
			int moveBy = Integer.parseInt(words[1]);
			player.setPosition(player.getPosition() + moveBy);
		} else {
			logger.error("An unknown call has been found: " + words[0]);
		}
	}

	/**
	 * Runs Chance/Community Chest actions on the player once a player picks up the
	 * card
	 *
	 * @param player The player which picks up this card
	 */
	void pickup(Player player) {
		String[] calls = call.split(";");

		// Clean up each command
		cleanCommands(calls);

		// Loop through the array of commands
		for (String currCall : calls) {
			handleCall(player, currCall);
		}
	}

	/**
	 * The deck of community chest cards
	 */
	protected static final Card[] communityChestDeck = { new Card("Advance to Go. Collect $200.", "goto 0; earn 200;"),
			new Card("Bank error in your favor. Collect $200.", "earn 200;"), new Card("Doctor fees. Pay $50.", PAY_50),
			new Card("From sale of stock you get $50.", "earn 50;"),
			new Card("Get out of jail free. – This card may be kept until needed or sold/traded.", "get-out-of-jail;"),
			new Card("Go to jail. Go directly to jail. Do not pass Go, Do not collect $200.", "go-to-jail;"),
			new Card("Grand Opera Night. Collect $50 from every player for opening night seats", "earn from-all 50;"),
			new Card("Holiday Fund matures. Collect $100.", "earn 100;"),
			new Card("Income tax refund. Collect $20.", "earn 20;"),
			new Card("It's your birthday. Collect $10 from every player.", "earn from-all 10;"),
			new Card("Life insurance matures. Collect $100", "earn 100;"), new Card("Hospital Fees. Pay $50.", PAY_50),
			new Card("School Fees. Pay $50.", PAY_50), new Card("Receive $25 consultancy fee.", "earn 25;"),
			new Card("You are assessed for street repairs: Pay $40 per house and $115 per hotel you own.",
					"pay buildings 40 115"),
			new Card("You have won second prize in a beauty contest. Collect $10.", "earn 10"),
			new Card("You inherit $100.", "earn 100") };

	/**
	 * The deck of chance cards
	 */
	protected static final Card[] chanceDeck = { new Card("Advance to Go. Collect $200.", "goto 0; earn 200;"),
			new Card("Advance to Illinois Avenue. If you pass Go, collect $200.", "advance 24;"),
			new Card("Advance to St. Charles Place. If you pass Go, collect $200.", "advance 16;"),
			new Card(
					"Advance token to nearest Utility. If unowned, you may buy it from the bank. If owned, throw dice and pay owner 10 times the amount thrown.",
					"advance nearest utility; utility-jackpot;"),
			new Card(
					"Advance token to nearest Railroad and pay owner twice the rent to which he is otherwise entitled. If Railroad is unowned, you may buy it from the Bank.",
					"advance nearest railroad; railroad-jackpot;"),
			new Card("Bank pays you dividend of $50.", "earn 50;"),
			new Card("Get out of Jail Free.", "get-out-of-jail;"), new Card("Go back 3 spaces.", "move -3;"),
			new Card("Go to Jail. Go directly to Jail. Do not pass GO, do not collect $200.", "go-to-jail;"),
			new Card("Make general repairs on all your property: For each house pay $25, For each hotel pay $100.",
					"pay buildings 25 100;"),
			new Card("Pay poor tax of $15.", "pay 15;"), new Card("Take a trip to Reading Railroad.", "advance 5;"),
			new Card("Take a walk on the Boardwalk. Advance token to Boardwalk.", "advance 39;"),
			new Card("You have been elected Chairman of the Board. Pay each player $50.", "pay all 50;"),
			new Card("You building and loan. Collect $150.", "earn 150;"),
			new Card("You have won a crossword competition. Collect $100.", "earn 100") };

	/**
	 * Return a random card from any deck (array) of cards; usually community
	 * chest/chance
	 *
	 * @param deck A deck (array) of Card objects
	 * @return A random card object from the deck provided
	 */
	static Card pickRandomCard(Card[] deck) {
		int index = randomGenerator.nextInt(deck.length);
		return deck[index];
	}

	/**
	 * @return The String representation of the Card object
	 */
	public String toString() {
		return message;
	}
}
