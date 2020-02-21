package org.virajshah.monopoly.tiles;


/**
 * The Tile class is the superclass for all the squares on a Monopoly game board. The class has two fields
 * inherited by all children: TYPE, and NAME.
 * 
 * @author Viraj Shah
 */
public abstract class Tile {
    /**
     * Enumeration used to express the type of tile the current *Tile object refers to
     */
    public static enum TileType {
        PROPERTY, UTILITY, RAILROAD, CHANCE, COMMUNITY_CHEST, TAX, FREE_PARKING,
        GO_TO_JAIL, JAIL, GO
    }

    /**
     * The TileType of an instance of a Tile
     */
    TileType TYPE;

    /**
     * The actual name of an instance of a Tile; eg: Reading Railroad, Boardwalk, GO, etc.
     */
    String NAME;

    /**
     * Constructs the prototype for all Tile subclasses
     *
     * @param tileType The type of tile which is being constructed, eg: Property Tile
     * @param tileName The name of the tile or property/utility/railroad name
     */
    Tile(TileType tileType, String tileName) {
        TYPE = tileType;
        NAME = tileName;
    }

    /**
     * @return The type of instance of a Tile subclass
     */
    public TileType getType() {
        return TYPE;
    }

    /**
     * @return The name of a Tile subclass
     */
    @SuppressWarnings("unused")
    public String getName() {
        return NAME;
    }

    /**
     * Builds a game board
     *
     * @return The Monopoly game board which was built
     */
    public static Tile[] buildBoard() {
        Tile[] out = new Tile[40];

        out[0] = new GoTile();
        out[1] = new PropertyTile("Mediterranean Avenue", 60,
                new int[]{2, 10, 30, 90, 160, 250}, 1);
        out[2] = new CommunityChestTile();
        out[3] = new PropertyTile("Baltic Avenue", 60,
                new int[]{4, 20, 60, 180, 320, 450}, 1);
        out[4] = new TaxTile();
        out[5] = new RailroadTile("Reading Railroad");
        out[6] = new PropertyTile("Oriental Avenue", 100,
                new int[]{6, 30, 90, 270, 400, 550}, 2);
        out[7] = new ChanceTile();
        out[8] = new PropertyTile("Vermont Avenue", 100,
                new int[]{6, 30, 90, 270, 400, 550}, 2);
        out[9] = new PropertyTile("Connecticut Avenue", 120,
                new int[]{8, 40, 100, 300, 450, 600}, 2);
        out[10] = new JailTile();
        out[11] = new PropertyTile("St. Charles Place", 140,
                new int[]{10, 50, 150, 450, 625, 750}, 3);
        out[12] = new UtilityTile("Electric Company");
        out[13] = new PropertyTile("States Avenue", 140,
                new int[]{10, 50, 150, 450, 625, 750}, 3);
        out[14] = new PropertyTile("Virginia Avenue", 160,
                new int[]{12, 60, 180, 500, 700, 900}, 3);
        out[15] = new RailroadTile("Pennsylvania Railroad");
        out[16] = new PropertyTile("St. James Place", 180,
                new int[]{14, 70, 200, 550, 750, 950}, 4);
        out[17] = new CommunityChestTile();
        out[18] = new PropertyTile("Tennessee Avenue", 180,
                new int[]{14, 70, 200, 550, 750, 950}, 4);
        out[19] = new PropertyTile("New York Avenue", 200,
                new int[]{16, 80, 220, 600, 800, 1000}, 4);
        out[20] = new FreeParkingTile();
        out[21] = new PropertyTile("Kentucky Avenue", 220,
                new int[]{18, 90, 250, 700, 875, 1050}, 5);
        out[22] = new ChanceTile();
        out[23] = new PropertyTile("Indiana Avenue", 220,
                new int[]{18, 90, 250, 700, 875, 1050}, 5);
        out[24] = new PropertyTile("Illinois Avenue", 240,
                new int[]{20, 100, 300, 750, 925, 1100}, 5);
        out[25] = new RailroadTile("B. & O. Railroad");
        out[26] = new PropertyTile("Atlantic Avenue", 260,
                new int[]{22, 110, 330, 800, 975, 1150}, 6);
        out[27] = new PropertyTile("Ventnor Avenue", 260,
                new int[]{22, 110, 330, 800, 975, 1150}, 6);
        out[28] = new UtilityTile("Waterworks");
        out[29] = new PropertyTile("Marvin Gardens", 280,
                new int[]{24, 120, 360, 850, 1025, 1200}, 6);
        out[30] = new GoToJailTile();
        out[31] = new PropertyTile("Pacific Avenue", 300,
                new int[]{26, 130, 390, 900, 1100, 1275}, 7);
        out[32] = new PropertyTile("North Carolina Avenue", 300,
                new int[]{26, 130, 390, 900, 1100, 1275}, 7);
        out[33] = new CommunityChestTile();
        out[34] = new PropertyTile("Pennsylvania Avenue", 320,
                new int[]{28, 150, 450, 1000, 1200, 1400}, 7);
        out[35] = new RailroadTile("Short Line");
        out[36] = new ChanceTile();
        out[37] = new PropertyTile("Park Place", 350,
                new int[]{35, 175, 500, 1100, 1300, 1500}, 8);
        out[38] = new TaxTile();
        out[39] = new PropertyTile("Boardwalk", 400,
                new int[]{50, 200, 600, 1400, 1700, 2000}, 8);

        return out;
    }

    /**
     * @return The name of a tile
     */
    public String toString() {
        return NAME;
    }
}
