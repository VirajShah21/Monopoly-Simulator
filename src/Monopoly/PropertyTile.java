package Monopoly;

/**
 * The PropertyTile is an subclass of Tile. It contains property's associated more specifically with those of colored
 * properties on a Monopoly game board.
 */
public class PropertyTile extends Tile {
    private int[] rents;
    private int propertyValue;
    private boolean isOwned;
    private Player owner;
    private int houses;
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
        isOwned = false;
        houses = 0;
        this.group = group;
    }

    /**
     * @return True if the property is owned; false otherwise
     */
    boolean isOwned() {
        return isOwned;
    }

    /**
     * Allows a player to purchase a property
     *
     * @param player The Player object which is purchasing the property
     */
    void buy(Player player) {
        if (!isOwned) {
            isOwned = true;
            owner = player;
            player.deductBalance(getPropertyValue());
            player.addAsset(this);
        }
    }

    /**
     * Forecloses a property. Gives the player their money bank, strips Player object of ownership,
     * and gives the property back to the bank
     */
    @SuppressWarnings("unused")
    public void foreclose() {
        owner.addBalance(propertyValue);

        isOwned = false;
        owner = null;
    }

    int getHouses() {
        if (houses > 4) {
            return 0;
        } else {
            return houses;
        }
    }

    boolean hasHotel() {
        return houses == 5;
    }

    int getPropertyValue() {
        return propertyValue;
    }

    Player getOwner() {
        return owner;
    }

    int getGroupNumber() {
        return group;
    }

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

    public String toString() {
        String append = houses <= 4 && houses > 0 ? " (" + houses + " houses)" : null;
        return String.format("%s%s", NAME, append == null ? "" : append);
    }
}
