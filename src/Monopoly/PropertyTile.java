package Monopoly;

public class PropertyTile extends Tile {
    private int[] rents;
    private int propertyValue;
    private boolean isOwned;
    private Player owner;
    private int houses;
    private int group;

    PropertyTile(String propertyName, int propertyValue, int[] rentAmounts, int group) {
        super(TileType.PROPERTY, propertyName);
        this.propertyValue = propertyValue;
        rents = rentAmounts;
        isOwned = false;
        houses = 0;
        this.group = group;
    }

    boolean isOwned() {
        return isOwned;
    }

    void buy(Player player) {
        isOwned = true;
        owner = player;
        player.deductBalance(getPropertyValue());
        player.addAsset(this);
    }

    @SuppressWarnings("unused")
    public void forclose() {
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
        int setCount = 0;
        boolean isMonopoly;
        for (Tile t : getOwner().getAssets())
            if (t.getType() == Tile.TileType.PROPERTY)
                if (((PropertyTile) t).getGroupNumber() == getGroupNumber())
                    setCount++;

        if (getGroupNumber() == 1 || getGroupNumber() == 8)
            isMonopoly = setCount == 2;
        else
            isMonopoly = setCount == 3;


        if (isMonopoly) {
            if (houses == 0)
                return rents[0] * 2;
            else
                return rents[houses];
        } else {
            return rents[0];
        }
    }

    public String toString() {
        String append = houses <= 4 && houses > 0 ? " (" + houses + " houses)" : null;
        return String.format("%s%s", NAME, append == null ? "" : append);
    }
}
