import java.util.Random;

public class Trader {
    //trader information
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
    //accepts player off
    public TradeOffer acceptOffer(TradeOffer playerOffer) {
        return playerOffer;
    }
    //traders patience is decreased 
    public void decreasePatience() {
        patience--;
    }
    //trade is rejected and ended
    public void rejectTrade() {
        inTrade = false;
    }
    //starts new trade with player
    public void beginTrade(Player player) {
        inTrade = true;
        previousOffer = null;
    }
    //check to see if trader is already in trade
    public boolean isInTrade() {
        return inTrade;
    }

    public String toString() {
        return type.toString().toLowerCase();
    }
    //new random offer is generated
    public TradeOffer generateOffer(Player player) {
        Item randomItem = new Item(
            ItemType.values()[new Random()
                .nextInt(ItemType.values().length)]);
        //price dependant on traders mood
        int price = switch (this.type) {
            case Friendly -> 5;
            case Generous -> 3;
            case Greedy -> 10;
            case Lowballer -> 15;
        };

        return new TradeOffer(randomItem, price);
    }
}
