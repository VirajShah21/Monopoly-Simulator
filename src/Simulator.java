import Monopoly.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class Simulator {
    private static final BufferedReader input =
            new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        Prefs.printLogsWhenCreated = true;

        while (true) {
            MonopolyGame game = new MonopolyGame();

            System.out.print("How many turns would you like to play: ");
            int turnsToPlay = Integer.parseInt(input.readLine());

            for (int turnNumber = 0; turnNumber < turnsToPlay; turnNumber++) {
                game.nextPlayer();
                game.playTurn();
            }
        }
    }
}
