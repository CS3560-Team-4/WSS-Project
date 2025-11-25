public class Terrain {
	public int waterCost;
	public int energyCost;
	public int movementCost;
    public String stringRep;
    public Object tileObject;

    public Terrain() {
        waterCost = 1;
        energyCost = 1;
        movementCost = 1;
        this.stringRep = "T";
        tileObject = null;
    }

    public String getSymbol() {
        return this.stringRep;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    public void setTileObject(Object object) {
        this.tileObject = object;
    }

    public Object getTileObject() {
        return this.tileObject;
    }

    public void removeTileObject() {
        this.tileObject = null;
    }
}
