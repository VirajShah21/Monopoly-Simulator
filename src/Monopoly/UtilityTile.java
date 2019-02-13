package Monopoly;

/**
 * The UtilityTile class is a subclass of Tile which has specific algorithms for getting rent amounts.
 */
public class UtilityTile extends OwnableTile {
    private int lastDiceRoll;

    /**
     * Construct a new UtilityTile
     *
     * @param name The name of the Utility
     */
    UtilityTile(String name) {
        super(TileType.UTILITY, name, 150);
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
     * @return The amount of rent payment due by landing on this utility
     */
    public int getRent() {
        return isMonopoly() ? lastDiceRoll * 10 : lastDiceRoll * 4;
    }

    /**
     * Assign the value of the last dice roll
     *
     * @param n The sum of two dice rolls
     */
    public void setLastDiceRoll(int n) {
        lastDiceRoll = n;
    }

    /**
     * @return The price of the Utility
     */
    public int getPropertyValue() {
        return 150;
    }
}
