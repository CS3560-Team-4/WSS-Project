
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a small sequence of {@link Move} steps from the player's current
 * position towards some target, along with accumulated resource costs.
 *
 * Costs are expressed in the same integer units used by the legacy engine
 * (Terrain.movementCost, Terrain.waterCost, Terrain.energyCost).
 */
public final class Path {

    private final List<Move> moves;
    private final int totalMovementCost;
    private final int totalWaterCost;
    private final int totalFoodCost;

    public Path(List<Move> moves, int totalMovementCost,
                int totalWaterCost, int totalFoodCost) {
        this.moves = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(moves, "moves")));
        this.totalMovementCost = totalMovementCost;
        this.totalWaterCost = totalWaterCost;
        this.totalFoodCost = totalFoodCost;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public int getTotalMovementCost() {
        return totalMovementCost;
    }

    public int getTotalWaterCost() {
        return totalWaterCost;
    }

    public int getTotalFoodCost() {
        return totalFoodCost;
    }

    /**
     * Returns true if the given player has sufficient resources to safely
     * execute this path.
     */
    public boolean isValidFor(Player player) {
        if (player == null) {
            return false;
        }
        // Energy in the legacy game represents the player's stamina/food and
        // is spent when moving. Here we require that the player can afford
        // both the movement and food portions using their current energy.
        int requiredEnergy = totalMovementCost + totalFoodCost;
        return player.getEnergy() >= requiredEnergy
                && player.getWater() >= totalWaterCost;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Path[");
        for (int i = 0; i < moves.size(); i++) {
            sb.append(moves.get(i));
            if (i < moves.size() - 1) {
                sb.append(" -> ");
            }
        }
        sb.append(", moveCost=").append(totalMovementCost);
        sb.append(", waterCost=").append(totalWaterCost);
        sb.append(", foodCost=").append(totalFoodCost);
        sb.append(']');
        return sb.toString();
    }
}
