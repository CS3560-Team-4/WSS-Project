import java.util.Random;
public class Map {
    Terrain[][] map;
    
    //to access height, use mapName.length
    //to access width, use mapName[i].length such that i is a non-negative int

<<<<<<< HEAD
	public Terrain[][] generateMap(int length, int height){
		if(length<=0||height<=0) {
=======
    //This constructor makes a matrix of terrain objects to form our map, accounts for 
    //invalid inputs, initially the matrix has null objects
    public Map(int width, int height) {
        if (width<=0||height<=0) {
>>>>>>> test
			System.out.println("Invalid map dimensions, generating random valid dimensions for map");
			Random r = new Random();
			width = r.nextInt(255)+1;
			height = r.nextInt(255)+1;
		}
<<<<<<< HEAD
		Terrain[][] map = new Terrain[height][length];
		return map;
	}

    private void fill(char ch) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x] = String.valueOf(ch);
=======
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
                if(0 <= n && n < 40){
                    map[y][x] = new Terrain();
                }
                else if(40 <= n && n< 65){
                    map[y][x] = new Desert();
                }
                else if(65 <= n && n < 80){
                    map[y][x] = new Frost();
                }
                else if(80 <= n && n < 98){
                    map[y][x] = new Mountain();
                }
                else{
                    map[y][x] = new DMV();
                }
>>>>>>> test
            }
        }
    }

    /*private void buildWalls() {
        // top and bottom
        for (int x = 0; x < map[0].length; x++) {
            tiles[0][x] = "#";
            tiles[height - 1][x] = "#";
        }

        // sides
        for (int y = 0; y < map.length; y++) {
            tiles[y][0] = "#";
            tiles[y][width - 1] = "#";
        }
    }*/

    public void updateWithPlayer(Player player) {
        // only update map if position actually changed
        if (player.getPrevX() != player.getPosX() || player.getPrevY() != player.getPosY()) {
            map[player.getPrevY()][player.getPrevX()].stringRep = player.terrainStringBuffer;
        }
        

        // save current terrain and set player icon
        print();
        player.terrainStringBuffer = map[player.getPosY()][player.getPosX()].stringRep;
        map[player.getPosY()][player.getPosX()].stringRep = "P";
        print();
    }

    //**may or may not need
    //**come back to this if item classes are being implemented
    //
    // set npc/item within square
    // public void setSquare(int x, int y, Object obj) {
    //     if (y < 0 || y >= map.length || x < 0 || x >= map[0].length) return;
    //     map[y][x] = obj.toString();
    // }

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
