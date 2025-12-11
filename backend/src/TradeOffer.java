public class TradeOffer {
    //resources that are given to player
    public int offerGold;
    public int offerWater;
    public int offerFood;
    //resources trader wants
    public int requestGold;
    public int requestWater;
    public int requestFood;

    public Item offeredItem;
    public int goldCost;
    //trade resource constructor
    public TradeOffer(int og, int ow, int of, int rg, int rw, int rf) {
        offerGold = og;
        offerWater = ow;
        offerFood = of;
        requestGold = rg;
        requestWater = rw;
        requestFood = rf;
    }
    //gold trade constructor
    public TradeOffer(Item item, int goldCost) {
        this.offeredItem = item;
        this.goldCost = goldCost;
    }
}
