package Monopoly;

import java.util.*;

import Monopoly.Tiles.*;

/**
 * Builds win-win trades for any two players. The TradeBroker class associates
 * with a Player object, which is the client. The broker will only build trades
 * if it will benefit the client Player.
 */
public class TradeBroker {
	/**
	 * The Player object which the TradeBroker primarily represents
	 */
	private Player client;

	/**
	 * Contstruct a TradeBroker object
	 * 
	 * @param client The client Player
	 */
	public TradeBroker(Player client) {
		this.client = client;
	}

	/**
	 * @return A HashMap of how complete each monopoly set is
	 */
	private HashMap<OwnableTile, Double> getSetCompletions() {
		HashMap<OwnableTile, Double> completionRates = new HashMap<>();

		for (OwnableTile asset : client.getAssets()) {
			if (asset.getType() == Tile.TileType.PROPERTY) {
				int group = ((PropertyTile) asset).getGroupNumber();
				int count = 0;
				int setMaxCount = group == 1 || group == 8 ? 2 : 3;
				for (OwnableTile comparableAsset : client.getAssets()) {
					if (comparableAsset.getType() == Tile.TileType.PROPERTY
							&& ((PropertyTile) comparableAsset).getGroupNumber() == group)
						count++;
				}
				completionRates.put(asset, (double) count / setMaxCount);
			} else if (asset.getType() == Tile.TileType.RAILROAD) {
				int count = 0;
				for (OwnableTile comparableAsset : client.getAssets()) {
					if (comparableAsset.getType() == Tile.TileType.RAILROAD)
						count++;
				}
				completionRates.put(asset, (double) count / 4.0);
			} else if (asset.getType() == Tile.TileType.UTILITY) {
				int count = 0;
				for (OwnableTile comparableAsset : client.getAssets()) {
					if (comparableAsset.getType() == Tile.TileType.UTILITY)
						count++;
				}
				completionRates.put(asset, (double) count / 2.0);
			}
		}

		return completionRates;
	}

	/**
	 * Describes the value of an asset to a client
	 * 
	 * @param asset An OwnableTile (Property, Railroad, or Utility) to be evaluated
	 *              by the TradeBroker
	 * @return The value of a property to the client
	 */
	public int valueToClient(OwnableTile asset) {
		double value = 200;
		double setCompletion;

		{
			HashMap<OwnableTile, Double> setCompletions = getSetCompletions();
			if (setCompletions.keySet().contains(asset))
				setCompletion = getSetCompletions().get(asset);
			else
				setCompletion = 0;
		}

		if (setCompletion == 1) {
			value *= 2;
		} else if (setCompletion >= 0.5) {
			value *= 1 + setCompletion;
		}

		if (asset.getType() == Tile.TileType.PROPERTY) {
			value *= 1.33;

			if (setCompletion == 1) {
				PropertyTile property = (PropertyTile) asset;
				if (property.getHouses() > 0) {
					value *= property.getHouses();
				}
			}
		}

		return (int) value + asset.getPropertyValue();
	}

	/**
	 * Build a list of the most wanted properties from a clients list of assets
	 * 
	 * @param completionThreshold How complete an asset should be to be added to the
	 *                            list of wanted assets
	 * @return An ArrayList of OwnableTiles which describe the most wanted
	 *         properties by the client
	 */
	public ArrayList<OwnableTile> mostWantedProperties(double completionThreshold) {
		HashMap<OwnableTile, Double> completionRates = getSetCompletions();
		ArrayList<OwnableTile> wanted = new ArrayList<>();

		for (OwnableTile asset : completionRates.keySet())
			if (completionRates.get(asset) >= completionThreshold)
				wanted.add(asset);

		for (int i = 0; i < wanted.size() - 1; i++) {
			for (int j = i + 1; j < wanted.size(); j++) {
				if (valueToClient(wanted.get(i)) < valueToClient(wanted.get(j))) {
					OwnableTile tmp = wanted.get(i);
					wanted.set(i, wanted.get(j));
					wanted.set(j, tmp);
				}
			}
		}

		return wanted;
	}

