import Monopoly.LoggerTools.LandingLog;
import Monopoly.LoggerTools.Logger;
import Monopoly.MonopolyGame;

import java.util.ArrayList;

/**
 * The Simulator to test a tiles profitability
 */
class TileProfitabilitySimulator {

    public static void main(String[] args) {
        Logger.printLogsWhenCreated = false;


        MonopolyGame game = new MonopolyGame();
        for (int i = 0; i < 1000; i++) {
            while (game.getPlayers().size() > 1) {
                game.nextPlayer();
                game.playTurn();
            }

            game = new MonopolyGame();
            // At bottom so that initialization occurs during declaration to avoid "game may not be initialized" error
        }

        int landingLogSize = Logger.getLandingLogStream().size();
        ArrayList<LandingLog> landingLogs = Logger.getLandingLogStream();
        int[] tileFrequency = new int[40];
        int[] tileProfits = new int[40];

        for (int i = 0; i < landingLogSize; i++) {
            LandingLog log = landingLogs.get(i);
            tileFrequency[log.getTile()]++;
            tileProfits[log.getTile()] += log.getRentDue();
        }

        for (int i = 0; i < tileFrequency.length /* 40 */; i++) {
            System.out.printf("%s\t%d\t%d\n", game.tileAt(i).getName(), tileFrequency[i], tileProfits[i]);
        }
    }
}

