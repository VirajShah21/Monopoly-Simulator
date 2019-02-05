package Monopoly;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * A monopoly Player class which provides features for managing a players balance, smartly evading bankruptcy,
 * managing properties/assets, using get out of jail free cards, tracking their turns in jail, knowing their position
 * on the board.
 */

@SuppressWarnings("unused")
public class Player {
    /**
     * The name of this player
     */
    private String name;

    /**
     * A pointer to MonopolyGame.board which is symbolic for the players position on the game board
     */
    private int position;

    /**
     * The amount of money which belongs to this player
     */
    private int balance;

    /**
     * The number of get out of jail free cards which belong to this player
     */
    private int getOutOfJailCards;

    /**
     * The number of turns a player has spent in jail
     */
    private int turnsInJail;

    /**
     * An ArrayList of all the assets (Tiles) owned by a player
     */
    private ArrayList<Tile> assets;

    /**
     * The game which the player belongs to
     */
    private MonopolyGame game;

    /**
     * True if the player is in jail; false otherwise
     */
    private boolean inJail;

    /**
     * False if the player is able to pull out of debt; true otherwise
     */
    private boolean isBankrupt;

    /**
     * Create a new Player and attach it to a monopoly game.
     *
     * @param name     The name of the new player
     * @param thisGame Game to which the player should be attached to
     */
    public Player(String name, MonopolyGame thisGame) {
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

    /**
     * Get the game which the player belongs to.
     *
     * @return The game which the player is attached to
     */
    public MonopolyGame getGame() {
        return game;
    }

    /**
     * Add a get out of jail free card to the players possession.
     */
    public void addGetOutOfJailCard() {
        getOutOfJailCards++;
    }

    /**
     * Get the number of get out of jail free cards.
     *
     * @return The number of get out of jail free cards.
     */
    public int getNumberOfGetOutOfJailCards() {
        return getOutOfJailCards;
    }

    /**
     * Get the position of this player. (0 &le; position &le; 4)
     *
     * @return The position of the player.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Set this players position on the game board.
     *
     * @param position The position on the game board.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Get the balance of this player.
     *
     * @return The balance of this player.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Send this player to the jail.
     */
    public void goToJail() {
        inJail = true;
        setPosition(10);
    }

    /**
     * Checks if a player is in jail.
     *
     * @return true if the player is in jail; false otherwise.
     */
    public boolean isInJail() {
        return inJail;
    }

    /**
     * Get the assets belonging to this player.
     *
     * @return The ArrayList of assets belonging to this player.
     */
    public ArrayList<Tile> getAssets() {
        return assets;
    }

    /**
     * Add an asset to the player's list of assets
     *
     * @param asset A Tile which acts as a title deed.
     */
    public void addAsset(Tile asset) {
        assets.add(asset);
    }

    /**
     * Remove an asset at a specified index
     *
     * @param index Index in the asset list to remove an asset from.
     */
    public void removeAsset(int index) {
        assets.remove(index);
    }

    /**
     * Play the player's turn; this method takes everything into account when playing a turn.
     */
    public void playTurn() {
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
        }

        Logger.log(String.format("%s moved to %s", this, game.tileAt(position)));

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

        for (Player p : game.getPlayers()) {
            TradeBroker.createOffer(this, p);
        }

        if (roll[0] == roll[1]) {
            Logger.log(String.format("%s rolled doubles (%d). %s will roll again.", this, roll[0], this));
            playTurn();
        }
    }

    /**
     * Find the least desired asset in an asset list. Used when selling properties.
     *
     * @return The least desired asset belonging to the player.
     */
    @Nullable
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

    /**
     * Dedcut balance from user. If tthey do not have enough money, they will sell get out of jail free cards and properties.
     *
     * @param amount The amount to be deducted from the player's balance
     */
    public void deductBalance(int amount) {
        balance -= amount;

        while (balance < 0 && getOutOfJailCards == 0) {
            getOutOfJailCards--;
            addBalance(50);
        }

        TradeBroker.sortAssetsByWorth(this);
        while (balance < 0 && assets.size() == 0) {
            try {
                Tile getRid = assets.get(0);
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
            } catch (NullPointerException e) {
                isBankrupt = true;
            }
        }
    }

    /**
     * Add a balance to the players holdings.
     *
     * @param amount The amount to be added to the player's balance
     */
    public void addBalance(int amount) {
        balance += amount;
    }

    /**
     * Pays money from one player to another.
     *
     * @param other  The player to recieve money from this player.
     * @param amount The amount of money to transfer.
     */
    public void payTo(Player other, int amount) {
        deductBalance(amount);
        other.addBalance(amount);
        Logger.log(String.format("%s paid %s $%d", this, other, amount));
    }

    /**
     * Checks if the player is bankrupt.
     *
     * @return true if the player is bankrupt; false otherwise.
     */
    public boolean isBankrupt() {
        return isBankrupt;
    }

    public int getPersonalEvaluation() {
        int eval = 0;
        for (Tile t : assets)
            eval += TradeBroker.getAssetWorth(t);

        eval += getOutOfJailCards * 50 + balance;

        return eval;
    }

    /**
     * Returns a string-safe version of this Player object.
     *
     * @return String: "Name ($balance)" <br>&nbsp;&nbsp;&nbsp;&nbsp; Ex: North ($1500)
     */
    public String toString() {
        return String.format("%s ($%d)", name, balance);
    }
}
