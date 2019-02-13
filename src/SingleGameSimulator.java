import Monopoly.LoggerTools.Logger;
import Monopoly.MonopolyGame;
import Monopoly.Player;

public class SingleGameSimulator {
    public static void main(String[] args) {
        Logger.printLogsWhenCreated = true;


        MonopolyGame game = new MonopolyGame();

        while (game.getPlayers().size() > 1) {
            game.nextPlayer();
            game.playTurn();

            System.out.println("\n\n");
            for (Player p : game.getPlayers())
                System.out.println(p.toString() + ": " + p.getAssets().toString());
            System.out.println("\n\n");
        }
    }
}
