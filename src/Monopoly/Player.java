package Monopoly;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Player {
    private String name;
    private int position, balance, getOutOfJailCards, turnsInJail;
    private ArrayList<Tile> assets;
    private MonopolyGame game;
    private boolean inJail, isBankrupt;

    Player(String name, MonopolyGame thisGame) {
        this.name = name;
        position = 0;
        balance = 1500;
        assets = new ArrayList<>();
        game = thisGame;
        getOutOfJailCards = 0;
        inJail = false;
        turnsInJail = 0;
        isBankrupt = false;
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

    void setPosition(int position) {
        this.position = position;
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


        Logger.log(String.format("%s rolled a %d and %d. Total: %d", this, roll[0], roll[1], moveAmount));

        if (inJail) {
            turnsInJail++;

            if (roll[0] == roll[1]) {
                inJail = false;
                turnsInJail = 0;
                Logger.log(String.format("%s rolled double %d's. Player will move %d spaces from jail",
                        this, roll[0], roll[0] * 2));
            } else if (turnsInJail == 4) {
                inJail = false;
                turnsInJail = 0;
                Logger.log(String.format("%s rolled for the fourth time in jail. Moving %d spaces\n",
                        this, roll[0] + roll[1]));
                return;
            } else {
                Logger.log(String.format("%s is still stuck in jail.\n", this));
                return;
            }
        }

        position += moveAmount;
        if (position > 39) {
            position -= 40;
            addBalance(200);
            Logger.log(String.format("%s moved to %s", this, game.tileAt(position)));
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

    private Tile leastDesiredAsset() {
        int[] rankings = new int[assets.size()];
        int lowestIndex;

        for (int i = 0; i < rankings.length; i++) {
            Tile asset = assets.get(i);

            if (asset.TYPE == Tile.TileType.UTILITY) {
                rankings[i] = 7;
                if (((UtilityTile) asset).isMonopoly())
                    rankings[i] -= 2;

            } else if (asset.TYPE == Tile.TileType.PROPERTY) {
                rankings[i] = 5;
                if (((PropertyTile) asset).isMonopoly()) {
                    rankings[i] -= 2;

                    if (((PropertyTile) asset).hasHotel())
                        rankings[i] -= 3;
                    else if (((PropertyTile) asset).getHouses() > 2)
                        rankings[i] -= 2;
                    else if (((PropertyTile) asset).getHouses() > 0) {
                        rankings[i] -= 1;
                    }
                }

            } else if (asset.TYPE == Tile.TileType.RAILROAD) {
                rankings[i] = 5;
                rankings[i] -= ((RailroadTile) asset).railroadsInSet();
            }
        }


        lowestIndex = 0;
        for (int i = 0; i < rankings.length; i++)
            if (rankings[i] < lowestIndex)
                lowestIndex = i;

        try {
            return assets.get(lowestIndex);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    void deductBalance(int amount) {
        System.out.println(amount);
        balance -= amount;

        while (balance < 0 || getOutOfJailCards == 0) {
            getOutOfJailCards--;
            addBalance(50);
        }

        while (balance < 0 || assets.size() == 0) {
            Tile getRid = leastDesiredAsset();

            if (getRid == null) {
                balance = -1;
                isBankrupt = true;
                Logger.log(String.format("%s is bankrupt!", this));
                return;
            }

            if (getRid.TYPE == Tile.TileType.PROPERTY) {
                PropertyTile p = (PropertyTile) getRid;
                p.foreclose();
            } else if (getRid.TYPE == Tile.TileType.UTILITY) {
                UtilityTile u = (UtilityTile) getRid;
                u.foreclose();
            } else if (getRid.TYPE == Tile.TileType.RAILROAD) {
                RailroadTile r = (RailroadTile) getRid;
                r.foreclose();
            }
        }
    }

    void addBalance(int amount) {
        balance += amount;
    }

    public void payTo(Player other, int amount) {
        deductBalance(amount);
        other.addBalance(amount);
        Logger.log(String.format("%s paid %s $%d", this, other, amount));
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public String toString() {
        return String.format("%s ($%d)", name, balance);
    }
}
