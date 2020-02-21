package org.virajshah.monopoly;
import org.virajshah.monopoly.core.MonopolyGame;

/**
 * A simulator to simulate one game of monopoly with logs
 * 
 * @author Viraj Shah
 *
 */
public class GameSimulator {
	public static void main(String[] args) {

		MonopolyGame game = new MonopolyGame();

		while (game.isRunning()) {
			game.nextPlayer();
			game.playTurn();
		}
	}
}
