import java.util.Random;

public class GameState {
    private static final int MAP_WIDTH = 11;
    private static final int MAP_HEIGHT = 11;

    private Map map;
    private final Player player;

    private int level;
    private int highScore;
    private int currentScore;
    private int playerTurnInterval;

    private Trader activeTrader;
    private TradeOffer activeOffer;

    public GameState() {
        map = new Map(MAP_WIDTH, MAP_HEIGHT);
        this.player = new Player(0, 0,map);
        player.setVision(new Vision(this)); // Use new simple Vision class

        this.level = 1;
        this.highScore = 0;
        this.currentScore = 0;
        this.playerTurnInterval = 0;

        player.terrainStringBuffer = map.getTerrain(player.getPosX(), player.getPosY()).stringRep;

        updateMap();
        spawnTileObjects();
    }

    public void updateMap() {
        map.updateWithPlayer(player);
    }

    public void initializePlayerPos(int x, int y) {
        // clamp inside walkable area
        int px = Math.max(1, Math.min(map.getWidth() - 2, x));
        int py = Math.max(1, Math.min(map.getHeight() - 2, y));
        player.setPosition(px, py, map);
    }

    public void spawnTileObjects() {
        ItemType[] itemTypes = {
            ItemType.WATER_BOTTLE,
            ItemType.MEDICINE,
            ItemType.ENERGY_DRINK,
            ItemType.TURKEY
        };

        TraderType[] traderTypes = {
            TraderType.Friendly,
            TraderType.Generous,
            TraderType.Greedy,
            TraderType.Lowballer
        };

        MoodState[] moodStates = {
            MoodState.Annoyed,
            MoodState.Calm,
            MoodState.Happy
        };

        String[] traderNames = {
            "Albert", "Samuel", "John", "David", "Johanssen",
            "Neil", "Joe", "Peter", "Youseff", "Jack"
        };

        // loop through all terrain tile objects
        // then randomize if they get a tile object
        // then randomize on what they get inside
        Terrain[][] terrainMap = map.getBoard();
        Terrain playerTile = terrainMap[player.getPosY()][player.getPosX()];

        Random r = new Random();

        for (Terrain[] t : terrainMap) {
            for (Terrain terrain : t) {
                // skip player tile
                if (terrain == playerTile) continue;

                // skip goal tile
                if ("E".equals(terrain.stringRep)) continue;

                if (r.nextInt(100) >= 94) {
                    if (r.nextInt(100) <= 79) {
                        Item item = new Item(itemTypes[r.nextInt(itemTypes.length)]);
                        terrain.setTileObject(item);
                    } else {
                        Trader trader = new Trader(
                            traderNames[r.nextInt(traderNames.length)],
                            traderTypes[r.nextInt(traderTypes.length)],
                            moodStates[r.nextInt(moodStates.length)],
                            r.nextInt(100) + 1
                        );
                        terrain.setTileObject(trader);
                    }
                } 
            }
        }
    }

    public void movePlayer(String dir) {
        if (player == null) return;

        // remember prev coords
        player.setPrevX(player.getPosX());
        player.setPrevY(player.getPosY());
        
        // move
        System.out.println("WATER  "+ player.getWater() +" ENERGY  "+ player.getEnergy() + " HP " + player.getHP());
        switch (dir == null ? "" : dir) {
            case "up" -> {
                if (player.getPosY() > 0) player.moveUp(this.map);
            }
            case "down" -> {
                if (player.getPosY() < map.getHeight() - 1) player.moveDown(this.map);
            }
            case "left" -> {
                if (player.getPosX() > 0) player.moveLeft(this.map);
            }
            case "right" -> {
                if (player.getPosX() < map.getWidth() - 1) player.moveRight(this.map);
            }
            default -> {  /*ignore*/  }
        }

        // for every 5 turns, player will get +1 gold
        playerTurnInterval++;
        if (playerTurnInterval >= 5) {
            player.incrementGoldBy(1);
            playerTurnInterval = 0;
        }
        
        // check player resources, update map status 
        // check for tile events
        // invalidate and recalculate vision
        player.resourceCheck();
        updateMap();
        checkForTileObject();
        calculateCurrentScore();
        player.getVision().invalidate();

        if (player.terrainStringBuffer.equals("E")){
            player.setOnGoalTile(true);
        }
    }

    public void reset() {
        // regen the entire map
        Map newMap = new Map(MAP_WIDTH, MAP_HEIGHT);

        // reset player position
        player.setPrevX(0);
        player.setPrevY(0);
        player.setPosition(0, 0, newMap);

        // reset player stats
        player.resetPlayerResources();

        // reset win condition
        player.setOnGoalTile(false);

        // reset player trade condition
        player.setOnTraderTile(false);

        // reset player consumption stats
        player.resetConsumedStats();

        // reset player visible tiles
        player.getVision().invalidate();

        // replace old map
        this.map = newMap;

        resetLevel();
        setHighScore();
        resetCurrentScore();

        updateMap();
        spawnTileObjects();
    }

    public void nextLevel() {
        // regen the map
        Map newMap = new Map(MAP_WIDTH, MAP_HEIGHT);

        // reset player position
        player.setPrevX(0);
        player.setPrevY(0);
        player.setPosition(0, 0, newMap);

        // reset player win condition
        player.setOnGoalTile(false);

        // reset player trade condition
        player.setOnTraderTile(false);

        // reset player visible tiles
        player.getVision().invalidate();

        // replace old map
        this.map = newMap;

        incrementLevel();
        updateMap();
        spawnTileObjects();
    }

    public Map getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }

    public void incrementLevel() {
        this.level++;
    }

    public int getLevel() {
        return this.level;
    }

    public void resetLevel() {
        this.level = 1;
    }

    public void resetCurrentScore() {
        this.currentScore = 0;
    }

    public int getCurrentScore() {
        return this.currentScore;
    }

    public void calculateCurrentScore() {
        this.currentScore = 
            ((this.level) * 100) + 
            (player.getGold() * 3) + 
            (player.getEnergyDrinkConsumed() * 2) +
            (player.getTurkeyConsumed() * 3) +
            (player.getMedicineConsumed() * 5) +
            (player.getWaterBottleConsumed() * 4);
    }

    public void setHighScore() {
        if (this.currentScore > this.highScore) {
            this.highScore = this.currentScore;
        }
    }

    public int getHighScore() {
        return this.highScore;
    }

    public boolean checkForTileObject() {
        Terrain currentTerrain = map.getTerrain(player.getPosX(), player.getPosY());

        if (currentTerrain == null) {
            return false;
        }

        if (currentTerrain.getTileObject() instanceof Item item) {
            handleItemEvent(item, currentTerrain);
            return true;

        } else if (currentTerrain.getTileObject() instanceof Trader trader) {
            handleTraderEvent(trader, currentTerrain);
            return true;
        }

        return false;
    }

    public void handleItemEvent(Item item, Terrain terrain) {
        item.use(player);
        terrain.removeTileObject();
    }

    public void handleTraderEvent(Trader trader, Terrain terrain) {
        activeTrader = trader;
        activeOffer = trader.generateOffer(player); // create an offer
    }

    public Trader getActiveTrader() {
        return this.activeTrader;
    }

    public TradeOffer getActiveOffer() {
        return this.activeOffer;
    }

    public void clearTrade() {
        this.activeOffer = null;
        this.activeTrader = null;
    }
}
