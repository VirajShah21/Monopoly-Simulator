package Monopoly;

class FreeParkingTile extends Tile {
    private int pool;

    FreeParkingTile() {
        super(TileType.FREE_PARKING, "Free Parking");
    }

    void addToPool(int amount) {
        pool += amount;
    }

    void clearPool() {
        pool = 0;
    }

    int getPoolAmount() {
        return pool;
    }
}
