package org.virajshah.monopoly;
import java.util.ArrayList;
import java.util.Arrays;

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
		System.out.println("Progress: |                    |");
		System.out.print("           ");
		for (int i = 0; i < TRIALS; i++) {
			while (game.isRunning()) {
				game.nextPlayer();
				game.playTurn();
			}
			if (i % (TRIALS / 20) == 0)
				System.out.print("=");
			game = new MonopolyGame();
		}
		System.out.println();


		int[] tileFrequency = new int[40];
		int[] tileProfits = new int[40];

		

		System.out.println(Arrays.toString(tileFrequency));

		for (int i = 0; i < tileFrequency.length /* 40 */; i++) {
			System.out.printf("%20s\t%10d\t%10d\n", game.tileAt(i).getName(), tileFrequency[i], tileProfits[i]);
		}
	}
}
