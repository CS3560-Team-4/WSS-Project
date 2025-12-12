
import java.util.Objects;

/**
 * GreedyBrain - prioritizes valuable tiles (items/traders) first,
 * then survival resources, then general exploration.
 */
public class GreedyBrain extends Brain {

    public GreedyBrain(GameState state, Player player, Vision vision) {
        super(Objects.requireNonNull(state),
              Objects.requireNonNull(player),
              Objects.requireNonNull(vision));
    }

    @Override
    protected Move chooseExploreStrategy() {
        // Prefer items or traders in vision.
        Vision.TileInfo target = vision.findClosest(t -> t.hasItem || t.hasTrader);
        if (target != null) {
            return stepToward(target.x, target.y);
        }

        // Otherwise, move toward any walkable neighbor (slight east bias).
        return pickExplorationStep();
    }

    @Override
    protected Move chooseSurvivalStrategy() {
        // Prefer water or food when in survival state.
        Vision.TileInfo resource = vision.findClosest(t -> t.hasWater || t.hasFood);
        if (resource != null) {
            return stepToward(resource.x, resource.y);
        }

        // Fall back to normal exploration if no resources visible.
        return chooseExploreStrategy();
    }

    @Override
    protected Move chooseGoalStrategy() {
        // If goal tile is visible, move directly toward it.
        Vision.TileInfo goal = vision.getGoalTile();
        if (goal != null) {
            return stepToward(goal.x, goal.y);
        }

        // Otherwise just fall back to greedy exploration.
        return chooseExploreStrategy();
    }

    private Move stepToward(int tx, int ty) {
        int px = player.getPosX();
        int py = player.getPosY();

        int dx = Integer.compare(tx, px);
        int dy = Integer.compare(ty, py);

        // Prefer horizontal moves first (toward east/west), then vertical.
        if (dx > 0) return Move.MoveEast;
        if (dx < 0) return Move.MoveWest;
        if (dy > 0) return Move.MoveSouth;
        if (dy < 0) return Move.MoveNorth;

        // Already at target tile; arbitrary fallback.
        return Move.MoveNorth;
    }

    private Move pickExplorationStep() {
        int px = player.getPosX();
        int py = player.getPosY();

        // Try to move east if any walkable neighbor is east; otherwise any neighbor.
        for (Vision.TileInfo n : vision.getWalkableNeighbors()) {
            if (n.x > px) return Move.MoveEast;
        }
        for (Vision.TileInfo n : vision.getWalkableNeighbors()) {
            int dx = n.x - px;
            int dy = n.y - py;
            if (dx > 0) return Move.MoveEast;
            if (dx < 0) return Move.MoveWest;
            if (dy > 0) return Move.MoveSouth;
            if (dy < 0) return Move.MoveNorth;
        }
        return Move.MoveNorth;
    }
}
