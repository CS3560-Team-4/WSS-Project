// package whatever.you.use;

/**
 * Vision - scans the map around the player and provides info about visible tiles.
 * This class is READ-ONLY - it never modifies the map.
 *
 * It is compatible with the legacy GameState and provides several vision types:
 *  - Vision (default radius-based)
 *  - CautiousVision (small radius 1)
 *  - KeenVision (radius 2 pattern)
 *  - NarrowVision (long thin cross)
 *  - QueenVision (queen-like range)
 */
import java.util.*;

public class Vision {
    private final GameState state;
    private final int radius;

    // Cached scan results
    private List<TileInfo> visibleTiles;
    private int lastScanX = -1;
    private int lastScanY = -1;

    /**
     * Simple container for info about a visible tile.
     */
    public static class TileInfo {
        public final int x, y;
        public final Terrain terrain;
        public final String symbol;
        public final boolean walkable;
        public final boolean hasItem;
        public final boolean hasTrader;
        public final boolean isGoal;
        public final boolean hasGold;
        public final boolean hasFood;
        public final boolean hasWater;

        public TileInfo(int x, int y, Terrain terrain, String originalSymbol) {
            this.x = x;
            this.y = y;
            this.terrain = terrain;
            this.symbol = originalSymbol;

            // Mountains or out-of-bounds could be non-walkable;
            // current implementation treats any non-null terrain as walkable.
            this.walkable = terrain != null;

            // Check what's on this tile
            Object obj = (terrain != null) ? terrain.getTileObject() : null;
            this.hasItem = obj instanceof Item;
            this.hasTrader = obj instanceof Trader;

            // Check if goal
            this.isGoal = "E".equals(originalSymbol) || terrain instanceof Goal;

            // Check item types (no GOLD item in this game)
            if (obj instanceof Item) {
                Item item = (Item) obj;
                ItemType type = item.getType();
                this.hasGold = false; // Gold comes from turns, not items
                this.hasFood = (type == ItemType.TURKEY || type == ItemType.ENERGY_DRINK);
                this.hasWater = (type == ItemType.WATER_BOTTLE || type == ItemType.MEDICINE);
            } else {
                this.hasGold = false;
                this.hasFood = false;
                this.hasWater = false;
            }
        }

        /** Manhattan distance from another point */
        public int distanceTo(int ox, int oy) {
            return Math.abs(x - ox) + Math.abs(y - oy);
        }
    }

    public Vision(GameState state) {
        this(state, 4); // default radius
    }

    public Vision(GameState state, int radius) {
        this.state = state;
        this.radius = radius;
    }

    /**
     * Optional hook for subclasses to define exact visible-offset patterns.
     * Returns null by default to use simple radius scanning.
     */
    protected Move[] getAllowedMoves() {
        return null;
    }

