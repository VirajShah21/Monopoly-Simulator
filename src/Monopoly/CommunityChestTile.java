package Monopoly;

/**
 * The CommunityChestTile class is a vestigial subclass of Tile. The main distinction is that
 * (new CommunityChestTile()).TYPE == COMMUNITY_CHEST
 */
class CommunityChestTile extends Tile {
    CommunityChestTile() {
        super(TileType.COMMUNITY_CHEST, "Community Chest");
    }
}
