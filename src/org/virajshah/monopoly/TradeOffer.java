package org.virajshah.monopoly;

import org.virajshah.monopoly.logger.Logger;
import org.virajshah.monopoly.tiles.PropertyTile;
import org.virajshah.monopoly.tiles.RailroadTile;
import org.virajshah.monopoly.tiles.Tile;
import org.virajshah.monopoly.tiles.UtilityTile;

/**
 * A POJO which stores transaction information for a trade
 * 
 * @author viraj
 */
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
	private int cashOffer;

	/**
	 * Constructs a TradeOffer object
	 *
	 * @param sender       The Player initializing the offer
	 * @param receiver     The receiving player
	 * @param senderTile   The sender's tile up for trade
	 * @param receiverTile The receiving player's tile being requested by the sender
	 * @param cashOffer    The amount of money the sender is willing to pay for the
	 *                     trade. If the value is negative, then the "sender" would
	 *                     receive the cash amount.
	 */
	public TradeOffer(Player sender, Player receiver, Tile senderTile, Tile receiverTile, int cashOffer) {
		this.sender = sender;
		this.senderTile = senderTile;
		this.receiver = receiver;
		this.receiverTile = receiverTile;
		this.cashOffer = cashOffer;
	}

	/**
	 * Execute the trade between the recipient and the sender
	 */
	public void execute() {
		if (cashOffer > 0) {
			sender.deductBalance(cashOffer);
			receiver.addBalance(cashOffer);
		} else if (cashOffer < 0) {
			sender.addBalance(-cashOffer);
			receiver.deductBalance(-cashOffer);
		}

		if (senderTile.getType() == Tile.TileType.PROPERTY) {
			((PropertyTile) senderTile).transferOwnership(receiver);
		} else if (senderTile.getType() == Tile.TileType.RAILROAD) {
			((RailroadTile) senderTile).transferOwnership(receiver);
		} else if (senderTile.getType() == Tile.TileType.UTILITY) {
			((UtilityTile) senderTile).transferOwnership(receiver);
		} else {
			System.out.println(senderTile + " is not an ownable asset");
			return;
		}

		if (receiverTile.getType() == Tile.TileType.PROPERTY) {
			((PropertyTile) receiverTile).transferOwnership(sender);
		} else if (receiverTile.getType() == Tile.TileType.RAILROAD) {
			((RailroadTile) receiverTile).transferOwnership(sender);
		} else if (receiverTile.getType() == Tile.TileType.UTILITY) {
			((UtilityTile) receiverTile).transferOwnership(sender);
		} else {
			System.out.println(receiverTile + " is not an ownable asset");
			return;
		}

		Logger.log(String.format("%s and %s executed a trade: %s and $%d for %s", sender, receiver, senderTile,
				cashOffer, receiverTile));
	}
}