	/**
	 * Calls mostWantedProperties(0.5) w/ completion threshold of 0.5 (deducted by
	 * 0.1 until the returned ArrayList has at least one item).
	 * 
	 * @return An ArrayList of the most wanted properties
	 */
	public ArrayList<OwnableTile> mostWantedProperties() {
		double completionThreshold = 0.5;
		ArrayList<OwnableTile> wanted = mostWantedProperties(completionThreshold);

		while (wanted.size() < 1 && completionThreshold >= 0) {
			completionThreshold -= 0.1;
			wanted = mostWantedProperties(completionThreshold);
		}

		return wanted;
	}

	/**
	 * Check if the client has a PropertyTile with the specified group number
	 * 
	 * @param groupNumber The group number (representative of color group) to search
	 *                    for
	 * @return True if client has PropertyTile with the specified group number;
	 *         false otherwise
	 */
	public boolean hasAssetFromSet(int groupNumber) {
		for (PropertyTile property : client.getProperties())
			if (property.getGroupNumber() == groupNumber)
				return true;
		return false;
	}

	/**
	 * Check if the client has an OwnableTile (either Railroad or Utility)
	 * 
	 * @param tileType The TileType to check for
	 * @return True if client has the specified type of tile; false otherwise
	 */
	public boolean hasAssetFromSet(Tile.TileType tileType) {
		if (tileType == Tile.TileType.RAILROAD) {
			return client.getRailroads().size() > 0;
		} else if (tileType == Tile.TileType.UTILITY) {
			return client.getUtilities().size() > 0;
		} else {
			return false;
		}
	}

	/**
	 * Get a PropertyTile from the client with the specified group number
	 * 
	 * @param groupNumber The filter for group
	 * @return A single PropertyTile from the specified group number from the
	 *         clients list of assets
	 */
	public PropertyTile getPrimaryAssetFromSet(int groupNumber) {
		for (PropertyTile property : client.getProperties()) {
			if (property.getGroupNumber() == groupNumber) {
				return property;
			}
		}
		return null;
	}

	/**
	 * Get an OwnableTile (Railroad or Utility) from the client with a specified
	 * TileType
	 * 
	 * @param tileType The filter for the property type
	 * @return An OwnableTile (Railroad or Utility) from the client
	 */
	public OwnableTile getPrimaryAssetFromSet(Tile.TileType tileType) {
		if (tileType == Tile.TileType.RAILROAD)
			return client.getRailroads().get(0);
		else if (tileType == Tile.TileType.UTILITY)
			return client.getUtilities().get(0);
		else
			return null;
	}

