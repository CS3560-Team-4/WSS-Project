public class GameState {
    private static final int MAP_WIDTH = 11;
    private static final int MAP_HEIGHT = 11;

    private final Map map;
    private final Player player;

    public GameState() {
        map = new Map(MAP_WIDTH,MAP_HEIGHT);
        this.player = new Player(1, 1,map);
        updateMap();
    }

    public void updateMap() {
        map.updateWithPlayer(player);
    }

    public void initializePlayerPos(int x, int y) {
        // clamp inside walkable area
        int px = Math.max(1, Math.min(map.getWidth() - 2, x));
        int py = Math.max(1, Math.min(map.getHeight() - 2, y));
        player.setPosition(px, py,map);
    }

    public void movePlayer(String dir) {
        if (player == null) return;

        map.getTerrain(player.getPosX(),player.getPosY()).stringRep = player.terrainStringBuffer;
        switch (dir == null ? "" : dir) {
            case "up" -> player.setPosY(Math.max(1, player.getPosY() - 1));
            case "down" -> player.setPosY(Math.min(map.getHeight() - 2, player.getPosY() + 1));
            case "left" -> player.setPosX(Math.max(1, player.getPosX() - 1));
            case "right" -> player.setPosX(Math.min(map.getWidth() - 2, player.getPosX() + 1));
            default -> {  /*ignore*/  }
        }

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
