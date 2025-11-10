public class Player {
    private int x;
    private int y;
    private double hp;
    public String terrainStringBuffer;

    public Player(int x, int y, Map map) {
        this.x = x;
        this.y = y;
        hp = 100;
    }

    public void setPosition(int x, int y, Map map) {
        this.x = x;
        this.y = y;
        terrainStringBuffer = map.getTerrain(x,y).stringRep;
    }

    public void setHP(double hp) {
        this.hp = hp;
    }

    public void setPosX(int x) {
        this.x = x;
    }

    public void setPosY(int y) {
        this.y = y;
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
}