    /**
     * Scan the area around the player and cache results.
     * Returns list of all visible tiles.
     */
    public List<TileInfo> scan() {
        Player p = state.getPlayer();
        int px = p.getPosX();
        int py = p.getPosY();

        // Return cached if position unchanged
        if (visibleTiles != null && px == lastScanX && py == lastScanY) {
            return visibleTiles;
        }

        visibleTiles = new ArrayList<>();
        Map map = state.getMap();
        int width = map.getWidth();
        int height = map.getHeight();

        // If this Vision subclass defines specific allowed moves, use them to determine visible tiles
        Move[] pattern = getAllowedMoves();
        if (pattern != null) {
            for (Move m : pattern) {
                int tx = px + m.dx;
                int ty = py + m.dy;

                if (tx < 0 || ty < 0 || tx >= width || ty >= height) continue;

                Terrain terrain = map.getTerrain(tx, ty);
                if (terrain == null) continue;

                String sym = terrain.stringRep;
                if ("P".equals(sym) && tx == px && ty == py) {
                    sym = p.terrainStringBuffer;
                }

                visibleTiles.add(new TileInfo(tx, ty, terrain, sym));
            }
        } else {
            // Default: scan all tiles within radius square
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dx = -radius; dx <= radius; dx++) {
                    int tx = px + dx;
                    int ty = py + dy;

                    // Skip out of bounds
                    if (tx < 0 || ty < 0 || tx >= width || ty >= height) continue;

                    // Skip current position
                    if (dx == 0 && dy == 0) continue;

                    Terrain terrain = map.getTerrain(tx, ty);
                    if (terrain == null) continue;

                    // Get original symbol (might be "P" if player was there)
                    String sym = terrain.stringRep;
                    if ("P".equals(sym) && tx == px && ty == py) {
                        sym = p.terrainStringBuffer;
                    }

                    visibleTiles.add(new TileInfo(tx, ty, terrain, sym));
                }
            }
        }

        lastScanX = px;
        lastScanY = py;
        return visibleTiles;
    }

    /**
     * Get all tiles with items (food, water, etc.) or traders in vision.
     */
    public List<TileInfo> getItemsInVision() {
        List<TileInfo> items = new ArrayList<>();
        for (TileInfo t : scan()) {
            if (t.hasItem || t.hasTrader) {
                items.add(t);
            }
        }
        return items;
    }

    /**
     * Find the goal tile if visible.
     */
    public TileInfo getGoalTile() {
        for (TileInfo t : scan()) {
            if (t.isGoal) return t;
        }
        return null;
    }

    /**
     * Get immediate walkable neighbors (1 step away).
     */
    public List<TileInfo> getWalkableNeighbors() {
        Player p = state.getPlayer();
        int px = p.getPosX();
        int py = p.getPosY();

        List<TileInfo> neighbors = new ArrayList<>();
        for (TileInfo t : scan()) {
            // Only 1 tile away in any direction
            if (Math.abs(t.x - px) <= 1 && Math.abs(t.y - py) <= 1 && t.walkable) {
                neighbors.add(t);
            }
        }
        return neighbors;
    }

    /**
     * Find closest tile matching a condition (by Manhattan distance).
     */
    public TileInfo findClosest(java.util.function.Predicate<TileInfo> condition) {
        Player p = state.getPlayer();
        int px = p.getPosX();
        int py = p.getPosY();

        TileInfo best = null;
        int bestDist = Integer.MAX_VALUE;

        for (TileInfo t : scan()) {
            if (condition.test(t)) {
                int dist = t.distanceTo(px, py);
                if (dist < bestDist) {
                    bestDist = dist;
                    best = t;
                }
            }
        }
        return best;
    }

    /**
     * Invalidate cache - call after player moves.
     */
    public void invalidate() {
        visibleTiles = null;
        lastScanX = -1;
        lastScanY = -1;
    }

    public int getRadius() {
        return radius;
    }

    /**
     * Get list of visible tile coordinates for frontend.
     * Returns List of [x, y] arrays.
     */
    public List<int[]> getVisibleCoordinates() {
        List<int[]> coords = new ArrayList<>();
        for (TileInfo t : scan()) {
            coords.add(new int[]{t.x, t.y});
        }
        return coords;
    }
}

// ==================== VISION VARIANTS ====================

/**
 * CautiousVision - small radius (1 tile).
 */
class CautiousVision extends Vision {
    public CautiousVision(GameState state) {
        super(state, 1);
    }
}

// Has a sight with a radius of 2
class KeenVision extends Vision {
    public KeenVision(GameState state) {
        super(state, 2);
    }

    @Override
    protected Move[] getAllowedMoves() {
        return new Move[]{
            Move.MoveNorth,
            Move.MoveNorth2,
            Move.MoveNorthWest,
            Move.MoveNorthEast,
            Move.MoveSouth,
            Move.MoveSouth2,
            Move.MoveSouthEast,
            Move.MoveSouthWest,
            Move.MoveEast,
            Move.MoveEast2,
            Move.MoveWest,
            Move.MoveWest2,
            Move.MoveNorth2East1,
            Move.MoveNorth2West1,
            Move.MoveSouth2East1,
            Move.MoveSouth2West1,
            Move.MoveEast2North1,
            Move.MoveEast2South1,
            Move.MoveWest2North1,
            Move.MoveWest2South1,
        };
    } // end allowedMoves
} // end class KeenVision

// Narrow vision - only sees 5 tiles straight in each cardinal direction, with 1 tile diagonal
class NarrowVision extends Vision {
    public NarrowVision(GameState state) {
        super(state, 5);
    }

