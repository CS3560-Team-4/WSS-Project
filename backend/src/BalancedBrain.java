import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * BalancedBrain - plays cautiously, prioritizing survival resources,
 * then low-risk exploration and progress toward the goal.
 */
public class BalancedBrain extends Brain {

    public BalancedBrain(GameState state, Player player, Vision vision) {
        super(Objects.requireNonNull(state),
              Objects.requireNonNull(player),
              Objects.requireNonNull(vision));
    }

    @Override
    protected Move chooseExploreStrategy() {
        // Exploration should focus on moving safely into new areas,
        // not greedily chasing the nearest item.
        return pickSafeNeighbor();
    }

    @Override
    protected Move chooseSurvivalStrategy() {
        // When resources are low, go for nearest water/food first.
        Vision.TileInfo resource = vision.findClosest(t -> t.hasWater || t.hasFood);
        if (resource != null) {
            return stepToward(resource.x, resource.y);
        }
        return pickSafeNeighbor();
    }

   @Override
protected Move chooseGoalStrategy() {

    Vision.TileInfo goal = vision.getGoalTile();
    if (goal == null) {
        // No goal in sight → keep exploring eastward (your existing bias)
        return stepEastBias();
    }

    int px = player.getPosX();
    int py = player.getPosY();
    int gx = goal.x;
    int gy = goal.y;

    // Compute deltas
    int dx = gx - px;
    int dy = gy - py;

    // --- 1. Try horizontal first (east/west) ---
    if (dx > 0 && isNeighborWalkable(px + 1, py)) {
        return Move.MoveEast;
    }
    if (dx < 0 && isNeighborWalkable(px - 1, py)) {
        return Move.MoveWest;
    }

    // --- 2. If horizontal path is blocked, try vertical toward goal ---
    if (dy > 0 && isNeighborWalkable(px, py + 1)) {
        return Move.MoveSouth;
    }
    if (dy < 0 && isNeighborWalkable(px, py - 1)) {
        return Move.MoveNorth;
    }

    // --- 3. If both direct paths are blocked, try any walkable neighbor
    //        that reduces MANHATTAN distance to the goal. ---
    Move best = null;
    int bestDist = Integer.MAX_VALUE;

    for (Vision.TileInfo n : vision.getWalkableNeighbors()) {
        int dist = Math.abs(gx - n.x) + Math.abs(gy - n.y);
        if (dist < bestDist) {
            bestDist = dist;
            best = directionFromDelta(n.x - px, n.y - py);
        }
    }

    if (best != null) return best;

    // --- 4. If fully boxed in, use fallback anti-loop explorer ---
    return pickSafeNeighbor();
}


    private Move stepToward(int tx, int ty) {
        int px = player.getPosX();
        int py = player.getPosY();

        int dx = Integer.compare(tx, px);
        int dy = Integer.compare(ty, py);

        // Balanced: prefer the axis with larger distance, but avoid standing still.
        if (Math.abs(tx - px) >= Math.abs(ty - py)) {
            if (dx > 0) return Move.MoveEast;
            if (dx < 0) return Move.MoveWest;
        }
        if (dy > 0) return Move.MoveSouth;
        if (dy < 0) return Move.MoveNorth;

        return Move.MoveNorth;
    }

   private Move pickSafeNeighbor() {
    int px = player.getPosX();
    int py = player.getPosY();

    List<Vision.TileInfo> neighbors = new ArrayList<>(vision.getWalkableNeighbors());

    Collections.shuffle(neighbors); // <-- prevents directional bias

    for (Vision.TileInfo n : neighbors) {
        int dx = n.x - px;
        int dy = n.y - py;
        return directionFromDelta(dx, dy);
    }

    return Move.MoveNorth;
    }
    private Move directionFromDelta(int dx, int dy) {
    if (dx > 0) return Move.MoveEast;
    if (dx < 0) return Move.MoveWest;
    if (dy > 0) return Move.MoveSouth;
    if (dy < 0) return Move.MoveNorth;
    return Move.MoveNorth; // fallback, shouldn't happen
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

private boolean isNeighborWalkable(int x, int y) {
    for (Vision.TileInfo n : vision.getWalkableNeighbors()) {
        if (n.x == x && n.y == y) return true;
    }
    return false;
}



}
