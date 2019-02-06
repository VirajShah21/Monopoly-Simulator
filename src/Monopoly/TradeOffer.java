package Monopoly;

public class TradeOffer {
    /**
     * The Player initializing the offer
     */
    private Player sender;

    /**
     * The receiving player
     */
    private Player receiver;

    /**
     * The sender's tile up for trade
     */
    private Tile senderTile;

    /**
     * The receiving player's tile being requested by the sender
     */
    private Tile receiverTile;

    /**
     * The sender's cash stake in the offer
     */
    private int senderStake;

    /**
     * The receiving player's tile being requested by the sender
     */
    private int receiverStake;

    /**
     * A boolean representing whether the current state of the trade offer is a fair trade
     */
    private boolean isFairTrade;

    /**
     * Constructs a TradeOffer object
     *
     * @param sender        The Player initializing the offer
     * @param senderTile    The sender's tile up for trade
     * @param senderStake   The sender's cash stake in the offer
     * @param receiver      The receiving player
     * @param receiverTile  The receiving player's tile being requested by the sender
     * @param receiverStake The receiving player's cash stake being request by the sender
     */
    public TradeOffer(Player sender, Tile senderTile, int senderStake, Player receiver, Tile receiverTile, int receiverStake) {
        this.sender = sender;
        this.senderTile = senderTile;
        this.senderStake = senderStake;
        this.receiver = receiver;
        this.receiverTile = receiverTile;
        this.receiverStake = receiverStake;
        isFairTrade = false;
    }

    /**
     * Send the offer to the recipient for negotiation
     */
    public void sendOffer() {
        if (getReceiverGain() > 0 && getSenderGain() > 0 && receiverStake > receiver.getBalance() * 0.45) {
            isFairTrade = true;
            return;
        } else if (getReceiverGain() > 0 && receiverStake > receiver.getBalance() * 0.45) {
            receiverStake = (int) (receiver.getBalance() * 0.45);
            negotiate();
        } else {
            if (getReceiverGain() < 0) {
                receiverStake += getReceiverGain() - 1;

                if (receiverStake < 0)
                    receiverStake = 0;
            }

            if (getReceiverGain() < 0) {
                senderStake -= getReceiverGain() - 1;
            }
            negotiate();
        }
    }

    /**
     * Allows the recipient to negotiate prices with the sender
     */
    public void negotiate() {
        if (getSenderGain() > 0 && getReceiverGain() > 0 && senderStake < sender.getBalance() * 0.45) {
            isFairTrade = true;
        } else {
            isFairTrade = false;
        }
    }

    /**
     * Returns the potential value gained by executing a trade for the sender
     *
     * @return The potential value gained by trade execution
     */
    public int getSenderGain() {
        return (TradeBroker.getAssetWorthToOther(receiverTile, sender) + senderStake) -
                (TradeBroker.getAssetWorth(senderTile) + senderStake);
    }

    /**
     * Returns the potential value gained by executing a trade for the recipient
     *
     * @return The potential value gained by trade execution
     */
    public int getReceiverGain() {
        return (TradeBroker.getAssetWorthToOther(senderTile, receiver) + senderStake) -
                (TradeBroker.getAssetWorth(receiverTile) + receiverStake);
    }

    /**
     * Execute the trae between the recipient and the sender
     */
    //noinspection Duplicates
    public void execute() {
        sender.deductBalance(senderStake);
        receiver.addBalance(senderStake);

        sender.addBalance(receiverStake);
        receiver.deductBalance(receiverStake);

        if (senderTile.TYPE == Tile.TileType.PROPERTY) {
            ((PropertyTile) senderTile).transferOwnership(receiver);
        } else if (senderTile.TYPE == Tile.TileType.RAILROAD) {
            ((RailroadTile) senderTile).transferOwnership(receiver);
        } else if (senderTile.TYPE == Tile.TileType.UTILITY) {
            ((UtilityTile) senderTile).transferOwnership(receiver);
        } else {
            System.out.println(senderTile + " is not an ownable asset");
            return;
        }

        if (receiverTile.TYPE == Tile.TileType.PROPERTY) {
            ((PropertyTile) receiverTile).transferOwnership(sender);
        } else if (receiverTile.TYPE == Tile.TileType.RAILROAD) {
            ((RailroadTile) receiverTile).transferOwnership(sender);
        } else if (receiverTile.TYPE == Tile.TileType.UTILITY) {
            ((UtilityTile) receiverTile).transferOwnership(sender);
        } else {
            System.out.println(receiverTile + " is not an ownable asset");
            return;
        }

        Logger.log(String.format("%s and %s executed a trade: %s and $%d for %s and %d",
                sender, receiver, senderTile, senderStake, receiverTile, receiverStake));

    }

    /**
     * @return True if a trade is fair both the sender and receiver; false otherwise
     */
    public boolean isFairTrade() {
        return isFairTrade;
    }
}
