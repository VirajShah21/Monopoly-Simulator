package Monopoly;
import java.util.ArrayList;

public class JailTile extends Tile {
    ArrayList<Player> playersInJail;
    public JailTile() {
        super(TileType.JAIL, "Jail");
        playersInJail = new ArrayList<>();
    }
}
