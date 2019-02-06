package Monopoly;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


/**
 * The TradeBroker class, when implemented, allows for automated players to create trade offers,
 * accept offers, deny offers, etc
 */
public class TradeBroker {
    /**
     * Get the worth of a player holding onto an asset
     *
     * @param tile The tile which is in question; If not PropertyTile, RailroadTile, or UtilityTile,
     *             then the tiles worth will be 0
     * @return The worth of a tile to the player who owns it
     */
    public static int getAssetWorth(@NotNull Tile tile) {
        int eval = 0;

        if (tile.TYPE == Tile.TileType.PROPERTY) {
            PropertyTile property = (PropertyTile) tile;

            eval = property.getPropertyValue() + 200;
            if (property.isMonopoly()) {
                eval *= 2;

                if (property.getHouses() <= 3)
                    eval += (int) Math.pow(10, property.getHouses());
                else if (property.getHouses() == 4)
                    eval += 1500;
                else if (property.getHouses() == 5)
                    eval += 2500;
            } else {
                int colorGroup = property.getGroupNumber();
                int colorGroupInOwnership = 0;

                for (Tile t : property.getOwner().getAssets())
                    if (t.TYPE == Tile.TileType.PROPERTY)
                        if (((PropertyTile) t).getGroupNumber() == colorGroup)
                            colorGroupInOwnership++;


                if (!(colorGroup == 1 || colorGroup == 8))
                    if (colorGroupInOwnership == 2)
                        eval += 250;
            }
        } else if (tile.TYPE == Tile.TileType.UTILITY) {
            UtilityTile property = (UtilityTile) tile;

            eval = property.getPropertyValue() + 200;

            if (property.isMonopoly())
                eval *= 2;
        } else if (tile.TYPE == Tile.TileType.RAILROAD) {
            RailroadTile property = (RailroadTile) tile;

            eval = property.getPropertyValue() + (int) (property.getRent() / 200.0 * 4) + 200;
        }

        return eval - 100;
    }

    public static int getAssetWorthToOther(@NotNull Tile tile, Player player) {
        int eval = 0;

        if (tile.TYPE == Tile.TileType.PROPERTY) {
            PropertyTile property = (PropertyTile) tile;

            eval = property.getPropertyValue() + 200;
            if (property.isMonopoly()) {
                eval = 0;
            }
        } else if (tile.TYPE == Tile.TileType.UTILITY) {
            UtilityTile property = (UtilityTile) tile;

            eval = property.getPropertyValue() + 200;

            if (property.isMonopoly())
                eval *= 2;
        } else if (tile.TYPE == Tile.TileType.RAILROAD) {
            RailroadTile property = (RailroadTile) tile;

            eval = property.getPropertyValue() + (int) (property.getRent() / 200.0 * 4) + 200;
        }

        return eval;
    }

    /**
     * Sort the assets in an instance of a Player object by the worth of holding onto the asset
     *
     * @param player The player whose assets are to be sorted
     */
    public static void sortAssetsByWorth(@NotNull Player player) {
        ArrayList<Tile> assets = player.getAssets();
        if (assets.size() > 1) {
            for (int i = 0; i < assets.size() - 1; i++) {
                for (int j = i + 1; j < assets.size(); j++) {
                    if (getAssetWorth(assets.get(i)) > getAssetWorth(assets.get(j))) {
                        Tile tmp = assets.get(i);
                        assets.set(i, assets.get(j));
                        assets.set(j, tmp);
                    }
                }
            }
        }

    }

    public static void createOffer(Player sender, Player reciever) {
        sortAssetsByWorth(sender);

        if (sender.getAssets().size() >= 2 && reciever.getAssets().size() >= 2) {
            for (Tile t : reciever.getAssets()) {
                if (getAssetWorthToOther(t, sender) >= getAssetWorth(sender.getAssets().get(0))) {
                    TradeOffer trade = new TradeOffer(sender, sender.getAssets().get(0), 0, reciever, t, 0);
                    trade.sendOffer();
                    if (trade.isFairTrade()) {
                        trade.execute();
                        break;
                    }
                }
            }
        }
    }
}
