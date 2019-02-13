package Monopoly.LoggerTools;

public class LandingLog {
    private String name;
    private int tile;
    private int rentDue;

    public LandingLog(String name, int tile, int rentDue) {
        this.name = name;
        this.tile = tile;
        this.rentDue = rentDue;
    }

    public String getName() {
        return name;
    }

    public int getTile() {
        return tile;
    }

    public int getRentDue() {
        return rentDue;
    }
}
