package Monopoly;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Player {
    private String name;
    private int position, balance;
    private ArrayList<Tile> assets;
    private MonopolyGame game;
    private int getOutOfJailCards;
    private boolean inJail;
    private int turnsInJail;

    Player(String name, MonopolyGame thisGame) {
        this.name = name;
        position = 0;
        balance = 1500;
        assets = new ArrayList<>();
        game = thisGame;
        getOutOfJailCards = 0;
        inJail = false;
        turnsInJail = 0;
    }

    MonopolyGame getGame() {
        return game;
    }

    void addGetOutOfJailCard() {
        getOutOfJailCards++;
    }

    public int getNumberOfGetOutOfJailCards() {
        return getOutOfJailCards;
    }

    int getPosition() {
        return position;
    }

    public int getBalance() {
        return balance;
    }

    void goToJail() {
        inJail = true;
        setPosition(10);
    }

    public boolean isInJail() {
        return inJail;
    }

    ArrayList<Tile> getAssets() {
        return assets;
    }

    void addAsset(Tile asset) {
        assets.add(asset);
    }

    public void removeAsset(int index) {
        assets.remove(index);
    }

    void playTurn() {
        int[] roll = Dice.roll2();
        int moveAmount = roll[0] + roll[1];


        Logger.log(String.format("%s rolled a %d and %d. Total: %d", name, roll[0], roll[1], moveAmount));

        if (inJail) {
            turnsInJail++;

            if (roll[0] == roll[1]) {
                inJail = false;
                turnsInJail = 0;
                Logger.log(String.format("%s rolled double %d's. Player will move %d spaces from jail",
                        name, roll[0], roll[0] * 2));
            } else if (turnsInJail == 4) {
                inJail = false;
                turnsInJail = 0;
                Logger.log(String.format("%s rolled for the fourth time in jail. Moving %d spaces\n",
                        name, roll[0] + roll[1]));
                return;
            } else {
                Logger.log(String.format("%s is still stuck in jail.\n", name));
                return;
            }
        }

        position += moveAmount;
        if (position > 39) {
            position -= 40;
            addBalance(200);
            Logger.log(String.format("%s moved to %s", name, game.tileAt(position)));
        }

        Tile currTile = game.tileAt(position);
        if (currTile.TYPE == Tile.TileType.PROPERTY) {
            if (!((PropertyTile) currTile).isOwned()) {
                ((PropertyTile) currTile).buy(this);
            } else if (((PropertyTile) currTile).getOwner() != this) {
                game.payRent(this, (PropertyTile) currTile);
            }
        } else if (currTile.TYPE == Tile.TileType.UTILITY) {
            if (!((UtilityTile) currTile).isOwned()) {
                ((UtilityTile) currTile).buy(this);
            } else {
                game.payRent(this, (UtilityTile) currTile, moveAmount);
            }
        } else if (currTile.TYPE == Tile.TileType.RAILROAD) {
            if (!((RailroadTile) currTile).isOwned()) {
                ((RailroadTile) currTile).buy(this);
            } else {
                game.payRent(this, (RailroadTile) currTile);
            }
        } else if (currTile.TYPE == Tile.TileType.CHANCE) {
            Card chanceCard = Card.pickRandomCard(Card.chanceDeck);
            chanceCard.pickup(this);
        } else if (currTile.TYPE == Tile.TileType.COMMUNITY_CHEST) {
            Card ccCard = Card.pickRandomCard(Card.communityChestDeck);
            ccCard.pickup(this);
        } else if (currTile.TYPE == Tile.TileType.GO_TO_JAIL) {
            setPosition(10);
            goToJail();
        } else if (currTile.TYPE == Tile.TileType.FREE_PARKING) {
            int poolSize = ((FreeParkingTile) currTile).getPoolAmount();
            this.addBalance(poolSize);
            ((FreeParkingTile) currTile).clearPool();
        } else if (currTile.TYPE == Tile.TileType.TAX) {
            if (position == 4) {
                deductBalance(200);
                ((FreeParkingTile) game.tileAt(20)).addToPool(200);
            } else if (position == 38) {
                deductBalance(100);
                ((FreeParkingTile) game.tileAt(20)).addToPool(100);
            }
        }
    }

    void deductBalance(int amount) {
        balance -= amount;
    }

    void addBalance(int amount) {
        balance += amount;
    }

    void setPosition(int position) {
        this.position = position;
    }

    public void payTo(Player other, int amount) {
        deductBalance(amount);
        other.addBalance(amount);
        Logger.log(String.format("%s paid %s $%d", this, other, amount));
    }

    public String toString() {
        return String.format("%s ($%d)", name, balance);
    }
}
