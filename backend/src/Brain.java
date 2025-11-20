/**
 * abstract base class for ai decision making
 * makes decisions for the player based on vision input
 * uses vision to analyze the game state and determine the best move
 * tracks player resources and makes strategic decisions
 * extend this class to create different brain types with unique strategies
 * 
 * @author karlos
 */
public abstract class Brain {
    /** vision object used to analyze surroundings */
    private Vision vision;
    /** current game state */
    private GameState state;
    /** reference to the player object */
    private Player player;
    
    /** current water level 0 to 100 */
    private int waterLevel;
    /** current food level 0 to 100 */
    private int foodLevel;
    /** amount of gold collected */
    private int goldAmount;
    /** remaining movement points */
    private int movementPoints;
    
    /** flag indicating if brain is in default mode
     * we should add a liberal mode!!!
     */
    private boolean defaultMode;
    /** current goal such as survive gather or explore */
    private String currentGoal;
    
    /**
     * creates a new brain with initial resources
     * @param state the current game state
     * @param vision the vision object to analyze surroundings
     */
    public Brain(GameState state, Vision vision) {
        this.state = state;
        this.vision = vision;
        this.player = state.getPlayer();
        
        // Initialize resources
        this.waterLevel = 100;
        this.foodLevel = 100;
        this.goldAmount = 0;
        this.movementPoints = 100;
        
        // Default strategy
        this.defaultMode = true;
        this.currentGoal = "SURVIVE";
    }
    
    /**
     * decides the next move based on current priorities and resource levels
     * subclasses can override this to implement different decision strategies
     * @return the chosen move direction
     */
    public Move decideMove() {
        // update resource-based priorities
        updateGoal();
        
        // critical water level - highest priority
        if (waterLevel < 30) {
            String[] waterMoves = vision.getClosestWater();
            if (waterMoves != null && waterMoves.length > 0) {
                return parseMove(waterMoves[0]);
            }
        }
        
        // Critical food level - second priority
        if (foodLevel < 30) {
            String[] foodMoves = vision.getClosestFood();
            if (foodMoves != null && foodMoves.length > 0) {
                return parseMove(foodMoves[0]);
            }
        }
        
        // Delegate to strategy method (can be overridden)
        return chooseStrategy();
    }
    
    /**
     * chooses which strategy to use based on current mode
     * override this in subclasses for different behaviors
     * aggresive strategy prioritizes gold, traders, and exploration.
     * @return the chosen move
     */
    protected Move chooseStrategy() {
        if (defaultMode) {
            return defaultStrategy();
        } else {
            return aggressiveStrategy();
        }
    }
    
    /**
     * default strategy prioritizes survival over goals
     * maintains high resource levels before pursuing objectives
     * can be overridden by subclasses
     * @return the chosen move
     */
    protected Move defaultStrategy() {
        // Maintain resources before seeking gold
        if (waterLevel < 60) {
            String[] waterMoves = vision.getClosestWater();
            if (waterMoves != null && waterMoves.length > 0) {
                return parseMove(waterMoves[0]);
            }
        }
        
        if (foodLevel < 60) {
            String[] foodMoves = vision.getClosestFood();
            if (foodMoves != null && foodMoves.length > 0) {
                return parseMove(foodMoves[0]);
            }
        }
        
        // If resources are good, look for gold
        Path goldPath = vision.closestGold();
        if (goldPath != null && !goldPath.isEmpty()) {
            return goldPath.getMoves().get(0);
        }
        
        // Take easiest/cheapesst path if nothing urgent
        Path easiest = vision.easiestPath();
        if (!easiest.isEmpty()) {
            return easiest.getMoves().get(0);
        }
        
        return Move.MoveNorth;
    }
    
    /**
     * aggressive strategy prioritizes gold and exploration over safety
     * takes more risks to maximize rewards
     * can be overridden by subclasses
     * @return the chosen move
     */
    protected Move aggressiveStrategy() {
        // Go for gold first if resources aren't critical
        Path goldPath = vision.closestGold();
        if (goldPath != null && !goldPath.isEmpty()) {
            return goldPath.getMoves().get(0);
        }
        
        // Look for trader
        Path traderPath = vision.closestTrader();
        if (traderPath != null && !traderPath.isEmpty()) {
            return traderPath.getMoves().get(0);
        }
        
        // Get resources only when needed
        if (waterLevel < 50) {
            String[] waterMoves = vision.getClosestWater();
            if (waterMoves != null && waterMoves.length > 0) {
                return parseMove(waterMoves[0]);
            }
        }
        
        if (foodLevel < 50) {
            String[] foodMoves = vision.getClosestFood();
            if (foodMoves != null && foodMoves.length > 0) {
                return parseMove(foodMoves[0]);
            }
        }
        
        return Move.MoveNorth;
    }
    
    /**
     * updates current goal based on resource levels
     * switches between survive gather and explore modes
     * can be overridden for different goal setting logic
     */
    protected void updateGoal() {
        if (waterLevel < 30 || foodLevel < 30) {
            currentGoal = "SURVIVE";
            defaultMode = true;
        } else if (waterLevel > 70 && foodLevel > 70) {
            currentGoal = "EXPLORE";
            defaultMode = false;
        } else {
            currentGoal = "GATHER";
            defaultMode = true;
        }
    }
    
