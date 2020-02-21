package org.virajshah.monopoly;
import org.virajshah.monopoly.core.MonopolyGame;
import org.virajshah.monopoly.core.Player;

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

			System.out.println("\n\n");
			for (Player p : game.getPlayers())
				System.out.println(p.toString() + ": " + p.getAssets().toString());
			System.out.println("\n\n");
		}

		System.out.println("Final players: " + game.getPlayers());
	}
}
