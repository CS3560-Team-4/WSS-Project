public class Terrain {
	int waterCost;
	int energyCost;
	int movementCost;
    String stringRep;
    Object tileObject;

	public Terrain() {
		waterCost = 1;
		energyCost = 1;
		movementCost = 1;
        stringRep = "T";
	}

    public String getSymbol() {
        return stringRep;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    public void setTileObject() {
        
    }
}
