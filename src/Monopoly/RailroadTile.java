package Monopoly;

/**
 * The RailRoad class is an extension of Tile, with a unique algorithm to get rent based on ownership of other railroads
 */
public class RailroadTile extends Tile {
    /**
     * The owner of this railroad
     */
    private Player owner;

    public RailroadTile(String name) {
        super(TileType.RAILROAD, name);
    }

    public int getRent(int railsOwned) {
        return railsOwned == 1 ? 25 : 2 * getRent(railsOwned - 1);
    }

    public int getRent() {
        int railsOwned = 0;

        for (Tile asset : owner.getAssets())
            if (asset.TYPE == TileType.RAILROAD)
                railsOwned++;

        return getRent(railsOwned);
    }

    public void buy(Player player) {
        owner = player;
        player.deductBalance(getPropertyValue());
        player.addAsset(this);
    }

    public void foreclose() {
        owner.addBalance(200);
        owner.removeAsset(owner.getAssets().indexOf(this));
        owner = null;
    }

    public int railroadsInSet() {
        int setCount = 0;
        for (Tile t : getOwner().getAssets())
            if (t.getType() == TileType.RAILROAD)
                setCount++;

        return setCount;
    }

    public boolean isMonopoly() {
        return railroadsInSet() == 4;
    }

    public int getPropertyValue() {
        return 200;
    }

    public Player getOwner() {
        return owner;
    }

    public void transferOwnership(Player newOwner) {
        owner.removeAsset(owner.getAssets().indexOf(this));
        owner = newOwner;
        owner.addAsset(this);
    }

    public boolean isOwned() {
        return owner != null;
    }
}
