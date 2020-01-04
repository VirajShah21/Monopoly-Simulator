package Monopoly.Tiles;

import java.util.ArrayList;

/**
 * The RailRoad class is an extension of Tile, with a unique algorithm to get rent based on ownership of other railroads
 */
public class RailroadTile extends OwnableTile {
    /**
     * Constructs a RailroadTile object
     *
     * @param name The name of the railroad
     */
    public RailroadTile(String name) {
        super(TileType.RAILROAD, name, 200);
    }

    public void mortgage() {
        owner.addBalance(propertyValue / 2);
        mortgaged = true;
    }

    public ArrayList<RailroadTile> getMonopolySet() {
        if (!isMonopoly()) return null;

        ArrayList<RailroadTile> railroads = new ArrayList<>();

        for (int i = 0; i < owner.getAssets().size(); i++)
            if (owner.getAssets().get(i).getType() == TileType.RAILROAD)
                railroads.add((RailroadTile) owner.getAssets().get(i));

        return railroads;
    }

    /**
     * Recursively finds the amount of rent due based on the number of railroads owned as given input
     *
     * @param railsOwned The number of a railroads owned
     * @return The amount of rent due on a Railroad
     */
    private int getRent(int railsOwned) {
        return railsOwned <= 1 ? 25 * Math.max(0, railsOwned) : 2 * getRent(railsOwned - 1);
    }

    /**
     * Automatically finds the amount of rent due; implicitly accesses the number of railroads owned.
     *
     * @return The amount of rent due on a Railroad
     */
    public int getRent() {
        int railsOwned = 0;

        for (Tile asset : owner.getAssets())
            if (asset.TYPE == TileType.RAILROAD)
                railsOwned++;

        return getRent(railsOwned);
    }

    /**
     * Get the number of railroads belonging to the owner of this railroad
     *
     * @return The number of railroads owned by the owning Player
     */
    public int railroadsInSet() {
        int setCount = 0;
        for (Tile t : getOwner().getAssets())
            if (t.getType() == TileType.RAILROAD)
                setCount++;

        return setCount;
    }

    /**
     * Checks to see if all four railroads are owned
     *
     * @return True if owner owns a monopoly on the railroads; false otherwise
     */
    public boolean isMonopoly() {
        return railroadsInSet() == 4;
    }

    /**
     * Returns the price of a railroad (always 200)
     *
     * @return 200 (never changes) – The price of a railroad
     */
    public int getPropertyValue() {
        return 200;
    }
}
