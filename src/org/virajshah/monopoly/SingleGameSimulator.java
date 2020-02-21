package org.virajshah.monopoly;
import org.virajshah.monopoly.core.MonopolyGame;
import org.virajshah.monopoly.core.Player;

/**
 * A simulator to simulate one game of monopoly with logs
 * 
 * @author Viraj Shah
 *
 */
public class SingleGameSimulator {
	public static void main(String[] args) {

		MonopolyGame game = new MonopolyGame();

		while (game.isRunning()) {
			game.nextPlayer();
			game.playTurn();

			System.out.println("\n\n");
			System.out.println("% Win");
			for (Player p : game.getPlayers())
				System.out.printf("%.4f %s: %s\n", game.chanceOfWinning(p), p, p.getAssets());

			System.out.println("\n\n");
		}

		System.out.println("Final players: " + game.getPlayers());
	}
}
