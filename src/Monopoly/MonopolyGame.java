package Monopoly;

/**
 * The MonopolyGame class is responsible for handling all functions of an instance of a Monopoly game.
 */
public class MonopolyGame {
    private Tile[] board;
    private Player[] players;
    private int currentPlayer;

    /**
     * Initializes a new monopoly game with a new board, and four players.
     */
    public MonopolyGame() {
        board = Tile.buildBoard();
        players = new Player[]{
                new Player("North", this),
                new Player("East", this),
                new Player("South", this),
                new Player("West", this)
        };
        currentPlayer = -1;
    }

    /**
     * Chance the current player to the next player which is still in the game
     */
    public void nextPlayer() {
        currentPlayer++;

        if (currentPlayer >= 4)
            currentPlayer = 0;

        try {
            if (players[currentPlayer].isBankrupt())
                nextPlayer();
        } catch (StackOverflowError e) {
            System.out.println("Every player is now bankrupt.");
            System.exit(0);
        }
    }

    /**
     * Calls upon a player to play their turn
     */
    public void playTurn() {
        players[currentPlayer].playTurn();
    }

    /**
     * @return The players in the current game
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Get the game tile (Tile) at the specified index
     *
     * @param index The index of the specified tile
     * @return The tile at the specified index
     */
    public Tile tileAt(int index) {
        return board[index];
    }

    /**
     * Transfer money from one player to another for rent on a colored property
     *
     * @param payer     The player who is paying rent (will lose money)
     * @param titleDeed The property on which rent is due
     */
    public void payRent(Player payer, PropertyTile titleDeed) {
        Logger.log(String.format("%s payed $%d %s for rent on %s",
                payer, titleDeed.getOwner(), titleDeed.getRent(), titleDeed));
        payer.deductBalance(titleDeed.getRent());
        titleDeed.getOwner().addBalance(titleDeed.getRent());
    }

    /**
     * Transfer money from one player to another for rent on a Utility
     *
     * @param payer     The player who is paying rent (will lose money)
     * @param titleDeed The Utility on which rent is due
     * @param roll      The amount rolled in order to have landed on such property
     */
    public void payRent(Player payer, UtilityTile titleDeed, int roll) {
        payer.deductBalance(titleDeed.getRent(roll));
        titleDeed.getOwner().addBalance(titleDeed.getRent(roll));
    }

    /**
     * Transfer money from one player to another for rent on a Railroad
     *
     * @param payer     The player who is paying rent (will lose money)
     * @param titleDeed The Railroad on which rent is due
     */
    public void payRent(Player payer, RailroadTile titleDeed) {
        payer.deductBalance(titleDeed.getRent());
        titleDeed.getOwner().addBalance(titleDeed.getRent());
    }

}
