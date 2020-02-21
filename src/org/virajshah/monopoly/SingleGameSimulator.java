import org.virajshah.monopoly.MonopolyGame;
import org.virajshah.monopoly.Player;
import org.virajshah.monopoly.logger.Logger;

/**
 * A simulator to simulate one game of monopoly with logs
 * 
 * @author Viraj Shah
 *
 */
public class SingleGameSimulator {
	public static void main(String[] args) {
		Logger.printLogsWhenCreated = true;

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
