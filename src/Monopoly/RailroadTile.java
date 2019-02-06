package Monopoly;

/**
 * The RailRoad class is an extension of Tile, with a unique algorithm to get rent based on ownership of other railroads
 */
public class RailroadTile extends Tile {
    /**
     * The owner of this railroad
     */
    private Player owner;

    /**
     * Constructs a RailroadTile object
     *
     * @param name The name of the railroad
     */
    public RailroadTile(String name) {
        super(TileType.RAILROAD, name);
    }

    /**
     * Recursively finds the amount of rent due based on the number of railroads owned as given input
     *
     * @param railsOwned The number of a railroads owned
     * @return The amount of rent due on a Railroad
     */
    public int getRent(int railsOwned) {
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
     * Purchase a railroad from the bank
     *
     * @param player The player whom is purchasing the railroad
     */
    public void buy(Player player) {
        owner = player;
        player.deductBalance(getPropertyValue());
        player.addAsset(this);
    }

    /**
     * Strip owner of ownership from the railroad and give them the property value back
     */
    public void foreclose() {
        owner.addBalance(200);
        owner.removeAsset(owner.getAssets().indexOf(this));
        owner = null;
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

    /**
     * Returns the owner who owns this railroad
     *
     * @return The owner who owns this railroad
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Transfers ownership of the railroad to another player
     *
     * @param newOwner The player receiving ownership of the railroad
     */
    public void transferOwnership(Player newOwner) {
        owner.removeAsset(owner.getAssets().indexOf(this));
        owner = newOwner;
        owner.addAsset(this);
    }

    /**
     * Checks to see if the railroad is owned by anyone
     *
     * @return True if railroad has an owner; false otherwise
     */
    public boolean isOwned() {
        return owner != null;
    }
}
