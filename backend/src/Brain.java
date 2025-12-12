import java.util.Objects;

/**
 * Abstract decision-making component that selects a {@link Move} for the
 * associated player each turn based on the current {@link Vision} and
 * available resources.
 */
public abstract class Brain {

    protected final GameState state;
    protected final Player player;
    protected Vision vision;

    protected Brain(GameState state, Player player, Vision vision) {
        this.state = Objects.requireNonNull(state, "state");
        this.player = Objects.requireNonNull(player, "player");
        this.vision = Objects.requireNonNull(vision, "vision");
    }

    public Vision getVision() {
        return vision;
    }

    public void setVision(Vision vision) {
        this.vision = Objects.requireNonNull(vision, "vision");
    }

    /**
     * Main entry point called once per game tick to decide the player's move.
     */
    public Move makeMove() {
        // --- TRADE OFFER HANDLING ---
        TradeOffer offer = state.getActiveOffer();
        if (offer != null) {
            // If the player can afford the offer, auto-accept it and pause movement.
            if (player.getGold() >= offer.goldCost) {
                // Pay for the offer
                player.setGold(player.getGold() - offer.goldCost);
                // Apply the item effect
                offer.offeredItem.use(player);
                // Remove the trader from the tile
                Terrain tile = state.getMap().getTerrain(player.getPosX(), player.getPosY());
                tile.removeTileObject();
                tile.stringRep = player.terrainStringBuffer;
                // End the trade
                state.clearTrade();
            }
            // Pause movement this turn while trade is/was active
            return rest();
        }
        // High-level template: survival > goal > exploration.
        if (isInSurvivalState()) {
            return chooseSurvivalStrategy();
        }
        if (isGoalVisible()) {
            return chooseGoalStrategy();
        }
        return chooseExploreStrategy();
    }

    // ---------------------------------------------------------------------
    // Template decision helpers
    // ---------------------------------------------------------------------

    /** True if the player is low on vital resources and should prioritize survival. */
    protected boolean isInSurvivalState() {
    // Consider the player in a survival state when water or energy is
    // getting low relative to the legacy max of 100.
    double water = getWaterLevel();
    double food = getFoodLevel();
    return water < 25 || food < 25;
    }

    /** Returns true if the goal tile is visible in vision. */
    protected boolean isGoalVisible() {
        // Use the new Vision API: a dedicated goal tile if visible.
        return vision.getGoalTile() != null;
    }

    /**
     * Strategy method used when the brain is exploring for resources or better
     * terrain. Subclasses must provide a finite, deterministic decision.
     */
    protected abstract Move chooseExploreStrategy();

    /**
     * Strategy method used when the player is in a survival state (low on
     * food or water).
     */
    protected abstract Move chooseSurvivalStrategy();

    /**
     * Strategy method used when the goal or eastern edge is visible.
     */
    protected abstract Move chooseGoalStrategy();

    // ---------------------------------------------------------------------
    // Resource access helpers (placeholder APIs)
    // ---------------------------------------------------------------------

    protected double getWaterLevel() {
        return player.getWater();
    }

    protected double getFoodLevel() {
        return player.getEnergy();
    }

    protected double getMovementPoints() {
        return player.getEnergy();
    }

    /**
     * Applies the "rest" rule: staying in place regains movement points while
     * still consuming a reduced amount of food and water.
     */
    protected Move rest() {
    // Rule is implemented by game engine; here we choose a no-op-ish move.
    // If a dedicated STAY move exists in the enum, you can switch back to it.
    return Move.MoveNorth;
    }
}
