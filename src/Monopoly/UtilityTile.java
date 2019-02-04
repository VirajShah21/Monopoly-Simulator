package Monopoly;

public class UtilityTile extends Tile {
    int propertyValue;
    private Player owner;

    UtilityTile(String name) {
        super(TileType.UTILITY, name);
        propertyValue = 150;
    }

    public boolean isMonopoly() {
        int count = 0;
        for (Tile asset : owner.getAssets())
            if (asset.TYPE == TileType.UTILITY)
                count++;
        return count == 2;
    }

    public int getRent(int diceRoll) {
        return isMonopoly() ? diceRoll * 10 : diceRoll * 4;
    }

    public boolean isOwned() {
        return owner != null;
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
        return 150;
    }

    public Player getOwner() {
        return owner;
    }
}
