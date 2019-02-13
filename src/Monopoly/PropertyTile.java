package Monopoly;

import Monopoly.LoggerTools.Logger;

/**
 * The PropertyTile is an subclass of Tile. It contains fields associated more specifically with those of colored
 * properties on a Monopoly game board.
 */
public class PropertyTile extends OwnableTile {
    /**
     * The rent depending on number of houses.
     * rent[0] = Rent with 0 houses,
     * rent[1] = Rent with 1 house ...
     * rent[4] = Rent with 4 houses
     * rent[5] = Rent with 1 hotel
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
     * @param rentAmounts   The rent amounts per house. Index 0 = 0 houses, Index 5 = 1 hotel
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
     * @return The number of houses belonging to this property
     */
    public int getHouses() {
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
     * this depends on the number of houses/hotels, and whether the property is a monopoly
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
     * Checks to see if the property owner owns a monopoly in this property's color group
     *
     * @return True if property belongs to a monopoly set; false otherwise
     */
    public boolean isMonopoly() {
        int setCount = 0;
        for (Tile t : getOwner().getAssets())
            if (t.getType() == Tile.TileType.PROPERTY)
                if (((PropertyTile) t).getGroupNumber() == getGroupNumber())
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
     * Purchases a house; automatically deducts balance from purchasing player and increments hosue counter
     */
    public void buyHouse() {
        if (allowedToBuild()) {
            owner.deductBalance(getHousePrice());
            this.houses++;

            if (houses < 5) {
                Logger.log(String.format("%s bought a house on %s for $%d", owner, this, getHousePrice()));
            } else {
                Logger.log(String.format("%s bought a hotel on %s for $%d", owner, this, getHousePrice()));
            }
        }
    }

    /**
     * @return True if a player is allowed to build upon this property; false otherwise.
     */
    public boolean allowedToBuild() {
        if (isMonopoly()) {
            PropertyTile lowestInSet = this;

            for (Tile asset : owner.getAssets()) {
                if (asset.TYPE == TileType.PROPERTY && ((PropertyTile) asset).getGroupNumber() == group) {
                    PropertyTile property = (PropertyTile) asset;

                    if (property.getHouses() < lowestInSet.getHouses())
                        return false;

                }
            }
            return true;
        }
        return false;
    }

    public boolean isMortgagable() {
        return super.isMortgagable() && houses == 0;
    }

    /**
     * @return The name of the property with the number of houses or hotels;
     * if no buildings are on the property then just the property name will show up
     */
    public String toString() {
        String append = houses <= 4 && houses > 0 ? " (" + houses + " houses)" : (hasHotel() ? " (w/ Hotel)" : null);
        return String.format("%s%s", NAME, append == null ? "" : append);
    }
}
