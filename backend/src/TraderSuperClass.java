import java.util.Random;

public abstract class TraderSuperClass {

    protected String name;
    protected TraderType type;
    protected MoodState mood;
    protected int patience;
    protected TradeOffer previousOffer;
    protected boolean inTrade;
    protected int behavior;
    protected int id;
    protected Random rand = new Random();

    public TraderSuperClass(String name, TraderType type, MoodState mood, int patience, int behavior, int id) {
        this.name = name;
        this.type = type;
        this.mood = mood;
        this.patience = patience;
        this.behavior = behavior;
        this.id = id;
        this.inTrade = false;
        this.previousOffer = null;
    }

    public abstract OfferResult evaluateOffer(TradeOffer playerOffer);

    public abstract TradeOffer counterOffer(TradeOffer playerOffer);

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

    public int getBehavior() {
        return behavior;
    }

    public int getId() {
        return id;
    }

    public boolean isInTrade() {
        return inTrade;
    }
}
