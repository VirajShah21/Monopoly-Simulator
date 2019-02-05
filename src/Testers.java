import Monopoly.*;

class Testers {
    static void sortAssetsByWorth() {
        MonopolyGame game = new MonopolyGame();
        Player x = new Player("x", game);
        PropertyTile a = (PropertyTile) game.tileAt(9);
        UtilityTile b = (UtilityTile) game.tileAt(12);
        PropertyTile c = (PropertyTile) game.tileAt(8);

        a.buy(x);
        b.buy(x);
        c.buy(x);

        System.out.println(x.getAssets());
        TradeBroker.sortAssetsByWorth(x);
        System.out.println(x.getAssets());
    }
}
