package Monopoly;

/**
 * The PropertyTile is an subclass of Tile. It contains fields associated more specifically with those of colored
 * properties on a Monopoly game board.
 */
public class PropertyTile extends Tile {
    /**
     * The rent depending on number of houses.
     * rent[0] = Rent with 0 houses,
     * rent[1] = Rent with 1 house ...
     * rent[4] = Rent with 4 houses
     * rent[5] = Rent with 1 hotel
     */
    private int[] rents;

    /**
     * The price of the property
     */
    private int propertyValue;

    /**
     * The Player object which represents who owns the property
     */
    private Player owner;

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
        super(TileType.PROPERTY, propertyName);
        this.propertyValue = propertyValue;
        rents = rentAmounts;
        houses = 0;
        this.group = group;
    }

    /**
     * @return True if the property is owned; false otherwise
     */
    boolean isOwned() {
        return owner != null;
    }

    /**
     * Allows a player to purchase a property
     *
     * @param player The Player object which is purchasing the property
     */
    void buy(Player player) {
        if (!isOwned()) {
            owner = player;
            player.deductBalance(getPropertyValue());
            player.addAsset(this);
        }
    }

    /**
     * Forecloses a property. Gives the player their money bank, strips Player object of ownership,
     * and gives the property back to the bank.
     */
    @SuppressWarnings("unused")
    public void foreclose() {
        owner.addBalance(propertyValue);
        owner.removeAsset(owner.getAssets().indexOf(this));
        owner = null;
    }

    /**
     * @return The number of houses belonging to this property
     */
    int getHouses() {
        if (houses > 4) {
            return 0;
        } else {
            return houses;
        }
    }

    /**
     * @return True if a hotel belongs to the property; false otherwise
     */
    boolean hasHotel() {
        return houses == 5;
    }

    /**
     * @return The price of a property
     */
    int getPropertyValue() {
        return propertyValue;
    }

    /**
     * @return The Player object of the Player who owns this property
     */
    Player getOwner() {
        return owner;
    }

    /**
     * @return The color group (as an integer) to which this property belongs to
     */
    int getGroupNumber() {
        return group;
    }

    /**
     * @return Get the rent amount which is due when someone lands on the property;
     * this depends on the number of houses/hotels, and whether the property is a monopoly
     */
    int getRent() {
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
     * @return The name of the property with the number of houses or hotels;
     * if no buildings are on the property then just the property name will show up
     */
    public String toString() {
        String append = houses <= 4 && houses > 0 ? " (" + houses + " houses)" : (hasHotel() ? " (w/ Hotel)" : null);
        return String.format("%s%s", NAME, append == null ? "" : append);
    }
}
