package Monopoly;

public class RailroadTile extends Tile {
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
        owner = null;
    }

    public int getPropertyValue() {
        return 200;
    }

    public boolean isOwned() {
        return owner != null;
    }

    public Player getOwner() {
        return owner;
    }
}
