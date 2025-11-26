public class Player {
    private int x;
    private int y;
    private int prevX;
    private int prevY;

    private double hp;
    private int energy;
    private int water;
    private int gold;
    private boolean onGoalTile;
    private boolean onTraderTile;

    public String terrainStringBuffer;
    public int turnsUntilDeath;

    private int waterBottleConsumed;
    private int turkeyConsumed;
    private int energyDrinkConsumed;
    private int medicineConsumed;

    public Player(int x, int y, Map map) {
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;

        this.hp = 100;
        this.energy = 100;
        this.water = 100;
        this.gold = 0;
        this.onGoalTile = false;

        this.terrainStringBuffer = map.getTerrain(x,y).stringRep;
        this.turnsUntilDeath = 0;
    }

    public void setPosition(int x, int y, Map map) {
        this.x = x;
        this.y = y;
        this.terrainStringBuffer = map.getTerrain(x,y).stringRep;
    }

    public void setHP(double hp) {
        this.hp = hp;
        clampPlayerStats();
    }

    public void setWater(int water) {
        this.water = water;
        clampPlayerStats();
    }

    public void setEnergy(int energy) {
        this.energy = energy;
        clampPlayerStats();
    }

    public int getWater() {
        return this.water;
    }

    public int getEnergy() {
        return this.energy;
    }

    public double getHP() {
        return this.hp;
    }

    public void incrementEnergyBy(int amount) {
        this.energy += amount;
        clampPlayerStats();
    }

    public void incrementWaterBy(int amount) {
        this.water += amount;
        clampPlayerStats();
    }

    public void incrementHpBy(int amount) {
        this.hp += amount;
        clampPlayerStats();
    }

    public int getGold() {
        return this.gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
        clampPlayerStats();
    }

    public void incrementGoldBy(int amount) {
        this.gold += amount;
        clampPlayerStats();
    }

    public void resetPlayerResources() {
        this.hp = 100;
        this.energy = 100;
        this.water = 100;
        this.gold = 0;
    }

    public void setOnGoalTile(boolean bool) {
        this.onGoalTile = bool;
        clampPlayerStats();
    }

    public boolean getOnGoalTile() {
        return this.onGoalTile;
    }

    public void setOnTraderTile(boolean bool) {
        this.onTraderTile = bool;
    }

    public boolean getOnTraderTile() {
        return this.onTraderTile;
    }

    public void setPrev() {
        this.prevX = this.x;
        this.prevY = this.y;
    }

    public void moveUp(Map map) {
        setPrev();
        this.y -= 1;
        Terrain terrain = map.getTerrain(x,y);
        this.energy = this.energy - terrain.energyCost;
        this.water = this.water - terrain.waterCost;
    }

    public void moveDown(Map map) {
        setPrev();
        this.y += 1;
        Terrain terrain = map.getTerrain(x,y);
        this.energy = this.energy - terrain.energyCost;
        this.water = this.water - terrain.waterCost;
    }

    public void moveLeft(Map map) {
        setPrev();
        this.x -= 1;
        Terrain terrain = map.getTerrain(x,y);
        this.energy = this.energy - terrain.energyCost;
        this.water = this.water - terrain.waterCost;
    }

    public void moveRight(Map map) {
        setPrev();
        this.x += 1;
        Terrain terrain = map.getTerrain(x,y);
        this.energy = this.energy - terrain.energyCost;
        this.water = this.water - terrain.waterCost;
    }

    public int getPosX() {
        return this.x;
    }

    public int getPosY() {
        return this.y;
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    //in the event we run out of resources like food and energy, the player will slowly start losing HP, we run through the isALive function
    //to see if our player has any HP left to lose, 100 is a test value, if we agree on maxHP field, replace 100 with that
    public void resourceCheck() {
        if ( this.water <=0 || this.energy <= 0) {
            this.hp = this.hp - 8;
            System.out.println("GET RESOURCES SOON, OR YOU WILL DIE");
        }
    }

    public int getPrevX() {
        return this.prevX;
    }

    public int getPrevY() {
        return this.prevY;
    }

    public void setPrevX(int x) {
        this.prevX = x;
    }

    public void setPrevY(int y) {
        this.prevY = y;
    }

    public void clampPlayerStats() {
        if (this.hp > 100) this.hp = 100;
        if (this.energy > 100) this.energy = 100;
        if (this.water > 100) this.water = 100;

        if (this.hp < 0) this.hp = 0;
        if (this.energy < 0) this.energy = 0;
        if (this.water < 0) this.water = 0;
        if (this.gold < 0) this.gold = 0;
    }

    public int getWaterBottleConsumed() {
        return this.waterBottleConsumed;
    }

    public int getTurkeyConsumed() {
        return this.turkeyConsumed;
    }

    public int getMedicineConsumed() {
        return this.medicineConsumed;
    }

    public int getEnergyDrinkConsumed() {
        return this.energyDrinkConsumed;
    }

    public void incrementWaterBottleConsumed() {
        this.waterBottleConsumed++;
    }

    public void incrementTurkeyConsumed() {
        this.turkeyConsumed++;
    }

    public void incrementMedicineConsumed() {
        this.medicineConsumed++;
    }

    public void incrementEnergyDrinkConsumed() {
        this.energyDrinkConsumed++;
    }

    public void resetConsumedStats() {
        this.waterBottleConsumed = 0;
        this.turkeyConsumed = 0;
        this.energyDrinkConsumed = 0;
        this.medicineConsumed = 0;
    }
}
