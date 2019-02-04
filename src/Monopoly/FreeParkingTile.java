package Monopoly;

public class FreeParkingTile extends Tile {
    private int pool;

    public FreeParkingTile() {
        super(TileType.FREE_PARKING, "Free Parking");
    }

    public void addToPool(int amount) {
        pool += amount;
    }

    public void clearPool() {
        pool = 0;
    }

    public int getPoolAmount() {
        return pool;
    }
}
