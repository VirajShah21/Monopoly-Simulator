package org.virajshah.monopoly;

import org.virajshah.monopoly.core.MonopolyGame;

/**
 * The Simulator to test a tiles profitability
 * 
 * @author Viraj Shah
 */
class TileProfitabilitySimulator {
	/**
	 * The number of trials to run. (Minimum value must be 20)
	 */
	private static final int TRIALS = 20; // minimum value is 20

	public static void main(String[] args) {
		MonopolyGame game = new MonopolyGame();
		for (int i = 0; i < TRIALS; i++) {
			while (game.isRunning()) {
				game.nextPlayer();
				game.playTurn();
			}
			game = new MonopolyGame();
		}
	}
}
