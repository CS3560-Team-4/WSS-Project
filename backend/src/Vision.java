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

    protected void invalidate() {
        cachedCandidates = null;
        rankedCache.clear();
    }
}

class CautiousVision extends Vision {
    public CautiousVision(GameState state) {
        super(state, 1, 1); // xVisionRange=1, yVisionRange=1
    }
    @Override protected Move[] allowedMoves() {
        return new Move[]{ Move.MoveNorth, Move.MoveSouth, Move.MoveEast, Move.MoveWest };
    }
}
