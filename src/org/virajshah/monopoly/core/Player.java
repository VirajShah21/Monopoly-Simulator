package org.virajshah.monopoly.core;

import java.util.ArrayList;
import org.virajshah.monopoly.tiles.FreeParkingTile;
import org.virajshah.monopoly.tiles.OwnableTile;
import org.virajshah.monopoly.tiles.PropertyTile;
import org.virajshah.monopoly.tiles.RailroadTile;
import org.virajshah.monopoly.tiles.Tile;
import org.virajshah.monopoly.tiles.UtilityTile;
import org.virajshah.monopoly.tiles.Tile.TileType;
import org.virajshah.monopoly.logs.Logger;

/**
 * A monopoly Player class which provides features for managing a players
 * balance, smartly evading bankruptcy, managing properties/assets, using get
 * out of jail free cards, tracking their turns in jail, knowing their position
 * on the board.
 * 
 * @author Viraj Shah
 */

public class Player {
	/**
	 * The name of this player
	 */
	private String name;

	/**
	 * A pointer to MonopolyGame.board which is symbolic for the players position on
	 * the game board
	 */
	private int position;

	/**
	 * The amount of money which belongs to this player
	 */
	private int balance;

	/**
	 * The number of get out of jail free cards which belong to this player
	 */
	private int getOutOfJailCards;

	/**
	 * The number of turns a player has spent in jail
	 */
	private int turnsInJail;

	/**
	 * An ArrayList of all the assets (Tiles) owned by a player
	 */
	private ArrayList<OwnableTile> assets;

	/**
	 * The game which the player belongs to
	 */
	private MonopolyGame game;

	/**
	 * True if the player is in jail; false otherwise
	 */
	private boolean inJail;

	/**
	 * The amount of money unable to deduct from player's balance
	 */
	private int unpaidBalances;

	/**
	 * The logger object associated with the current game
	 */
	private Logger logger;

	/**
	 * Create a new Player and attach it to a monopoly game.
	 *
	 * @param name     The name of the new player
	 * @param thisGame Game to which the player should be attached to
	 */
	public Player(String name, MonopolyGame thisGame) {
		this.name = name;
		position = 0;
		balance = 1500;
		assets = new ArrayList<>();
		game = thisGame;
		getOutOfJailCards = 0;
		inJail = false;
		turnsInJail = 0;
		unpaidBalances = 0;
		logger = thisGame.getLogger();
	}

	/**
	 * Get the game which the player belongs to.
	 *
	 * @return The game which the player is attached to
	 */
	public MonopolyGame getGame() {
		return game;
	}

	/**
	 * Add a get out of jail free card to the players possession.
	 */
	public void addGetOutOfJailCard() {
		getOutOfJailCards++;
	}

	/**
	 * Get the number of get out of jail free cards.
	 *
	 * @return The number of get out of jail free cards.
	 */
	public int getNumberOfGetOutOfJailCards() {
		return getOutOfJailCards;
	}

	/**
	 * Get the position of this player. (0 &le; position &le; 4)
	 *
	 * @return The position of the player.
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Set this players position on the game board.
	 *
	 * @param position The position on the game board.
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Get the balance of this player.
	 *
	 * @return The balance of this player.
	 */
	public int getBalance() {
		return balance;
	}

	/**
	 * Send this player to the jail.
	 */
	public void goToJail() {
		inJail = true;
		setPosition(10);
	}

	/**
	 * Checks if a player is in jail.
	 *
	 * @return true if the player is in jail; false otherwise.
	 */
	public boolean isInJail() {
		return inJail;
	}

	/**
	 * Get the assets belonging to this player.
	 *
	 * @return The ArrayList of assets belonging to this player.
	 */
	public ArrayList<OwnableTile> getAssets() {
		return assets != null ? assets : new ArrayList<OwnableTile>();
	}

	/**
	 * Add an asset to the player's list of assets
	 *
	 * @param asset A Tile which acts as a title deed.
	 */
	public void addAsset(OwnableTile asset) {
		if (assets != null)
			assets.add(asset);
	}

	/**
	 * Removes an asset at a specified index
	 *
	 * @param index Index in the asset list to remove an asset from.
	 */
	public void removeAsset(int index) {
		if (assets != null && index >= 0)
			assets.remove(index);
	}

	/**
	 * Get all "colored properties" from the player's list of assets
	 * 
	 * @return An ArrayList of PropertyTiles belonging to a player
	 */
	public ArrayList<PropertyTile> getProperties() {
		ArrayList<PropertyTile> list = new ArrayList<>();

		for (OwnableTile asset : assets) {
			if (asset.getType() == TileType.PROPERTY) {
				list.add((PropertyTile) asset);
			}
		}

		return list;
	}

	/**
	 * Get all railroads from the player's list of assets
	 * 
	 * @return An ArrayList of RailroadTiles belonging to a player
	 */
	public ArrayList<RailroadTile> getRailroads() {
		ArrayList<RailroadTile> list = new ArrayList<>();

		for (OwnableTile asset : assets) {
			if (asset.getType() == Tile.TileType.RAILROAD) {
				list.add((RailroadTile) asset);
			}
		}

		return list;
	}

