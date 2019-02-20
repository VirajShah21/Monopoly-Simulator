import Monopoly.LoggerTools.LandingLog;
import Monopoly.LoggerTools.Logger;
import Monopoly.MonopolyGame;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Simulator to test a tiles profitability
 */
class TileProfitabilitySimulator {

    public static void main(String[] args) {
        Logger.printLogsWhenCreated = true;


        MonopolyGame game = new MonopolyGame();
        System.out.println("Progress: |                    |");
        System.out.print("           ");
        for (int i = 0; i < 1000; i++) {
            while (game.isRunning()) {
                game.nextPlayer();
                game.playTurn();
            }
            if (i % 50 == 0) System.out.print("=");
            game = new MonopolyGame();
            Logger.clearAllMessageLogs();
        }
        System.out.println();

        int landingLogSize = Logger.getLandingLogStream().size();
        ArrayList<LandingLog> landingLogs = Logger.getLandingLogStream();

        int[] tileFrequency = new int[40];
        int[] tileProfits = new int[40];

        for (int i = 0; i < landingLogSize; i++) {
            LandingLog log = landingLogs.get(i);
            tileFrequency[log.getTile()]++;
            tileProfits[log.getTile()] += log.getRentDue();
        }

        System.out.println(Arrays.toString(tileFrequency));

        for (int i = 0; i < tileFrequency.length /* 40 */; i++) {
            System.out.printf("%s\t%d\t%d\n", game.tileAt(i).getName(), tileFrequency[i], tileProfits[i]);
        }
    }
}

