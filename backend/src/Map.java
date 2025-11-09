import java.util.Random;
public class Map {
    private final int width;
    private final int height;
    private final String[][] tiles;
    
    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new String[height][width];
        fill('.');
        buildWalls();
    }

	public Terrain[][]generateMap(int length, int height){
		if(length<=0||height<=0) {
			System.out.println("Invalid map dimensions, generating random valid dimensions for map");
			Random r = new Random();
			length = r.nextInt(255)+1;
			height = r.nextInt(255)+1;
		}
		Terrain[][]map = new Terrain[height][length];
		return map;
	}

    private void fill(char ch) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x] = String.valueOf(ch);
            }
        }
    }

    private void buildWalls() {
        // top and bottom
        for (int x = 0; x < width; x++) {
            tiles[0][x] = "#";
            tiles[height - 1][x] = "#";
        }

        // sides
        for (int y = 0; y < height; y++) {
            tiles[y][0] = "#";
            tiles[y][width - 1] = "#";
        }
    }

    public void updateWithPlayer(Player player) {
        fill('.');
        buildWalls();
        tiles[player.getPosY()][player.getPosX()] = "P";
    }

    // set npc/item within square
    public void setSquare(int x, int y, Object obj) {
        if (y < 0 || y >= height || x < 0 || x >= width) return;
        tiles[y][x] = obj.toString();
    }

    public int getWidth() { 
        return width; 
    }

    public int getHeight() { 
        return height; 
    }
}
