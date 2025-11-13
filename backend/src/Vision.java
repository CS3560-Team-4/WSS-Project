import java.util.*;
import static java.util.Comparator.comparingInt;

// a lot of this code is using temporary solutions until
// itme classes and trader classes are implemented
// i just wanted to have at least something for now. 
// Directions, i love enums. we can prob change this later 

enum Direction {
    N(0,-1, Move.MoveNorth),
    S(0, 1, Move.MoveSouth),
    E(1, 0, Move.MoveEast),
    W(-1,0, Move.MoveWest),
    NE(1,-1, Move.MoveNorthEast),
    NW(-1,-1, Move.MoveNorthWest),
    SE(1, 1, Move.MoveSouthEast),
    SW(-1, 1, Move.MoveSouthWest);

    final int dx, dy;
    final Move move;
    Direction(int dx, int dy, Move move) { this.dx = dx; this.dy = dy; this.move = move; }
}

enum Target { FOOD, WATER, GOLD, TRADER }

//visible tile the player can see, can be multiple tiles depending on vision type
final class Candidate {
    final Direction dir;
    final int tx, ty;
    final Terrain terrain;
    final Path path;  
    final int dist;     
    Candidate(Direction dir, int tx, int ty, Terrain t) {
        this.dir = dir; this.tx = tx; this.ty = ty; this.terrain = t;
        this.path = new Path();
        this.path.addMove(dir.move, t.movementCost, t.waterCost, t.energyCost);
        this.dist = Math.abs(dir.dx) + Math.abs(dir.dy);
    }
}

public abstract class Vision {
    protected final GameState state;

    public Vision(GameState state) { this.state = state; }

    public Path closestFood()        { return closest(Target.FOOD, 1); }
    public Path closestWater()       { return closest(Target.WATER, 1); }
    public Path closestGold()        { return closest(Target.GOLD, 1); }
    public Path closestTrader()      { return closest(Target.TRADER, 1); }
    public Path secondClosestFood()  { return closest(Target.FOOD, 2); }
    public Path secondClosestWater() { return closest(Target.WATER, 2); }
    public Path secondClosestGold()  { return closest(Target.GOLD, 2); }
    public Path secondClosestTrader(){ return closest(Target.TRADER, 2); }

    //easiest path means the path with the lowest movement cost
    public Path easiestPath() {
        List<Candidate> cands = candidatesOnce();
        if (cands.isEmpty()) return new Path();
        Candidate best = cands.stream()
            .min(comparingInt((Candidate c) -> c.terrain.movementCost)
                .thenComparingInt(c -> -c.tx)) 
            .orElse(null);
        return (best == null) ? new Path() : best.path;
    }

    //what directions can we see in?
    //can override allowed directions in subclasses for each sight type
    //NE/SE/NW/SW for diagonal vision but there might be a better way for directions
    protected abstract Direction[] allowedDirections();

    private List<Candidate> cachedCandidates;
    private final EnumMap<Target, List<Path>> rankedCache = new EnumMap<>(Target.class);

    //constantly check for next candidates that are visible
    private List<Candidate> candidatesOnce() {
        if (cachedCandidates != null) return cachedCandidates;

        Player p = state.getPlayer();
        Map map = state.getMap();
        int px = p.getPosX(), py = p.getPosY();

        List<Candidate> out = new ArrayList<>();
        for (Direction d : allowedDirections()) {
            int tx = px + d.dx, ty = py + d.dy;
            if (tx < 0 || ty < 0 || ty >= map.getHeight() || tx >= map.getWidth()) continue;
            Terrain t = map.getTerrain(tx, ty);
            if (t == null) continue;
            out.add(new Candidate(d, tx, ty, t));
        }
        cachedCandidates = out;
        rankedCache.clear();
        return out;
    }

    //checks if the tile has items or actors
    private boolean matches(Target target, Terrain t) {
        // Prefer a typed enum like t.kind. fallback to type/rep string checks
        String type = (t.getType() == null) ? "" : t.getType().toLowerCase();
        String rep  = (t.stringRep == null) ? "" : t.stringRep.toLowerCase();
        switch (target) {
            case FOOD:   return type.contains("food")   || rep.equals("food");
            case WATER:  return type.contains("water")  || rep.equals("water");
            case GOLD:   return type.contains("gold")   || rep.equals("gold");
            case TRADER: return type.contains("trader") || rep.equals("trader");
            default: return false;
        }
    }

    //sort tile based on certain criteria, finding the most relevant tiles 
    private List<Path> rankedFor(Target target) {
        List<Path> cached = rankedCache.get(target);
        if (cached != null) return cached;

        Player p = state.getPlayer(); // if east preference ever depends on player state
        List<Candidate> pool = new ArrayList<>();
        for (Candidate c : candidatesOnce()) {
            if (matches(target, c.terrain)) pool.add(c);
        }

        // Sort by: distance (asc), movement cost (asc), furthest east (tx desc)
        pool.sort(
            comparingInt((Candidate c) -> c.dist)
            .thenComparingInt(c -> c.terrain.movementCost)
            .thenComparingInt(c -> -c.tx)
        );

        List<Path> ranked = new ArrayList<>(pool.size());
        for (Candidate c : pool) ranked.add(c.path);
        rankedCache.put(target, ranked);
        return ranked;
    }

    // K-th best (1-based) or empty if not present
    private Path closest(Target target, int k) {
        List<Path> r = rankedFor(target);
        if (k <= 0 || k > r.size()) return new Path();
        return r.get(k - 1);
    }

    //call when palayer moves, clear cached data
    protected void invalidate() {
        cachedCandidates = null;
        rankedCache.clear();
    }
}

// cautious vision that can only see immediately N, E, S, W
class CautiousVision extends Vision {
    public CautiousVision(GameState state) { super(state); }

    @Override protected Direction[] allowedDirections() {
        return new Direction[]{ Direction.N, Direction.S, Direction.E, Direction.W };
    }
}
