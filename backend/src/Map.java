import java.util.Random;
public class Map {
    Terrain[][] map;
    
    //to access height, use map.getHeight()
    //to access width, use map.getWidth() such that i is a non-negative int

    //This constructor makes a matrix of terrain objects to form our map, accounts for 
    //invalid inputs, initially the matrix has null objects
    public Map(int width, int height) {
        Random r = new Random();

        if (width <=0 || height <= 0) {
			System.out.println("Invalid map dimensions, generating random valid dimensions for map");
			width = r.nextInt(255) + 1;
			height = r.nextInt(255) + 1;
		}

        map = new Terrain[height][width];
        populate();
    }

    //fills the map (a matrix of terrain) with actual instances of terrain
    //use this to spawn a map and actually have it not be full of null objects
    //since this is a private, use it only in map.java, and this method 
    //should only be used to create a map, not to constantly refill as 
    //we use randomization to make the map
    private void populate() {
        int n;
        Random r = new Random();
        
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                n = r.nextInt(100);
                if (0 <= n && n < 40) {
                    map[y][x] = new Terrain();
                } 
                else if (40 <= n && n< 55) {
                    map[y][x] = new Desert();
                }
                else if (55 <= n && n< 65) {
                    map[y][x] = new Swamp();
                }
                else if (65 <= n && n < 80) {
                    map[y][x] = new Frost();
                }
                else if (80 <= n && n < 98) {
                    map[y][x] = new Mountain();
                }
                else {
                    map[y][x] = new DMV();
                }
            }
        }

        // prevent goal tile from spawning near the player
        int goalTileY = r.nextInt(map.length);
        int goalTileX = r.nextInt(map[0].length);

        while (goalTileY <= 3) {
            goalTileY = r.nextInt(map.length);
        }

        while (goalTileX <= 3) {
            goalTileX = r.nextInt(map[0].length);
        }
        
        // finally instantiate goal tile
        map[goalTileY][goalTileX] = new Goal();
    }

    public void updateWithPlayer(Player player) {
        int prevX = player.getPrevX();
        int prevY = player.getPrevY();
        int x = player.getPosX();
        int y = player.getPosY();

        // restore the previous tile, only if valid
        if (prevX >= 0 && prevY >= 0) {
            map[prevY][prevX].stringRep = player.terrainStringBuffer;
        }

        // record what's under the new tile
        String newTerrain = map[y][x].stringRep;

        // place player by overwriting the new position with "P"
        map[y][x].stringRep = "P";

        // store that newTerrain for next time
        player.terrainStringBuffer = newTerrain;
    }

    //print the map
    //use this to show where things are
    public void print() {
        for (int y = 0; y < map.length;y++) {
            for (int x = 0; x < map[0].length;x++) {
                System.out.print(map[y][x].stringRep + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public int getHeight() {
        return map.length;
    }

    public int getWidth() {
        return map[0].length;
    }

    public Terrain getTerrain(int x, int y) {
        return map[y][x];
    }

    public Terrain[][] getBoard() {
        return map;
    }
}
