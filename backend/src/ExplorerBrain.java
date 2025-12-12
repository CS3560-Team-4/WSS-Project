import java.util.Objects;

/**
 * ExplorerBrain - prioritizes exploration and steady progress toward the goal,
 * taking detours only for survival resources.
 */
public class ExplorerBrain extends Brain {

    public ExplorerBrain(GameState state, Player player, Vision vision) {
        super(Objects.requireNonNull(state),
              Objects.requireNonNull(player),
              Objects.requireNonNull(vision));
    }

    @Override
    protected Move chooseExploreStrategy() {
        // Explorer: push eastward when nothing urgent is happening.
        return stepEastBias();
    }

    @Override
    protected Move chooseSurvivalStrategy() {
        // When low on resources, go for nearest water or food.
        Vision.TileInfo resource = vision.findClosest(t -> t.hasWater || t.hasFood);
        if (resource != null) {
            return stepToward(resource.x, resource.y);
        }
        // If no resources visible, still explore east.
        return stepEastBias();
    }

    @Override
    protected Move chooseGoalStrategy() {
        // If goal is visible, bias horizontal progress toward its column first,
        // then adjust vertically as needed. This reduces simple up/down wobbling
        // near the goal.
        Vision.TileInfo goal = vision.getGoalTile();
        if (goal != null) {
            int px = player.getPosX();
            int py = player.getPosY();

            int gx = goal.x;
            int gy = goal.y;

            // Prefer horizontal movement toward the goal's column when not aligned.
            if (gx > px) return Move.MoveEast;
            if (gx < px) return Move.MoveWest;

            // Once aligned horizontally, move vertically toward the goal.
            if (gy > py) return Move.MoveSouth;
            if (gy < py) return Move.MoveNorth;

            // Already on the goal tile; any move is fine (engine will handle win).
            return Move.MoveNorth;
        }
        // Otherwise continue exploring east.
        return stepEastBias();
    }

    private Move stepToward(int tx, int ty) {
        int px = player.getPosX();
        int py = player.getPosY();

        int dx = Integer.compare(tx, px);
        int dy = Integer.compare(ty, py);

        if (dx > 0) return Move.MoveEast;
        if (dx < 0) return Move.MoveWest;
        if (dy > 0) return Move.MoveSouth;
        if (dy < 0) return Move.MoveNorth;
        return Move.MoveNorth;
    }

    private Move stepEastBias() {
        int px = player.getPosX();
        int py = player.getPosY();

        // Prefer any walkable neighbor that moves east.
        for (Vision.TileInfo n : vision.getWalkableNeighbors()) {
            if (n.x > px) return Move.MoveEast;
        }

        // Otherwise pick any walkable neighbor deterministically.
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
