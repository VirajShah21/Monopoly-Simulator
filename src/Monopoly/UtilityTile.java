package Monopoly;

public class UtilityTile extends Tile {
    private Player owner;

    UtilityTile(String name) {
        super(TileType.UTILITY, name);
    }

    public boolean isMonopoly() {
        int count = 0;
        for (Tile asset : owner.getAssets())
            if (asset.TYPE == TileType.UTILITY)
                count++;
        return count == 2;
    }

    int getRent(int diceRoll) {
        return isMonopoly() ? diceRoll * 10 : diceRoll * 4;
    }

    boolean isOwned() {
        return owner != null;
    }

    void buy(Player player) {
        owner = player;
        player.deductBalance(getPropertyValue());
        player.addAsset(this);
    }

    @SuppressWarnings("unused")
    public void foreclose() {
        owner.addBalance(150);
        owner = null;

    }

    int getPropertyValue() {
        return 150;
    }

    Player getOwner() {
        return owner;
    }
}