	/**
	 * Get all utilities from the player's list of assets
	 * 
	 * @return An ArrayList of UtilityTiles belonging to a player
	 */
	public ArrayList<UtilityTile> getUtilities() {
		ArrayList<UtilityTile> list = new ArrayList<>();

		for (OwnableTile asset : assets) {
			if (asset.getType() == TileType.UTILITY) {
				list.add((UtilityTile) asset);
			}
		}

		return list;
	}

	/**
	 * Play the player's turn; this method takes everything into account when
	 * playing a turn.
	 */
	public void playTurn() {
		if (isBankrupt())
			return;

		logger.info(String.format("Beginning %s's turn", name));

		TradeBroker broker = new TradeBroker(this);
		int[] roll = Dice.roll2();
		int moveAmount = roll[0] + roll[1];

		logger.info(String.format("%s rolled a %d and a %d. Moving %d spaces.", name, roll[0], roll[1], moveAmount));

		if (inJail) {
			turnsInJail++;
			logger.info(String.format("%s is in jail. Time spent: %d turns", name, turnsInJail + 1));

			if (roll[0] == roll[1]) {
				inJail = false;
				turnsInJail = 0;
				logger.info("\t Rolled doubles... breaking out of jail.");
			} else if (turnsInJail == 4) {
				inJail = false;
				turnsInJail = 0;
				logger.info("\t Sentence served.");
			} else {
				logger.info("\t End of turn.");
				return;
			}
		}

		position += moveAmount;
		
		if (position > 39) {
			position -= 40;
			addBalance(200);
			logger.info(String.format("%s passed Go. Collecting $200", name));
		}
		
		

		Tile currTile = game.tileAt(position);
		logger.info(String.format("%s moved to %s.", name, currTile.getName()));
		if (currTile.getType() == TileType.PROPERTY || currTile.getType() == TileType.RAILROAD
				|| currTile.getType() == TileType.UTILITY) {
			OwnableTile tile = (OwnableTile) currTile;

			if (!tile.isOwned()) {
				tile.buy(this);
				logger.info(String.format("%s is purchasing %s", name, currTile.getName()));
			} else if (tile.getOwner() != this) {
				game.payRent(this, tile, moveAmount);
				logger.info(String.format("%s is paying rent to %s on %s.", name, tile.getOwner(), tile.getName()));
			}
		} else if (currTile.getType() == TileType.CHANCE) {
			Card chanceCard = Card.pickRandomCard(Card.chanceDeck);
			chanceCard.pickup(this);

			if (currTile.getType() == TileType.PROPERTY || currTile.getType() == TileType.RAILROAD
					|| currTile.getType() == TileType.UTILITY) {
				OwnableTile tile = (OwnableTile) currTile;
				if (!tile.isOwned()) {
					tile.buy(this);
					logger.info(String.format("%s is purchasing %s", name, currTile.getName()));
				} else if (tile.getOwner() != this) {
					game.payRent(this, tile, moveAmount);
					logger.info(String.format("%s is paying rent to %s on %s.", name, tile.getOwner(), tile.getName()));
				}
			}
		} else if (currTile.getType() == TileType.COMMUNITY_CHEST) {
			Card ccCard = Card.pickRandomCard(Card.communityChestDeck);
			ccCard.pickup(this);

			if (currTile.getType() == TileType.PROPERTY || currTile.getType() == TileType.RAILROAD
					|| currTile.getType() == TileType.UTILITY) {
				OwnableTile tile = (OwnableTile) currTile;
				if (!tile.isOwned()) {
					tile.buy(this);
					logger.info(String.format("%s is purchasing %s", name, currTile.getName()));
				} else if (tile.getOwner() != this) {
					game.payRent(this, tile, moveAmount);
					logger.info(String.format("%s is paying rent to %s on %s.", name, tile.getOwner(), tile.getName()));
				}
			}
		} else if (currTile.getType() == TileType.GO_TO_JAIL) {
			setPosition(10);
			goToJail();
			logger.info(String.format("%s got sent to jail.", name));
		} else if (currTile.getType() == TileType.FREE_PARKING) {
			int poolSize = ((FreeParkingTile) currTile).getPoolAmount();
			this.addBalance(poolSize);
			((FreeParkingTile) currTile).clearPool();
			logger.info(String.format("%s landed on free parking.", name));
		} else if (currTile.getType() == TileType.TAX) {
			if (position == 4) {
				deductBalance(200);
				((FreeParkingTile) game.tileAt(20)).addToPool(200);
			} else if (position == 38) {
				deductBalance(100);
				((FreeParkingTile) game.tileAt(20)).addToPool(100);
			}
		}

		if (!isBankrupt())
			for (Player p : game.getPlayers())
				while (broker.buildBestTradeOffer(p))
					;

		broker.sortAssetsByWorth();

		for (OwnableTile asset : assets) {
			if (asset.getType() == TileType.PROPERTY) {
				PropertyTile property = (PropertyTile) asset;

				while (property.getHousePrice() < 0.25 * balance && !property.hasHotel() && property.allowedToBuild()) {
					property.buyHouse();
					logger.info(String.format("%s bought a house on %s", name, property.getName()));
				}
			}
		}

		if (roll[0] == roll[1]) {
			logger.info(String.format("Since %s rolled double. They are going again", name));
			playTurn();
		}

		// If player can, un-mortgage properties, then do all this:
		broker.sortAssetsByWorth();

		for (int i = assets.size() - 1; i >= 0; i--) {
			// If un-mortgage amount is less than a quarter of balance
			if ((assets.get(i).getPropertyValue() / 2) * 1.1 < 0.25 * balance) {
				assets.get(i).unmortgage();
				logger.info(String.format("%s unmortgaged %s", name, assets.get(i).getName()));
			}
		}

		if (balance == -1) {
			game.getPlayers().remove(this);
		}
	}

