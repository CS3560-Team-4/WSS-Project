import java.util.*;
import static java.util.Comparator.comparingInt;

enum ItemKind { FOOD, WATER, GOLD, TRADER }

final class TileView {
    public final int x, y;
    public final Terrain terrain;
    public final ItemKind itemKind;

    TileView(int x, int y, Terrain terrain, ItemKind itemKind) {
        this.x = x; this.y = y; this.terrain = terrain; this.itemKind = itemKind;
    }
}

final class Candidate {
    public final Move move;
    public final TileView tile;
    public final Path path;
    public final int dist;

    Candidate(Move move, TileView tile) {
        this.move = move; this.tile = tile;
        this.path = new Path();
        Terrain t = tile.terrain;
        if (t != null) this.path.addMove(move, t.movementCost, t.waterCost, t.energyCost);
        this.dist = Math.abs(move.dx) + Math.abs(move.dy);
    }
}

public abstract class Vision {
    // uml implementation 
    protected int xVisionRange;
    protected int yVisionRange;
    
    protected final GameState state;

    public Vision(GameState state, int xVisionRange, int yVisionRange) {
        this.state = state;
        this.xVisionRange = xVisionRange;
        this.yVisionRange = yVisionRange;
    }

    //get closest somethin or other
    public String[] getClosestFood() {
        Path p = closest(ItemKind.FOOD, 1);
        return pathToStringArray(p);
    }

    public String[] getClosestWater() {
        Path p = closest(ItemKind.WATER, 1);
        return pathToStringArray(p);
    }

    public Path closestGold()        { return closest(ItemKind.GOLD, 1); }
    public Path closestTrader()      { return closest(ItemKind.TRADER, 1); }
    public Path secondClosestFood()  { return closest(ItemKind.FOOD, 2); }
    public Path secondClosestWater() { return closest(ItemKind.WATER, 2); }
    public Path secondClosestGold()  { return closest(ItemKind.GOLD, 2); }
    public Path secondClosestTrader(){ return closest(ItemKind.TRADER, 2); }

    //easiest path!
    public Path easiestPath() {
        List<Candidate> cands = candidatesOnce();
        if (cands.isEmpty()) return new Path();
        Candidate best = cands.stream()
            .min(comparingInt((Candidate c) -> c.tile.terrain.movementCost)
                .thenComparingInt(c -> -c.tile.x))
            .orElse(null);
        return (best == null) ? new Path() : best.path;
    }

    // subclasses define allowed moves 
    protected abstract Move[] allowedMoves();

    private List<Candidate> cachedCandidates;
    private final EnumMap<ItemKind, List<Path>> rankedCache = new EnumMap<>(ItemKind.class);

    private List<Candidate> candidatesOnce() {
        if (cachedCandidates != null) return cachedCandidates;
        Player p = state.getPlayer();
        Map map = state.getMap();
        int px = p.getPosX(), py = p.getPosY();

        List<Candidate> out = new ArrayList<>();
        for (Move m : allowedMoves()) {
            int tx = px + m.dx, ty = py + m.dy;
            // Check vision range bounds
            if (Math.abs(m.dx) > xVisionRange || Math.abs(m.dy) > yVisionRange) continue;
            if (tx < 0 || ty < 0 || ty >= map.getHeight() || tx >= map.getWidth()) continue;
            Terrain t = map.getTerrain(tx, ty);
            if (t == null) continue;
            ItemKind ik = inferItemKind(t);
            out.add(new Candidate(m, new TileView(tx, ty, t, ik)));
        }
        cachedCandidates = out;
        rankedCache.clear();
        return out;
    }

    private ItemKind inferItemKind(Terrain t) {
        if (t == null) return null;
        String type = (t.getType() == null) ? "" : t.getType().toLowerCase();
        String rep  = (t.stringRep == null) ? "" : t.stringRep.toLowerCase();
        if (type.contains("food") || rep.equals("food")) return ItemKind.FOOD;
        if (type.contains("water") || rep.equals("water")) return ItemKind.WATER;
        if (type.contains("gold") || rep.equals("gold")) return ItemKind.GOLD;
        if (type.contains("trader") || rep.equals("trader")) return ItemKind.TRADER;
        return null;
    }

    private List<Path> rankedFor(ItemKind kind) {
        List<Path> cached = rankedCache.get(kind);
        if (cached != null) return cached;

        List<Candidate> pool = new ArrayList<>();
        for (Candidate c : candidatesOnce()) if (c.tile.itemKind == kind) pool.add(c);

        pool.sort(
            comparingInt((Candidate c) -> c.dist)
            .thenComparingInt(c -> c.tile.terrain.movementCost)
            .thenComparingInt(c -> -c.tile.x)
        );

        List<Path> ranked = new ArrayList<>(pool.size());
        for (Candidate c : pool) ranked.add(c.path);
        rankedCache.put(kind, ranked);
        return ranked;
    }

    private Path closest(ItemKind kind, int k) {
        List<Path> r = rankedFor(kind);
        if (k <= 0 || k > r.size()) return new Path();
        return r.get(k - 1);
    }

    // convert Path to String[] 
    private String[] pathToStringArray(Path p) {
        if (p.isEmpty()) return new String[0];
        List<Move> moves = p.getMoves();
        String[] result = new String[moves.size()];
        for (int i = 0; i < moves.size(); i++) {
            result[i] = moves.get(i).name();
        }
        return result;
    }

    // Return a list of all the visible coordinates
    public List<int[]> getVisibleCoordinates() {
        List<int[]> coords = new ArrayList<>();
        for (Candidate c : candidatesOnce()) {
            coords.add(new int[]{ c.tile.x, c.tile.y });
        }
        return coords;
    }

    protected void invalidate() {
        cachedCandidates = null;
        rankedCache.clear();
    }
}

class CautiousVision extends Vision {
    public CautiousVision(GameState state) {
        super(state, 1, 1); // xVisionRange=1, yVisionRange=1
    }

    @Override 
    protected Move[] allowedMoves() {
        return new Move[]{ 
            Move.MoveNorth, 
            Move.MoveSouth, 
            Move.MoveEast, 
            Move.MoveWest,
            Move.MoveNorthEast,
            Move.MoveNorthWest,
            Move.MoveSouthEast,
            Move.MoveSouthWest,
        };
    }
}

// Has a sight with a radius of 2
class KeenVision extends Vision {
    public KeenVision(GameState state) {
        super(state, 2, 2);
    }

    @Override
    protected Move[] allowedMoves() {
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
        super(state, 5, 5);
    }

    @Override
    protected Move[] allowedMoves() {
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
        super(state, 10, 10);
    }

    @Override
    protected Move[] allowedMoves() {
        return new Move[]{
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

            Move.MoveSouthWest,
            Move.MoveSouthWest2,
            Move.MoveSouthWest3,
            Move.MoveSouthWest4,
            Move.MoveSouthWest5,
            Move.MoveSouthWest6,
            Move.MoveSouthWest7,
            Move.MoveSouthWest8,
            Move.MoveSouthWest9,
            Move.MoveSouthWest10
        };
    } // end allowedMoves
} // end class QueenVision