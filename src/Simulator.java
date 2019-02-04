import Monopoly.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class Simulator {
    private static final BufferedReader input =
            new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        Logger.printLogsWhenCreated = true;
        MonopolyGame game = new MonopolyGame();
        while (true) {
            System.out.print("How many turns would you like to play: ");
            int turnsToPlay = Integer.parseInt(input.readLine());

            for (int turnNumber = 0; turnNumber < turnsToPlay; turnNumber++) {
                game.nextPlayer();
                game.playTurn();
            }

            System.out.println("\n");
            System.out.println("======================");
            System.out.println("|  Results of Round  |");
            System.out.println("======================");
            System.out.println();
            
            for (Player person : game.getPlayers()) {
                System.out.println(person);
            }
        }
    }
}
