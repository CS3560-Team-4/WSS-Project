import java.util.Random;

public class Trader {
    protected String name;
    protected TraderType type;
    protected MoodState mood;
    protected int patience;
    protected TradeOffer previousOffer;
    protected boolean inTrade;

    public Trader(String name, TraderType type, MoodState mood, int patience) {
        this.name = name;
        this.type = type;
        this.mood = mood;
        this.patience = patience;
        this.inTrade = false;
        this.previousOffer = null;
    }

    public TradeOffer acceptOffer(TradeOffer playerOffer) {
        return playerOffer;
    }

    public void decreasePatience() {
        patience--;
    }

    public void rejectTrade() {
        inTrade = false;
    }

    public void beginTrade(Player player) {
        inTrade = true;
        previousOffer = null;
    }

    public boolean isInTrade() {
        return inTrade;
    }

    public String toString() {
        return type.toString().toLowerCase();
    }

    public TradeOffer generateOffer(Player player) {
        Item randomItem = new Item(
            ItemType.values()[new Random()
                .nextInt(ItemType.values().length)]);
        
        int price = switch (this.type) {
            case Friendly -> 5;
            case Generous -> 3;
            case Greedy -> 10;
            case Lowballer -> 15;
        };

        return new TradeOffer(randomItem, price);
    }
}
