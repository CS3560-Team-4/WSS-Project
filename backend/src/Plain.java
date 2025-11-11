public class Plain {
	int waterCost;
	int energyCost;
	int movementCost;
    String stringRep;

	public Plain() {
		waterCost = 1;
		energyCost = 1;
		movementCost = 1;
        stringRep = "P";
	}

    public String getSymbol() {
        return stringRep;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
