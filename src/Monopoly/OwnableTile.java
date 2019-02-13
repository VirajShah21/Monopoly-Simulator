package Monopoly;

public abstract class OwnableTile extends Tile {


    /**
     * The Player object which represents who owns the property
     */
    protected Player owner;

    protected boolean mortgaged;

    protected int propertyValue;

    /**
     * Constructs the Tile super.super class, and the Ownable Tile super class
     *
     * @param tileType      The type of tile being created
     * @param tileName      The name of the tile being created
     * @param propertyValue The price of the property
     */
    public OwnableTile(TileType tileType, String tileName, int propertyValue) {
        super(tileType, tileName);
        this.propertyValue = propertyValue;
        mortgaged = false;
    }

    /**
     * @return True if the property is owned; false otherwise
     */
    public boolean isOwned() {
        return owner != null;
    }

    /**
     * @return The Player object of the Player who owns this property
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Forecloses a property. Gives the player their money bank, strips Player object of ownership,
     * and gives the property back to the bank.
     */
    @SuppressWarnings("unused")
    public void foreclose() {
        owner.addBalance(getPropertyValue());
        owner.removeAsset(owner.getAssets().indexOf(this));
        owner = null;
    }


    /**
     * Allows a player to purchase the current utility tile
     *
     * @param player The player who is purchasing the property
     */
    public void buy(Player player) {
        if (!isOwned()) {
            owner = player;
            player.deductBalance(getPropertyValue());
            player.addAsset(this);
        }
    }

    /**
     * Transfers owner ship of a property tile to another Player object
     *
     * @param newOwner The owner who the property should be sent to
     */
    public void transferOwnership(Player newOwner) {
        int oldIndex = owner.getAssets().indexOf(this);
        owner.removeAsset(oldIndex);
        owner = newOwner;
        owner.addAsset(this);
    }

    public boolean isMortgaged() {
        return mortgaged;
    }

    public boolean isMortgagable() {
        return !mortgaged;
    }

    public void mortgage() {
        if (isMortgagable()) {
            mortgaged = true;
            owner.addBalance(propertyValue / 2);
        }
    }

    /**
     * Get the value of the property
     *
     * @return The value of the property
     */
    abstract int getPropertyValue();

    /**
     * Get the rent as a result of landing on the ownable tile
     *
     * @return The rent of landing on the tile
     */
    abstract int getRent();

    /**
     * Checks to see if the ownable tile is a monopoly
     *
     * @return True if property belongs to an owned monopoly set; false otherwise
     */
    abstract boolean isMonopoly();

}
