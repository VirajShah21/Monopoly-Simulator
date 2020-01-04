package Monopoly.Tiles;

/**
 * The ChanceTile class is a vestigial subclass of Tile. The main distinction is that (new ChanceTile()).TYPE == CHANCE
 */
class ChanceTile extends Tile {
    /**
     * Constructs a Tile object with properties of a chance tile
     */
    ChanceTile() {
        super(TileType.CHANCE, "Chance");
    }
}