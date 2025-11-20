/**
 * greedy brain prioritizes gold above all other objectives
 * takes risks to maximize gold collection
 */
public class GreedyBrain extends Brain {
    
    /**
     * creates a new greedy brain
     * @param state the current game state
     * @param vision the vision object used to analyze surroundings
     */
    public GreedyBrain(GameState state, Vision vision) {
        super(state, vision);
        setDefaultMode(false);
    }
    
    @Override
    protected Move chooseStrategy() {
        // Always be aggressive unless critically low on resources
        if (getWaterLevel() < 20 || getFoodLevel() < 20) {
            return aggressiveStrategy();
        }
        return aggressiveStrategy();
    }
    
    @Override
    protected Move aggressiveStrategy() {
        /**
         * aggressive strategy for greedy brain
         * prefer gold then traders then resources when necessary
         */
        Path goldPath = getVision().closestGold();
        if (goldPath != null && !goldPath.isEmpty()) {
            // Check if we can afford it
            if (canAffordMove(goldPath)) {
                return goldPath.getMoves().get(0);
            }
        }
        
        // Look for second closest gold
        Path secondGold = getVision().secondClosestGold();
        if (secondGold != null && !secondGold.isEmpty() && canAffordMove(secondGold)) {
            return secondGold.getMoves().get(0);
        }
        
        // Only get resources if we can't afford to move
        if (getWaterLevel() < 30) {
            String[] waterMoves = getVision().getClosestWater();
            if (waterMoves != null && waterMoves.length > 0) {
                return parseMove(waterMoves[0]);
            }
        }
        
        if (getFoodLevel() < 30) {
            String[] foodMoves = getVision().getClosestFood();
            if (foodMoves != null && foodMoves.length > 0) {
                return parseMove(foodMoves[0]);
            }
        }
        
        // Find trader to sell gold
        Path traderPath = getVision().closestTrader();
        if (traderPath != null && !traderPath.isEmpty()) {
            return traderPath.getMoves().get(0);
        }
        
        return Move.MoveEast; // Default: explore eastward
    }
    /**
     * update goal for greedy brain
     * remain focused on gold unless resources are critically low
     */
    @Override
    protected void updateGoal() {
        if (getWaterLevel() < 15 || getFoodLevel() < 15) {
            setDefaultMode(true);
        } else {
            setDefaultMode(false);
        }
    }
}
