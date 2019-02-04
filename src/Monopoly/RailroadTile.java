package Monopoly;

public class RailroadTile extends Tile {
    private Player owner;

    RailroadTile(String name) {
        super(TileType.RAILROAD, name);
    }

    int getRent(int railsOwned) {
        return railsOwned == 1 ? 25 : 2 * getRent(railsOwned - 1);
    }

    int getRent() {
        int railsOwned = 0;

        for (Tile asset : owner.getAssets())
            if (asset.TYPE == TileType.RAILROAD)
                railsOwned++;

        return getRent(railsOwned);
    }

    void buy(Player player) {
        owner = player;
        player.deductBalance(getPropertyValue());
        player.addAsset(this);
    }

    public void foreclose() {
        owner = null;
    }

    int getPropertyValue() {
        return 200;
    }

    boolean isOwned() {
        return owner != null;
    }

    Player getOwner() {
        return owner;
    }
}
