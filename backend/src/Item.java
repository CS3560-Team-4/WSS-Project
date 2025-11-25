public class Item {
    public enum Kind {
        WATER_BOTTLE,
        MEDICINE,
        ENERGY_DRINK,
        TURKEY
    }

    private Kind kind;
    private int restoreAmount;

    public Item(Kind kind, int restoreAmount) {
        this.kind = kind;
        this.restoreAmount = restoreAmount;
    }

    public Kind getKind() {
        return kind;
    }

    public int getRestoreAmount() {
        return restoreAmount;
    }

    // Use the item on a player
    public void use(Player player) {
        switch (kind) {
            case TURKEY:
                player.increaseEnergy(25);
                break;
            case WATER_BOTTLE:
                player.increaseWater(25);
                break;
            case MEDICINE:
                player.setHP(player.getHP() + 25);
                break;
            case ENERGY_DRINK:
                // You need to add an increaseEnergy method to Player
                player.increaseEnergy(10);
                break;
        }
    }
}