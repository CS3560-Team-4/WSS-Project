import java.util.ArrayList;
import java.util.List;

public class Path {
    private final List<Move> moves = new ArrayList<>();
    private int totalMovementCost = 0;
    private int totalWaterCost = 0;
    private int totalFoodCost = 0;

    public void addMove(Move move, int movementCost, int waterCost, int foodCost) {
        moves.add(move);
        totalMovementCost += movementCost;
        totalWaterCost += waterCost;
        totalFoodCost += foodCost;
    }

    public boolean isEmpty() {
        return moves.isEmpty();
    }

    public List<Move> getMoves() {
        return moves;
    }

    public int getMovementCost() {
        return totalMovementCost;
    }

    public int getWaterCost() {
        return totalWaterCost;
    }

    public int getFoodCost() {
        return totalFoodCost;
    }
}
