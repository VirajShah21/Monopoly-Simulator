package Monopoly.LoggerTools;

/**
 * The LandingLog class contains the structure for recording every position of a
 * player
 * 
 * @author Viraj Shah
 */
public class LandingLog {
	/**
	 * The name of the player being logged
	 */
	private String name;

	/**
	 * The position of the tile being logged
	 */
	private int tile;

	/**
	 * The amount of money which was payed on the tile
	 */
	private int rentDue;

	/**
	 * Constructs a LandingLog object
	 *
	 * @param name    The name of the player being looged
	 * @param tile    The position of the tile being logged
	 * @param rentDue The amount of money which was payed in rent as a result of
	 *                landing on the tile
	 */
	public LandingLog(String name, int tile, int rentDue) {
		this.name = name;
		this.tile = tile;
		this.rentDue = rentDue;
	}

	/**
	 * @return The name of the player which was logged
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The position of the tile which was logged
	 */
	public int getTile() {
		return tile;
	}

	/**
	 * @return The logged amount of rent paid
	 */
	public int getRentDue() {
		return rentDue;
	}
}
