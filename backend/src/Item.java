public class Item {
    private ItemType type;
    private final int TURKEY_RESOURCE = 25;
    private final int WATER_BOTTLE_RESOURCE = 25;
    private final int MEDICINE_RESOURCE = 25;
    private final int ENERGY_DRINK_RESOURCE = 10;

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
                player.incrementEnergyBy(TURKEY_RESOURCE);
                break;
            case WATER_BOTTLE:
                player.incrementWaterBy(ENERGY_DRINK_RESOURCE);
                break;
            case MEDICINE:
                player.incrementHpBy(MEDICINE_RESOURCE);
                break;
            case ENERGY_DRINK:
                player.incrementEnergyBy(WATER_BOTTLE_RESOURCE);
                break;
        }
    }
}