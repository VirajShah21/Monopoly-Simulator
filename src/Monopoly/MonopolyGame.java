package Monopoly;

public class MonopolyGame {
    private Tile[] board;
    private Player[] players;
    int currentPlayer;

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

    public void nextPlayer() {
        currentPlayer++;

        if (currentPlayer >= 4)
            currentPlayer = 0;
    }

    public void playTurn() {
        players[currentPlayer].playTurn();
    }

    public Player[] getPlayers() {
        return players;
    }

    public Tile tileAt(int index) {
        return board[index];
    }

    public void payRent(Player payer, PropertyTile titleDeed) {
        payer.deductBalance(titleDeed.getRent());
        titleDeed.getOwner().addBalance(titleDeed.getRent());
    }

    public void payRent(Player payer, UtilityTile titleDeed, int roll) {
        payer.deductBalance(titleDeed.getRent(roll));
        titleDeed.getOwner().addBalance(titleDeed.getRent(roll));
    }

    public void payRent(Player payer, RailroadTile titleDeed) {
        payer.deductBalance(titleDeed.getRent());
        titleDeed.getOwner().addBalance(titleDeed.getRent());
    }

}
