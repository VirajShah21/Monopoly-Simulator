package Monopoly.Tiles;

/**
 * The FreeParkingTile class is a subclass of Tile. Extensions include adding
 * money to the free parking pool, clearing out the free parking pool, and
 * retrieving the pool amount.
 * 
 * @author Viraj Shah
 */
public class FreeParkingTile extends Tile {
	/**
	 * The amount of money in the Free Parking pool of money
	 */
	private int pool;

	/**
	 * Initializes a new FreeParkingTile object
	 */
	FreeParkingTile() {
		super(TileType.FREE_PARKING, "Free Parking");
	}

	/**
	 * Adds an amount of money to the Free Parking pool.
	 *
	 * @param amount The amount of money to add to the Free Parking pool.
	 */
	public void addToPool(int amount) {
		pool += amount;
	}

	/**
	 * Clear out the amount of money in the Free Parking Pool
	 */
	public void clearPool() {
		pool = 0;
	}

	/**
	 * @return The amount of money in the Free Parking Pool
	 */
	public int getPoolAmount() {
		return pool;
	}
}
