/**
 * BalancedBrain - Maintains balance between all goals.
 * Uses the default Brain behavior but with fine-tuned thresholds.
 */
public class BalancedBrain extends Brain {
    
    private static final int IDEAL_WATER = 60;
    private static final int IDEAL_FOOD = 60;
    private static final int MIN_SAFE_WATER = 40;
    private static final int MIN_SAFE_FOOD = 40;
    
    /**
     * creates a new balanced brain with fine tuned thresholds
     * @param state the current game state
     * @param vision the vision object used to analyze surroundings
     */
    public BalancedBrain(GameState state, Vision vision) {
        super(state, vision);
    }

    /**
     * chooses a strategy that balances resource management and goals
     * prefers restoring deficits before pursuing gold
     * @return the chosen move
     */
    @Override
    protected Move chooseStrategy() {
        // Balance resources first, then pursue goals
        
        // Check if resources are unbalanced
        int waterDeficit = IDEAL_WATER - getWaterLevel();
        int foodDeficit = IDEAL_FOOD - getFoodLevel();
        
        // Prioritize whichever is more needed
        if (waterDeficit > foodDeficit && waterDeficit > 20) {
            String[] waterMoves = getVision().getClosestWater();
            if (waterMoves != null && waterMoves.length > 0) {
                return parseMove(waterMoves[0]);
            }
        } else if (foodDeficit > 20) {
            String[] foodMoves = getVision().getClosestFood();
            if (foodMoves != null && foodMoves.length > 0) {
                return parseMove(foodMoves[0]);
            }
        }
        
        // If balanced, pursue gold
        if (getWaterLevel() >= MIN_SAFE_WATER && getFoodLevel() >= MIN_SAFE_FOOD) {
            Path goldPath = getVision().closestGold();
            if (goldPath != null && !goldPath.isEmpty() && canAffordMove(goldPath)) {
                return goldPath.getMoves().get(0);
            }
        }
        
        // Take easiest available path
        Path easiest = getVision().easiestPath();
        if (!easiest.isEmpty()) {
            return easiest.getMoves().get(0);
        }
        
        return Move.MoveNorth;
    }
    
    @Override
    protected void updateGoal() {
        int waterLevel = getWaterLevel();
        int foodLevel = getFoodLevel();
        
        // Dynamic goal setting based on balanced thresholds
        if (waterLevel < MIN_SAFE_WATER || foodLevel < MIN_SAFE_FOOD) {
            setDefaultMode(true);
        } else if (waterLevel >= IDEAL_WATER && foodLevel >= IDEAL_FOOD) {
            setDefaultMode(false);
        }
        // Otherwise maintain current mode
    }
}