	/**
	 * Builds the best possible trade for the client (and for another specified
	 * player
	 * 
	 * @param otherPlayer The other player to broker a deal with
	 * @return True if a good (and fair) deal was made and executed; false if no
	 *         deal was made.
	 */
	public boolean buildBestTradeOffer(Player otherPlayer) {
		TradeBroker otherBroker = new TradeBroker(otherPlayer);
		OwnableTile mostWanted, otherMostWanted;
		int wantedSet, otherWantedSet; // 1-8 = colored properties; 9 = railroad; 10 = utility
		Tile.TileType wantedSetType, otherWantedSetType; // to avoid long lines later

		{
			ArrayList<OwnableTile> ranked = mostWantedProperties();
			ArrayList<OwnableTile> otherRanked = otherBroker.mostWantedProperties();
			mostWanted = ranked.size() > 0 ? mostWantedProperties().get(0) : null;
			otherMostWanted = otherRanked.size() > 0 ? otherBroker.mostWantedProperties().get(0) : null;
		}

		if (mostWanted == null || otherMostWanted == null)
			return false;

		if (mostWanted.getType() == Tile.TileType.PROPERTY)
			wantedSet = ((PropertyTile) mostWanted).getGroupNumber();
		else if (mostWanted.getType() == Tile.TileType.RAILROAD)
			wantedSet = 9;
		else if (mostWanted.getType() == Tile.TileType.UTILITY)
			wantedSet = 10;
		else
			wantedSet = 0;

		if (otherMostWanted.getType() == Tile.TileType.PROPERTY)
			otherWantedSet = ((PropertyTile) otherMostWanted).getGroupNumber();
		else if (otherMostWanted.getType() == Tile.TileType.RAILROAD)
			otherWantedSet = 9;
		else if (otherMostWanted.getType() == Tile.TileType.UTILITY)
			otherWantedSet = 10;
		else
			otherWantedSet = 0;

		if (wantedSet == 9)
			wantedSetType = Tile.TileType.RAILROAD;
		else if (wantedSet == 10)
			wantedSetType = Tile.TileType.UTILITY;
		else if (wantedSet >= 1 && wantedSet <= 8)
			wantedSetType = Tile.TileType.PROPERTY;
		else
			wantedSetType = null;

		if (otherWantedSet == 9)
			otherWantedSetType = Tile.TileType.RAILROAD;
		else if (otherWantedSet == 10)
			otherWantedSetType = Tile.TileType.UTILITY;
		else if (otherWantedSet >= 1 && otherWantedSet <= 8)
			otherWantedSetType = Tile.TileType.PROPERTY;
		else
			otherWantedSetType = null;

		if (wantedSet != otherWantedSet && wantedSet != 0 && otherWantedSet != 0) {
			OwnableTile wantedAsset = null;
			OwnableTile otherWantedAsset = null;

			if (otherWantedSet < 9 && hasAssetFromSet(otherWantedSet))
				otherWantedAsset = getPrimaryAssetFromSet(otherWantedSet);
			else if (hasAssetFromSet(otherWantedSetType))
				otherWantedAsset = getPrimaryAssetFromSet(otherWantedSetType);

			if (otherWantedAsset != null) {
				if (wantedSet < 9 && otherBroker.hasAssetFromSet(wantedSet))
					wantedAsset = otherBroker.getPrimaryAssetFromSet(wantedSet);
				else if (otherBroker.hasAssetFromSet(wantedSetType))
					wantedAsset = otherBroker.getPrimaryAssetFromSet(wantedSetType);

			}

			if (wantedAsset != null && otherWantedAsset != null) {
				int wantedAssetValue = (valueToClient(wantedAsset) + otherBroker.valueToClient(wantedAsset)) / 2;
				int otherWantedAssetValue = (valueToClient(otherWantedAsset)
						+ otherBroker.valueToClient(otherWantedAsset)) / 2;
				int cashOffer = wantedAssetValue - otherWantedAssetValue;
				TradeOffer deal = new TradeOffer(client, otherPlayer, otherWantedAsset, wantedAsset, cashOffer);

				// Check if the deal will bankrupt any players
				if (cashOffer > 0) {
					if (client.getBalance() - cashOffer < 300)
						return false;
				} else if (cashOffer < 0) {
					if (otherPlayer.getBalance() + cashOffer < 300)
						return false;
				}

				deal.execute();
				return true;
			}
		}
		return false;
	}

	/**
	 * Organize the clients assets by value (most valuable to least valuable)
	 */
	public void sortAssetsByWorth() {
		for (int i = 0; i < client.getAssets().size() - 1; i++) {
			for (int j = i + 1; j < client.getAssets().size(); j++) {
				if (valueToClient(client.getAssets().get(i)) < valueToClient(client.getAssets().get(j))) {
					OwnableTile tmp = client.getAssets().get(i);
					client.getAssets().set(i, client.getAssets().get(j));
					client.getAssets().set(j, tmp);
				}
			}
		}
	}
}