package Monopoly;

public class TradeOffer {
    private Player sender;
    private Player receiver;
    private Tile senderTile;
    private Tile receiverTile;
    private int senderStake;
    private int receiverStake;
    private boolean isFairTrade;

    public TradeOffer(Player sender, Tile senderTile, int senderStake, Player receiver, Tile receiverTile, int receiverStake) {
        this.sender = sender;
        this.senderTile = senderTile;
        this.senderStake = senderStake;
        this.receiver = receiver;
        this.receiverTile = receiverTile;
        this.receiverStake = receiverStake;
        isFairTrade = false;
//        System.out.printf("Trade offer created: %s %s %d %s %s %d\n", sender, senderTile, senderStake, receiver, receiverTile, receiverStake);
//        System.out.printf("Evals: %d, %d\n", getSenderGain(), getReceiverGain());
    }


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
//            System.out.printf("Trade offer created: %s %s %d %s %s %d\n", sender, senderTile, senderStake, receiver, receiverTile, receiverStake);
//            System.out.printf("Evals: %d, %d\n", getSenderGain(), getReceiverGain());
            negotiate();
        }
    }

    public void negotiate() {
        if (getSenderGain() > 0 && getReceiverGain() > 0 && senderStake > sender.getBalance() * 0.45) {
            isFairTrade = true;
        } else {
            isFairTrade = false;
        }

//        System.out.println("Fair trade: " + isFairTrade);
    }

    public int getSenderGain() {
        return (TradeBroker.getAssetWorthToOther(receiverTile, sender) + senderStake) -
                (TradeBroker.getAssetWorth(senderTile) + senderStake);
    }

    public int getReceiverGain() {
        return (TradeBroker.getAssetWorthToOther(senderTile, receiver) + senderStake) -
                (TradeBroker.getAssetWorth(receiverTile) + receiverStake);
    }

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

    public Player getSender() {
        return sender;
    }

    public void setSender(Player sender) {
        this.sender = sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public void setReceiver(Player receiver) {
        this.receiver = receiver;
    }

    public Tile getSenderTile() {
        return senderTile;
    }

    public void setSenderTile(Tile senderTile) {
        this.senderTile = senderTile;
    }

    public Tile getReceiverTile() {
        return receiverTile;
    }

    public void setReceiverTile(Tile receiverTile) {
        this.receiverTile = receiverTile;
    }

    public int getSenderStake() {
        return senderStake;
    }

    public void setSenderStake(int senderStake) {
        this.senderStake = senderStake;
    }

    public int getReceiverStake() {
        return receiverStake;
    }

    public void setReceiverStake(int receiverStake) {
        this.receiverStake = receiverStake;
    }

    public boolean isFairTrade() {
        return isFairTrade;
    }
}
