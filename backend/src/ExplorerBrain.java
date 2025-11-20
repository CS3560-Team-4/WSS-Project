/**
 * explorer brain seeks to explore new areas
 * tries to visit unvisited tiles and maximize map coverage
 */
public class ExplorerBrain extends Brain {
    
    /**
     * creates a new explorer brain
     * @param state the current game state
     * @param vision the vision object used to analyze surroundings
     */
    public ExplorerBrain(GameState state, Vision vision) {
        super(state, vision);
    setDefaultMode(false);
    }
    
    /**
     * chooses a strategy that prioritizes exploration
     * maintains minimum resources then explores
     * @return the chosen move
     */
    @Override
    protected Move chooseStrategy() {
        if (getWaterLevel() < 35) {
            String[] waterMoves = getVision().getClosestWater();
            if (waterMoves != null && waterMoves.length > 0) {
                return parseMove(waterMoves[0]);
            }
        }
        
        if (getFoodLevel() < 35) {
            String[] foodMoves = getVision().getClosestFood();
            if (foodMoves != null && foodMoves.length > 0) {
                return parseMove(foodMoves[0]);
            }
        }
        
        return exploreNewDirection();
    }
    
    /**
     * choose a move that explores new territory
     * @return an exploratory move
     */
    private Move exploreNewDirection() {
        Player player = getPlayer();
        Map map = getState().getMap();
        
        // Prefer moves toward unexplored edges
        int px = player.getPosX();
        int py = player.getPosY();
        int width = map.getWidth();
        int height = map.getHeight();
        
        // Calculate distances to edges
        int distToEast = width - px;
        int distToWest = px;
        int distToSouth = height - py;
        int distToNorth = py;
        
        // Find furthest edge and move toward it
        int maxDist = Math.max(Math.max(distToEast, distToWest), 
                              Math.max(distToSouth, distToNorth));
        
        if (maxDist == distToEast && distToEast > 2) {
            return Move.MoveEast;
        } else if (maxDist == distToWest && distToWest > 2) {
            return Move.MoveWest;
        } else if (maxDist == distToSouth && distToSouth > 2) {
            return Move.MoveSouth;
        } else if (maxDist == distToNorth && distToNorth > 2) {
            return Move.MoveNorth;
        }
        
        // If in center, use diagonal moves for efficiency
        return Move.MoveNorthEast;
    }
    
    @Override
    protected void updateGoal() {
        // Always in exploration mode unless critical
        if (getWaterLevel() < 25 || getFoodLevel() < 25) {
            aggressiveStrategy();
        } else {
            setDefaultMode(false);
        }
    }
}
