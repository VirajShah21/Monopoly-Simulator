package Monopoly;

/**
 * The GoToJail class is an extension of Tile, with no internal differences in functionality
 */
class GoToJailTile extends Tile {
    /**
     * Constructs a Tile with the properties of a a Go to jail tile
     */
    GoToJailTile() {
        super(TileType.GO_TO_JAIL, "Go to Jail");
    }
}
