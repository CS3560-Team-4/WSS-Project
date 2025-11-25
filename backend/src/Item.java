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