    /**
     * consumes resources after executing a move
     * deducts movement water and food costs from current levels
     * @param moveCost the movement cost of the terrain
     * @param waterCost the water cost of the terrain
     * @param foodCost the food cost of the terrain
     */
    public void consumeResources(int moveCost, int waterCost, int foodCost) {
        movementPoints -= moveCost;
        waterLevel -= waterCost;
        foodLevel -= foodCost;
        
        // Ensure resources don't go negative
        if (movementPoints < 0) movementPoints = 0;
        if (waterLevel < 0) waterLevel = 0;
        if (foodLevel < 0) foodLevel = 0;
    }
    
    /**
     * replenishes water resource up to maximum of 100
     * usually called when we pick up water
     * @param amount amount of water to add
     */
    public void addWater(int amount) {
        waterLevel = Math.min(100, waterLevel + amount);
    }
    
    /**
     * replenishes food resource up to maximum of 100
     * usually called when we pick up food
     * @param amount amount of food to add
     */
    public void addFood(int amount) {
        foodLevel = Math.min(100, foodLevel + amount);
    }
    
    /**
     * adds gold to the inventory
     * usually called when we pick up gold
     * @param amount amount of gold to add
     */
    public void addGold(int amount) {
        goldAmount += amount;
    }
    
    /**
     * executes a move and updates resources based on terrain costs
     * invalidates vision cache after the move
     * @param move the move to execute
     */
    public void executeMove(Move move) {
        // Get terrain at target location
        int px = player.getPosX();
        int py = player.getPosY();
        int tx = px + move.dx;
        int ty = py + move.dy;
        
        Map map = state.getMap();
        if (tx >= 0 && ty >= 0 && tx < map.getWidth() && ty < map.getHeight()) {
            Terrain terrain = map.getTerrain(tx, ty);
            if (terrain != null) {
                consumeResources(terrain.movementCost, terrain.waterCost, terrain.energyCost);
            }
        }
        
        // Invalidate vision cache after move
        vision.invalidate();
    }
    
    /**
     * parses a move name string to a move enum value
     * protected so subclasses can use this helper method
     * @param moveName the name of the move such as movenorth
     * @return the corresponding move enum value
     */
    protected Move parseMove(String moveName) {
        return switch (moveName) {
            case "MoveNorth" -> Move.MoveNorth;
            case "MoveSouth" -> Move.MoveSouth;
            case "MoveEast" -> Move.MoveEast;
            case "MoveWest" -> Move.MoveWest;
            case "MoveNorthEast" -> Move.MoveNorthEast;
            case "MoveNorthWest" -> Move.MoveNorthWest;
            case "MoveSouthEast" -> Move.MoveSouthEast;
            case "MoveSouthWest" -> Move.MoveSouthWest;
            default -> Move.MoveNorth;
        };
    }
    
    /**
     * gets the current vision object
     * @return the vision object
     */
    public Vision getVision() {
        return vision;
    }
    
    /**
     * updates the vision object after state changes
     * @param vision the new vision object
     */
    public void setVision(Vision vision) {
        this.vision = vision;
    }
    
    /**
     * gets the current game state
     * @return the current game state
     */
    public GameState getState() {
        return state;
    }
    
    /**
     * gets the current water level
     * @return water level from 0 to 100
     */
    public int getWaterLevel() {
        return waterLevel;
    }
    
    /**
     * sets the water level clamped between 0 and 100
     * @param waterLevel new water level
     */
    public void setWaterLevel(int waterLevel) {
        this.waterLevel = Math.max(0, Math.min(100, waterLevel));
    }
    
    /**
     * gets the current food level
     * @return food level from 0 to 100
     */
    public int getFoodLevel() {
        return foodLevel;
    }
    
    /**
     * sets the food level clamped between 0 and 100
     * @param foodLevel new food level
     */
    public void setFoodLevel(int foodLevel) {
        this.foodLevel = Math.max(0, Math.min(100, foodLevel));
    }
    
    /**
     * gets the current gold amount
     * @return gold amount
     */
    public int getGoldAmount() {
        return goldAmount;
    }
    
    /**
     * sets the gold amount to a non negative value
     * @param goldAmount new gold amount
     */
    public void setGoldAmount(int goldAmount) {
        this.goldAmount = Math.max(0, goldAmount);
    }
    
    /**
     * gets the remaining movement points
     * @return movement points remaining
     */
    public int getMovementPoints() {
        return movementPoints;
    }
    
    /**
     * sets the movement points to a non negative value
     * @param movementPoints new movement points
     */
    public void setMovementPoints(int movementPoints) {
        this.movementPoints = Math.max(0, movementPoints);
    }
    
    /**
     * checks if brain is in default mode
     * @return true if default false if aggressive
     */
    public boolean isdefaultMode() {
        return defaultMode;
    }
    
    /**
     * sets the strategy mode
     * @param defaultMode true for default false for aggressive
     */
    public void setDefaultMode(boolean defaultMode) {
        this.defaultMode = defaultMode;
    }
    
    /**
     * gets the current goal
     * @return current goal string such as survive gather or explore
     */
    public String getCurrentGoal() {
        return currentGoal;
    }
    
    /**
     * gets the player reference
     * @return the player object
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * checks if brain is in a critical state
     * @return true if resources are critically low
     */
    public boolean isCriticalState() {
        return waterLevel < 20 || foodLevel < 20 || movementPoints < 10;
    }
    
    /**
     * checks if brain has enough resources to afford a move
     * @param path the path to check
     * @return true if resources are sufficient
     */
    public boolean canAffordMove(Path path) {
        return waterLevel >= path.getWaterCost() 
            && foodLevel >= path.getFoodCost() 
            && movementPoints >= path.getMovementCost();
    }
    
    /**
     * resets all resources to maximum values
     */
    public void resetResources() {
        waterLevel = 100;
        foodLevel = 100;
        movementPoints = 100;
    }
}
