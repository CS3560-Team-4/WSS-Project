public class GameState {
    private static final int MAP_WIDTH = 10;
    private static final int MAP_HEIGHT = 10;

    private final String[][] map;
    private final Player player;

    public GameState() {
        this.map = new String[MAP_HEIGHT][MAP_WIDTH];
        this.player = new Player(1, 1);
        updateMap();
    }

    private void fill(char ch) {
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                map[y][x] = String.valueOf(ch);
            }
        }
    }

    private void buildWalls() {
        // top and bottom
        for (int x = 0; x < MAP_WIDTH; x++) {
            map[0][x] = "#";
            map[MAP_HEIGHT - 1][x] = "#";
        }

        for (int y = 0; y < MAP_HEIGHT; y++) {
            map[y][0] = "#";
            map[y][MAP_WIDTH - 1] = "#";
        }
    }

    public void updateMap() {
        fill('.');
        buildWalls();
        // player positon
        map[player.getPosY()][player.getPosX()] = "P";
    }

    public void initializePlayerPos(int x, int y) {
        // clamp inside walkable area
        int px = Math.max(1, Math.min(MAP_WIDTH - 2, x));
        int py = Math.max(1, Math.min(MAP_HEIGHT -2, y));
        player.setPosition(px, py);
    }

    public void movePlayer(String dir) {
        if (player == null) return;

        switch (dir == null ? "" : dir) {
            case "up" -> player.setPosY(Math.max(1, player.getPosY() - 1));
            case "down" -> player.setPosY(Math.min(MAP_HEIGHT - 2, player.getPosY() + 1));
            case "left" -> player.setPosX(Math.max(1, player.getPosX() - 1));
            case "right" -> player.setPosX(Math.min(MAP_WIDTH - 2, player.getPosX() + 1));
            default -> { /* ignore */ }
        }

        updateMap();
    }

    // set an npc/item within the square
    public void setSquare(int x, int y, Object obj) {
        if (y < 0 || y >= MAP_HEIGHT || x < 0 || x >= MAP_WIDTH) return;
        map[x][y] = obj.toString();
    }

    public String[][] getMap() {
        return map;
    }
}