	/**
	 * @return Get the total number of houses belonging to the player
	 */
	public int getNumberOfHouses() {
		int total = 0;

		for (OwnableTile a : assets) {
			if (a.getType() == TileType.PROPERTY) {
				total += ((PropertyTile) a).getHouses();
			}
		}

		return total;
	}

	/**
	 * Sell assets until the player's balance is at least the amount specified.
	 * Liquidation occurs in the order of:
	 * <ol>
	 * <li>1. Non-properties and non-monopoly sets (least valuable first)</li>
	 * <li>2. Non-monopoly sets (least valuable first)</li>
	 * <li>3. All properties (least valuable first)</li>
	 * </ol>
	 * 
	 * @param amount The expected balance after liquidation occurs
	 */
	private void liquidate(int amount) {
		TradeBroker broker = new TradeBroker(this);

		broker.sortAssetsByWorth();

		if (amount > balance) {
			for (int i = assets.size() - 1; i >= 0 && amount > balance; i--) {
				if (!assets.get(i).isMonopoly() && !assets.get(i).isMortgaged()
						&& assets.get(i).getType() != TileType.PROPERTY) {
					assets.get(i).mortgage();
				}

				if (i == 0)
					break;
			}
		}

		broker.sortAssetsByWorth();

		if (amount > balance) {
			for (int i = assets.size() - 1; i >= 0 && amount > balance; i--) {
				if (!assets.get(i).isMonopoly() && !assets.get(i).isMortgaged()) {
					assets.get(i).mortgage();
				}

				if (i == 0)
					break;
			}
		}

		broker.sortAssetsByWorth();

		if (amount > balance) {
			for (int i = assets.size() - 1; i >= 0 && amount > balance; i--) {
				if (!assets.get(i).isMortgaged()) {
					assets.get(i).mortgage();
				}

				if (i == 0)
					break;
			}
		}

	}

	/**
	 * Deduct balance from user. If they do not have enough money, then player will
	 * mortgage properties.
	 * 
	 * @param amount The amount to be deducted from the player's balance
	 * @return The amount of money deducted from the player's balance
	 */
	public int deductBalance(int amount) {
		if (amount > balance) {
			liquidate(amount);
		}
		int lastBalance = balance;

		if (amount > balance) {
			balance = -1;
			getGame().getPlayers().remove(this);
			return lastBalance;
		} else {
			balance -= amount;
			return amount;
		}
	}

	/**
	 * Add a balance to the players holdings.
	 *
	 * @param amount The amount to be added to the player's balance
	 * @return The amount of money added to the player's balance
	 */
	public int addBalance(int amount) {
		ArrayList<Player> players = getGame().getPlayers();
		int circulation = 0;
		for (Player p : players)
			circulation += p.getBalance();
		if (20580 - circulation - amount >= 0) {
			balance += amount;
			return amount;
		} else {
			balance += 20580 - circulation;
			return amount;
		}
	}

	/**
	 * Pays money from one player to another.
	 *
	 * @param other  The player to receive money from this player.
	 * @param amount The amount of money to transfer.
	 */
	public void payTo(Player other, int amount) {

		int amountDeducted = deductBalance(amount);

		if (amountDeducted == amount) {
			other.addBalance(amount);
		} else {
			other.addBalance(amountDeducted);
			for (int i = 0; i < assets.size(); i++)
				assets.get(i).transferOwnership(other);
		}

	}

	/**
	 * Checks if the player is bankrupt.
	 *
	 * @return False if the player is able to pull out of debt; true otherwise
	 */
	public boolean isBankrupt() {
		return balance < 0;
	}

	/**
	 * Returns a string-safe version of this Player object.
	 *
	 * @return String: "Name ($balance)" <br>
	 *         &nbsp;&nbsp;&nbsp;&nbsp; Ex: North ($1500)
	 */
	public String toString() {
		return String.format("%s ($%d)", name, balance);
	}
}
