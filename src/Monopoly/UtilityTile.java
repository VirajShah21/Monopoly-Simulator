package Monopoly;

/**
 * The UtilityTile class is a subclass of Tile which has specific algorithms for getting rent amounts.
 */
public class UtilityTile extends Tile {
    /**
     * The owner of an instance of a UtilityTile
     */
    private Player owner;

    /**
     * Construct a new UtilityTile
     *
     * @param name The name of the Utility
     */
    UtilityTile(String name) {
        super(TileType.UTILITY, name);
    }

    /**
     * Checks if both utility's are owned by the same Player
     *
     * @return True if both utility's are owned by the same Player; false otherwise
     */
    public boolean isMonopoly() {
        int count = 0;
        for (Tile asset : owner.getAssets())
            if (asset.TYPE == TileType.UTILITY)
                count++;
        return count == 2;
    }

    /**
     * @param diceRoll The total rolled by the dice
     * @return The amount of rent payment due by landing on this utility with a certain dice roll
     */
    int getRent(int diceRoll) {
        return isMonopoly() ? diceRoll * 10 : diceRoll * 4;
    }

    /**
     * @return True if a player owns this property; false otherwise
     */
    boolean isOwned() {
        return owner != null;
    }

    /**
     * Allows a player to purchase the current utility tile
     *
     * @param player The player who is purchasing the property
     */
    void buy(Player player) {
        owner = player;
        player.deductBalance(getPropertyValue());
        player.addAsset(this);
    }

    /**
     * Foreclose a property by reimbursing the owner and removing the title deed.
     */
    @SuppressWarnings("unused")
    public void foreclose() {
        owner.addBalance(150);
        owner.removeAsset(owner.getAssets().indexOf(this));
        owner = null;

    }

    /**
     * @return The price of the Utility
     */
    int getPropertyValue() {
        return 150;
    }

    /**
     * @return The owner of the Utility
     */
    Player getOwner() {
        return owner;
    }
}
