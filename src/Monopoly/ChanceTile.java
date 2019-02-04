package Monopoly;

/**
 * The ChanceTile class is a vestigial subclass of Tile. The main distinction is that (new ChanceTile()).TYPE == CHANCE
 */
class ChanceTile extends Tile {
    ChanceTile() {
        super(TileType.CHANCE, "Chance");
    }
}