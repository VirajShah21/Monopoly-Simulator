package org.virajshah.monopoly.tiles;

import java.util.List;

import org.virajshah.monopoly.core.Player;

/**
 * An extension of the Tile class, which has fields and methods for colored
 * sets, railroads, and utilities
 * 
 * @author Viraj Shah
 *
 */
public abstract class OwnableTile extends Tile {

	/**
	 * The Player object which represents who owns the property
	 */
	protected Player owner;

	/**
	 * True if the asset is mortgaged
	 */
	protected boolean mortgaged;

	/**
	 * The price of the property
	 */
	protected int propertyValue;

	/**
	 * Constructs the Tile super.super class, and the Ownable Tile super class
	 *
	 * @param tileType      The type of tile being created
	 * @param tileName      The name of the tile being created
	 * @param propertyValue The price of the property
	 */
	public OwnableTile(TileType tileType, String tileName, int propertyValue) {
		super(tileType, tileName);
		this.propertyValue = propertyValue;
		mortgaged = false;
	}

	/**
	 * @return True if the property is owned; false otherwise
	 */
	public boolean isOwned() {
		return owner != null;
	}

	/**
	 * @return The Player object of the Player who owns this property
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * Forecloses a property. Gives the player their money bank, strips Player
	 * object of ownership, and gives the property back to the bank.
	 */
	@SuppressWarnings("unused")
	public void foreclose() {
		owner.addBalance(getPropertyValue());
		owner.removeAsset(owner.getAssets().indexOf(this));
		owner = null;
	}

	/**
	 * Allows a player to purchase the current utility tile
	 *
	 * @param player The player who is purchasing the property
	 */
	public void buy(Player player) {
		if (!isOwned()) {
			owner = player;
			player.deductBalance(getPropertyValue());
			player.addAsset(this);
		}
	}

	/**
	 * Transfers owner ship of a property tile to another Player object
	 *
	 * @param newOwner The owner who the property should be sent to
	 */
	public void transferOwnership(Player newOwner) {
		int oldIndex = owner.getAssets().indexOf(this);
		owner.removeAsset(oldIndex);
		owner = newOwner;
		owner.addAsset(this);
	}

	/**
	 * Checks if a property is mortgaged
	 *
	 * @return True if the property is mortgaged, false otherwise
	 */
	public boolean isMortgaged() {
		return mortgaged;
	}

	/**
	 * Mortgages the property
	 */
	public void mortgage() {
		if (!mortgaged) {
			mortgaged = true;
			owner.addBalance(propertyValue / 2);
		}
	}

	/**
	 * Unmortgages a property if the owner has enough funds
	 */
	public void unmortgage() {
		if (mortgaged && owner.getBalance() > 1.1 * ((double) propertyValue / 2)) {
			mortgaged = false;
			owner.deductBalance((int) (1.1 * ((double) propertyValue / 2)));
		}
	}

	/**
	 * Get the value of the property
	 *
	 * @return The value of the property
	 */
	public abstract int getPropertyValue();

	/**
	 * Get the rent as a result of landing on the ownable tile
	 *
	 * @return The rent of landing on the tile
	 */
	public abstract int getRent();

	/**
	 * Checks to see if the OwnableTile is a monopoly
	 *
	 * @return True if property belongs to an owned monopoly set; false otherwise
	 */
	public abstract boolean isMonopoly();

	/**
	 * @return A list of assets belonging to the same monopoly set and owner
	 */
	abstract List<? extends OwnableTile> getMonopolySet();
	// Keep the generic wildcard type despite sonar lint (causes issues in
	// PropertyTile

	@Override
	public String toString() {
		if (isMortgaged()) {
			return getName() + " (Mortgaged)";
		}

		return getName();
	}
}
