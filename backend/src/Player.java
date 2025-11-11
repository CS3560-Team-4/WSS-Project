public class Player {
    private int x;
    private int y;
    private int prevX;
    private int prevY;
    private double hp;
    public String terrainStringBuffer;

    public Player(int x, int y, Map map) {
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
        hp = 100;
        this.terrainStringBuffer = map.getTerrain(x, y).stringRep;
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

    public void moveUp() {
        setPrev();
        this.y -= 1;
    }

    public void moveDown() {
        setPrev();
        this.y += 1;
    }

    public void moveLeft() {
        setPrev();
        this.x -= 1;
    }

    public void moveRight() {
        setPrev();
        this.x += 1;
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
