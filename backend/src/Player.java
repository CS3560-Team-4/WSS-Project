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

    public String terrainStringBuffer;
    public int turnsUntilDeath;

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
    }

    public void setWater(int water) {
        this.water = water;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
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

    public int getGold() {
        return this.gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void incrementGoldBy(int amount) {
        this.gold += amount;
    }

    public void setOnGoalTile(boolean bool) {
        this.onGoalTile = bool;
    }

    public boolean getOnGoalTile() {
        return this.onGoalTile;
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
    //note: perhaps a while loop to control health loss?
    //this is meant to be a one time check so no i dont think that sould be a good idea
    public void resourceCheck() {
        if ( this.water <=0 || this.energy <= 0) {
            this.hp = this.hp - 25;
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
}
