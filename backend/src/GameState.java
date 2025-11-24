public class GameState {
    private static final int MAP_WIDTH = 11;
    private static final int MAP_HEIGHT = 11;

    private Map map;
    private final Player player;

    public GameState() {
        map = new Map(MAP_WIDTH, MAP_HEIGHT);
        this.player = new Player(1, 0,map);

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
        if(!player.isAlive()) return;
        // remember prev coords
        player.setPrevX(player.getPosX());
        player.setPrevY(player.getPosY());

        // move
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

        // if (player.getPosX() >= 0 && player.getPosX() < map.getWidth() &&
        //     player.getPosY() >= 0 && player.getPosY() < map.getHeight()) {
        //     player.setPosition(player.getPosX(), player.getPosY(), map);
        // }

        updateMap();
        if(player.terrainStringBuffer.equals("E")){
            System.out.println("A WINNER IS YOU");
        }
    }

    public void reset() {
        // regen the entire map
        Map newMap = new Map(MAP_WIDTH, MAP_HEIGHT);

        // reset player position
        player.setPrevX(1);
        player.setPrevY(1);
        player.setPosition(1, 1, newMap);

        // replace old map
        this.map = newMap;

        // player's new location
        updateMap();
    }

    // set an npc/item within the square
    /*public void setSquare(int x, int y, Object obj) {
        map.setSquare(x, y, obj);
    }*/

    public Map getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }
}
