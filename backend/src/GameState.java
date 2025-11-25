public class GameState {
    private static final int MAP_WIDTH = 11;
    private static final int MAP_HEIGHT = 11;

    private Map map;
    private final Player player;

    private int level;
    private int playerTurnInterval;

    public GameState() {
        map = new Map(MAP_WIDTH, MAP_HEIGHT);
        this.player = new Player(1, 0,map);

        this.level = 0;
        this.playerTurnInterval = 0;

        player.terrainStringBuffer = map.getTerrain(player.getPosX(), player.getPosY()).stringRep;

        updateMap();
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
        
        // check player resources and update map status 
        player.resourceCheck();
        updateMap();

        if (player.terrainStringBuffer.equals("E")){
            player.setOnGoalTile(true);
        }
    }

    public void reset() {
        // regen the entire map
        Map newMap = new Map(MAP_WIDTH, MAP_HEIGHT);

        // reset player position
        player.setPrevX(1);
        player.setPrevY(0);
        player.setPosition(1, 0, newMap);

        // reset player stats
        player.setHP(100.0);
        player.setWater(100);
        player.setEnergy(100);
        player.setGold(0);
        player.setOnGoalTile(false);

        // replace old map
        this.map = newMap;

        // player's new location
        resetLevel();
        updateMap();
    }

    public void nextLevel() {
        // regen the map
        Map newMap = new Map(MAP_WIDTH, MAP_HEIGHT);

        // reset player position
        player.setPrevX(1);
        player.setPrevY(0);
        player.setPosition(1, 0, newMap);

        // reset player win condition
        player.setOnGoalTile(false);

        // replace old map
        this.map = newMap;

        // player's new location
        incrementLevel();
        updateMap();
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
        this.level = 0;
    }
}
