public class Player {
    private int x;
    private int y;
    private int prevX;
    private int prevY;
    private double hp;
    private int energy;
    private int water;
    public String terrainStringBuffer;
    public int turnsUntilDeath;

    public Player(int x, int y, Map map) {
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
        hp = 100;
        this.terrainStringBuffer = map.getTerrain(x,y).stringRep;
        energy = 100;
        water = 100;
        turnsUntilDeath = 0;
    }

    public void setPosition(int x, int y, Map map) {
        this.x = x;
        this.y = y;
        terrainStringBuffer = map.getTerrain(x,y).stringRep;
    }

    public void setHP(double hp) {
        this.hp = hp;
    }

    public void setPrev() {
        this.prevX = this.x;
        this.prevY = this.y;
    }

    public void moveUp(Map map) {
        setPrev();
        this.y -= 1;
        Terrain terrain = map.getTerrain(x,y);
        energy = energy - terrain.energyCost;
        water = water - terrain.waterCost;
    }

    public void moveDown(Map map) {
        setPrev();
        this.y += 1;
        Terrain terrain = map.getTerrain(x,y);
        energy = energy - terrain.energyCost;
        water = water - terrain.waterCost;
    }

    public void moveLeft(Map map) {
        setPrev();
        this.x -= 1;
        Terrain terrain = map.getTerrain(x,y);
        energy = energy - terrain.energyCost;
        water = water - terrain.waterCost;
    }

    public void moveRight(Map map) {
        setPrev();
        this.x += 1;
        Terrain terrain = map.getTerrain(x,y);
        energy = energy - terrain.energyCost;
        water = water - terrain.waterCost;
    }

    public int getPosX() {
        return x;
    }

    public int getPosY() {
        return y;
    }

    public boolean isAlive() {
        return hp > 0;
    }
    //in the event we run out of resources like food and energy, the player will slowly start losing HP, we run through the isALive function
    //to see if our player has any HP left to lose, 100 is a test value, if we agree on maxHP field, replace 100 with that
    public void outofResources(){
        if( water <=0 || energy <= 0){
            hp = hp - (100/3);
            if(isAlive()){
                System.out.println("GET RESOURCES SOON, OR YOU WILL DIE");
            }
            else{
                System.out.println("YOU ARE DEAD, NOT BIG SURPRISE");
            }
        }
    }

    public int getPrevX() {
        return prevX;
    }

    public int getPrevY() {
        return prevY;
    }

    public void setPrevX(int x) {
        this.prevX = x;
    }

    public void setPrevY(int y) {
        this.prevY = y;
    }
}