    @Override
    protected Move[] getAllowedMoves() {
        return new Move[]{
            Move.MoveNorth,
            Move.MoveNorth2,
            Move.MoveNorth3,
            Move.MoveNorth4,
            Move.MoveNorth5,

            Move.MoveSouth,
            Move.MoveSouth2,
            Move.MoveSouth3,
            Move.MoveSouth4,
            Move.MoveSouth5,

            Move.MoveWest,
            Move.MoveWest2,
            Move.MoveWest3,
            Move.MoveWest4,
            Move.MoveWest5,

            Move.MoveEast,
            Move.MoveEast2,
            Move.MoveEast3,
            Move.MoveEast4,
            Move.MoveEast5,

            Move.MoveNorthEast,
            Move.MoveNorthWest,
            Move.MoveSouthEast,
            Move.MoveSouthWest
        };
    } // end allowedMoves
} // end class NarrowVision

// Has the sight of a queen like in chess
class QueenVision extends Vision {
    public QueenVision(GameState state) {
        super(state, 10);
    }

    @Override
    protected Move[] getAllowedMoves() {
        return new Move[]{
            // North
            Move.MoveNorth,
            Move.MoveNorth2,
            Move.MoveNorth3,
            Move.MoveNorth4,
            Move.MoveNorth5,
            Move.MoveNorth6,
            Move.MoveNorth7,
            Move.MoveNorth8,
            Move.MoveNorth9,
            Move.MoveNorth10,

            // South
            Move.MoveSouth,
            Move.MoveSouth2,
            Move.MoveSouth3,
            Move.MoveSouth4,
            Move.MoveSouth5,
            Move.MoveSouth6,
            Move.MoveSouth7,
            Move.MoveSouth8,
            Move.MoveSouth9,
            Move.MoveSouth10,

            // East
            Move.MoveEast,
            Move.MoveEast2,
            Move.MoveEast3,
            Move.MoveEast4,
            Move.MoveEast5,
            Move.MoveEast6,
            Move.MoveEast7,
            Move.MoveEast8,
            Move.MoveEast9,
            Move.MoveEast10,

            // West
            Move.MoveWest,
            Move.MoveWest2,
            Move.MoveWest3,
            Move.MoveWest4,
            Move.MoveWest5,
            Move.MoveWest6,
            Move.MoveWest7,
            Move.MoveWest8,
            Move.MoveWest9,
            Move.MoveWest10,

            // Diagonals NE
            Move.MoveNorthEast,
            Move.MoveNorthEast2,
            Move.MoveNorthEast3,
            Move.MoveNorthEast4,
            Move.MoveNorthEast5,
            Move.MoveNorthEast6,
            Move.MoveNorthEast7,
            Move.MoveNorthEast8,
            Move.MoveNorthEast9,
            Move.MoveNorthEast10,

            // Diagonals NW
            Move.MoveNorthWest,
            Move.MoveNorthWest2,
            Move.MoveNorthWest3,
            Move.MoveNorthWest4,
            Move.MoveNorthWest5,
            Move.MoveNorthWest6,
            Move.MoveNorthWest7,
            Move.MoveNorthWest8,
            Move.MoveNorthWest9,
            Move.MoveNorthWest10,

            // Diagonals SE
            Move.MoveSouthEast,
            Move.MoveSouthEast2,
            Move.MoveSouthEast3,
            Move.MoveSouthEast4,
            Move.MoveSouthEast5,
            Move.MoveSouthEast6,
            Move.MoveSouthEast7,
            Move.MoveSouthEast8,
            Move.MoveSouthEast9,
            Move.MoveSouthEast10,

            // Diagonals SW
            Move.MoveSouthWest,
            Move.MoveSouthWest2,
            Move.MoveSouthWest3,
            Move.MoveSouthWest4,
            Move.MoveSouthWest5,
            Move.MoveSouthWest6,
            Move.MoveSouthWest7,
            Move.MoveSouthWest8,
            Move.MoveSouthWest9,
            Move.MoveSouthWest10,
        };
    } // end allowedMoves
} // end class QueenVision