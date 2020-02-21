package org.virajshah.monopoly.tiles;

import java.util.ArrayList;
import java.util.List;

/**
 * The PropertyTile is an subclass of Tile. It contains fields associated more
 * specifically with those of colored properties on a Monopoly game board.
 * 
 * @author Viraj Shah
 */
public class PropertyTile extends OwnableTile {
	/**
	 * The rent depending on number of houses. rent[0] = Rent with 0 houses, rent[1]
	 * = Rent with 1 house ... rent[4] = Rent with 4 houses rent[5] = Rent with 1
	 * hotel
	 */
	private int[] rents;

	/**
	 * The number of houses on this property (*5 = 1 hotel)
	 */
	private int houses;

	/**
	 * The color group (as a number) to which this property belongs to
	 */
	private int group;

	/**
	 * Constructs a PropertyTile object
	 *
	 * @param propertyName  The name of the property being constructed
	 * @param propertyValue The price of the property
	 * @param rentAmounts   The rent amounts per house. Index 0 = 0 houses, Index 5
	 *                      = 1 hotel
	 * @param group         The color group to which the property belongs to
	 */
	PropertyTile(String propertyName, int propertyValue, int[] rentAmounts, int group) {
		super(TileType.PROPERTY, propertyName, propertyValue);
		this.propertyValue = propertyValue;
		rents = rentAmounts;
		houses = 0;
		this.group = group;
	}

	/**
	 * Sets the number of houses on a property
	 *
	 * @param n The number of houses to be set
	 */
	void setNumberOfHouses(int n) {
		houses = n;
	}

	public int getHousesInMonopolySet() {
		int groupNumber = getGroupNumber();
		int count = 0;
		for (OwnableTile t : owner.getAssets()) {
			if (t.type == TileType.PROPERTY && ((PropertyTile) t).getGroupNumber() == groupNumber) {
				count += ((PropertyTile) t).getNumberOfHouses();
			}
		}
		return count;
	}

	/**
	 * Mortgages a property
	 */
	@Override
	public void mortgage() {
		if (isMonopoly()) {
			ArrayList<PropertyTile> colorSet = (ArrayList<PropertyTile>) getMonopolySet();

			for (int i = 0; i < colorSet.size(); i++) {
				int numHouses = colorSet.get(i).getNumberOfHouses();
				owner.addBalance(numHouses * getHousePrice() / 2);
				colorSet.get(i).setNumberOfHouses(0);
			}
		}

		owner.addBalance(propertyValue / 2);
		mortgaged = true;
	}

	/**
	 * @return The number of houses belonging to this property
	 */
	public int getNumberOfHouses() {
		if (houses > 4) {
			return 0;
		} else {
			return houses;
		}
	}

	/**
	 * @return True if a hotel belongs to the property; false otherwise
	 */
	public boolean hasHotel() {
		return houses == 5;
	}

	/**
	 * @return The price of a property
	 */
	public int getPropertyValue() {
		return propertyValue;
	}

	/**
	 * @return The color group (as an integer) to which this property belongs to
	 */
	public int getGroupNumber() {
		return group;
	}

	/**
	 * @return Get the rent amount which is due when someone lands on the property;
	 *         this depends on the number of houses/hotels, and whether the property
	 *         is a monopoly
	 */
	public int getRent() {
		if (isMonopoly()) {
			if (houses == 0)
				return rents[0] * 2;
			else
				return rents[houses];
		} else {
			return rents[0];
		}
	}

	/**
	 * Checks to see if the property owner owns a monopoly in this property's color
	 * group
	 *
	 * @return True if property belongs to a monopoly set; false otherwise
	 */
	public boolean isMonopoly() {
		int setCount = 0;
		for (OwnableTile t : getOwner().getAssets())
			if (t.getType() == Tile.TileType.PROPERTY && ((PropertyTile) t).getGroupNumber() == getGroupNumber())
				setCount++;

		if (getGroupNumber() == 1 || getGroupNumber() == 8)
			return setCount == 2;
		else
			return setCount == 3;
	}

	/**
	 * Get the price to purchase a house/hotel on a property
	 *
	 * @return Price of a house/hotel
	 */
	public int getHousePrice() {
		if (group == 1 || group == 2)
			return 50;
		else if (group == 3 || group == 4)
			return 100;
		else if (group == 5 || group == 6)
			return 150;
		else
			return 200;
	}

	/**
	 * Purchases a house; automatically deducts balance from purchasing player and
	 * increments house counter
	 * 
	 * @return True if owner is allowed to develop on this property and property is
	 *         successfully bought; false otherwise
	 */
	public boolean buyHouse() {
		if (allowedToBuild()) {
			owner.deductBalance(getHousePrice());
			this.houses++;
			return true;
		}
		return false;
	}

	// & Utility Tile classes
	public List<PropertyTile> getMonopolySet() {
		if (!isMonopoly())
			return new ArrayList<>();

		ArrayList<PropertyTile> colorSet = new ArrayList<>();

		for (int i = 0; i < owner.getAssets().size(); i++) {
			if (owner.getAssets().get(i).getType() == TileType.PROPERTY) {
				PropertyTile currTile = (PropertyTile) owner.getAssets().get(i);
				if (currTile.getGroupNumber() == getGroupNumber())
					colorSet.add(currTile);
			}
		}

		return colorSet;
	}

	/**
	 * Sell a house back to the bank
	 */
	public void sellHouse() {
		if (houses > 0) {
			houses--;
			owner.addBalance(getHousePrice() / 2);
		}
	}

	public void autoSellHouseOnMonopoly() {
		if (isMonopoly()) {
			ArrayList<PropertyTile> colorSet = (ArrayList<PropertyTile>)getMonopolySet();
			PropertyTile highestProperty = this;

			for (int i = 1; i < colorSet.size(); i++) {
				if (colorSet.get(i).getNumberOfHouses() > highestProperty.getNumberOfHouses()) {
					highestProperty = colorSet.get(i);
				}
			}
			highestProperty.sellHouse();
		}
	}

	/**
	 * @return True if a player is allowed to build upon this property; false
	 *         otherwise.
	 */
	public boolean allowedToBuild() {
		if (isMonopoly() && !isMortgaged()) {
			PropertyTile lowestInSet = this;

			for (OwnableTile asset : owner.getAssets()) {
				if (asset.type == TileType.PROPERTY && ((PropertyTile) asset).getGroupNumber() == group) {
					PropertyTile property = (PropertyTile) asset;

					if (property.getNumberOfHouses() < lowestInSet.getNumberOfHouses())
						return false;

				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @return The name of the property with the number of houses or hotels; if no
	 *         buildings are on the property then just the property name will show
	 *         up
	 */
	@Override
	public String toString() {
		String append = houses <= 4 && houses > 0 ? " (" + houses + " houses)" : (hasHotel() ? " (w/ Hotel)" : null);
		return String.format("[%d]%s%s %s", group, name, append == null ? "" : append,
				isMortgaged() ? "(Mortgaged)" : "");
	}
}
