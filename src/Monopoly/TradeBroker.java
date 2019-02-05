package Monopoly;

import javax.rmi.CORBA.Util;
import java.util.*;


/**
 * The TradeBroker class, when implemented, allows for automated players to create trade offers,
 * accept offers, deny offers, etc
 */
public class TradeBroker {
    public static int getAssetWorth(Tile tile) {
        int eval = 0;

        if (tile.TYPE == Tile.TileType.PROPERTY) {
            PropertyTile property = (PropertyTile) tile;

            eval = property.getPropertyValue();
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

            eval = property.getPropertyValue();

            if (property.isMonopoly())
                eval *= 2;
        } else if (tile.TYPE == Tile.TileType.RAILROAD) {
            RailroadTile property = (RailroadTile) tile;

            eval = property.getPropertyValue() + (int)(property.getRent() / 200.0 * 4);
        }

        return eval;
    }

    public static void sortAssetsByWorth(Player player) {
        ArrayList<Tile> assets = player.getAssets();

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
