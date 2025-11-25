public class Item {
    private ItemType type;

    public Item(ItemType type) {
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }

    public String toString() {
        return type.toString().toLowerCase();
    }

    // Use the item on a player
    public void use(Player player) {
        switch (type) {
            case TURKEY:
                player.incrementEnergyBy(25);
                break;
            case WATER_BOTTLE:
                player.incrementWaterBy(25);
                break;
            case MEDICINE:
                player.incrementHpBy(25);
                break;
            case ENERGY_DRINK:
                player.incrementEnergyBy(10);
                break;
        }
    }
}