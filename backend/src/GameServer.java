import io.javalin.Javalin;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

public class GameServer {
    // static final Gson gson = new Gson();
    static final Gson gson = new GsonBuilder()
        .registerTypeHierarchyAdapter(Terrain.class, (JsonSerializer<Terrain>) (src, typeOfSrc, context) -> 
            new JsonPrimitive(src.stringRep)).setPrettyPrinting().create();
    static final GameState game = new GameState();

    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> cors.add(it -> it.allowHost("http://localhost:5173")));
        }).start(8080);

        // GET /state
        app.get("/state", ctx -> {
            game.updateMap();
            Terrain[][] board = game.getMap().getBoard();

            // board info
            Map<String, Object> response = configureBoardResponse(board);
        
            // player info
            Player p = game.getPlayer();
            Map<String, Object> playerInfo = configurePlayerInfo(p);
            response.put("player", playerInfo);

            // get the game level
            response.put("level", game.getLevel());

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        });

        // POST /reset
        // hard resets entire game
        app.post("/reset", ctx -> {
            game.reset();
            
            Terrain[][] board = game.getMap().getBoard();

            // board info
            Map<String, Object> response = configureBoardResponse(board);

            // player info
            Player p = game.getPlayer();
            Map<String, Object> playerInfo = configurePlayerInfo(p);
            response.put("player", playerInfo);

            // get the game level
            response.put("level", game.getLevel());

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        });

        // POST /nextlevel
        // hard resets entire game
        app.post("/nextlevel", ctx -> {
            game.nextLevel();
            
            Terrain[][] board = game.getMap().getBoard();

            // board info
            Map<String, Object> response = configureBoardResponse(board);

            // player info
            Player p = game.getPlayer();
            Map<String, Object> playerInfo = configurePlayerInfo(p);
            response.put("player", playerInfo);

            // get the game level
            response.put("level", game.getLevel());

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        });

        // POST /move
        // **expected return
        // body: {"direction": "up|down|left|right"}
        app.post("/move", ctx -> {
            // Get the latest board from map
            Terrain[][] board = game.getMap().getBoard();

            MoveRequest move = gson.fromJson(ctx.body(), MoveRequest.class);
            game.movePlayer(move.direction);

            // board info
            Map<String, Object> response = configureBoardResponse(board);

            // player info
            Player p = game.getPlayer();
            Map<String, Object> playerInfo = configurePlayerInfo(p);
            response.put("player", playerInfo);

            // get the game level
            response.put("level", game.getLevel());

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        });

        //--------------------------------------------------------------------------------
        //**POST brains
        // for brain hints, not a total AI takeover

        int hintCost = 5;
        // POST /balancedbrain
        app.post("/balancedbrain", ctx -> {
            Player p = game.getPlayer();
            p.setGold(p.getGold() - hintCost);

            Vision vision = new CautiousVision(game);
            Brain brain = new BalancedBrain(game, vision); 

            // chosen move that the brain decides
            Move chosen = brain.decideMove();

            Map<String, Object> response = new HashMap<>();
            response.put("brainMove", chosen.name());

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        });

        // POST /explorerbrain
        app.post("/explorerbrain", ctx -> {
            Player p = game.getPlayer();
            p.setGold(p.getGold() - hintCost);

            // game.updateMap();
            Vision vision = new CautiousVision(game);
            Brain brain = new ExplorerBrain(game, vision); 

            // chosen move that the brain decides
            Move chosen = brain.decideMove();

            Map<String, Object> response = new HashMap<>();
            response.put("brainMove", chosen.name());

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        });

        // POST /greedybrain
        app.post("/greedybrain", ctx -> {
            Player p = game.getPlayer();
            p.setGold(p.getGold() - hintCost);

            // game.updateMap();
            Vision vision = new CautiousVision(game);
            Brain brain = new GreedyBrain(game, vision); 

            // chosen move that the brain decides
            Move chosen = brain.decideMove();

            Map<String, Object> response = new HashMap<>();
            response.put("brainMove", chosen.name());

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        });
    }

    static Map<String, Object> configurePlayerInfo(Player player) {
        Map<String, Object> playerInfo = new HashMap<>();

        playerInfo.put("x", player.getPosX());
        playerInfo.put("y", player.getPosY());
        playerInfo.put("terrainStringBuffer", player.terrainStringBuffer);

        playerInfo.put("health", player.getHP());
        playerInfo.put("water", player.getWater());
        playerInfo.put("energy", player.getEnergy());
        playerInfo.put("gold", player.getGold());

        playerInfo.put("status", player.isAlive());
        playerInfo.put("won", player.getOnGoalTile());
        playerInfo.put("trading", player.getOnTraderTile());

        return playerInfo;
    }

    static Map<String, Object> configureBoardResponse(Terrain[][] board) {
        TileDTO[][] dtoBoard = new TileDTO[board.length][board[0].length];
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                dtoBoard[i][j] = new TileDTO(board[i][j]);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("rows", board.length);
        response.put("cols", board[0].length);
        response.put("board", dtoBoard);

        return response;
    }

    static class TileDTO {
        public String terrain;
        public Object tileObject;  // Can be ItemDTO or TraderDTO

        TileDTO(Terrain terrainObj) {
            // Always send terrain symbol
            this.terrain = terrainObj.stringRep;

            // Convert tileObject depending on what it is
            Object raw = terrainObj.getTileObject();

            if (raw == null) {
                this.tileObject = null;
            } else if (raw instanceof Item item) {
                this.tileObject = new ItemDTO(item);
            } else if (raw instanceof Trader trader) {
                this.tileObject = new TraderDTO(trader);
            }
        }
    }

    static class ItemDTO {
        public String type;       // "ITEM"
        public String itemType;   // "WATER_BOTTLE", "TURKEY", etc.

        ItemDTO(Item i) {
            this.type = "ITEM";
            this.itemType = i.getType().name();
        }
    }

    static class TraderDTO {
        public String type = "TRADER";
        public String name;
        public String traderType;  // Friendly, Generous, etc.
        public String mood;        // Annoyed, Calm, Happy
        public int patience;

        TraderDTO(Trader t) {
            this.name = t.name;
            this.traderType = t.type.name();
            this.mood = t.mood.name();
            this.patience = t.patience;
        }
    }

    static class MoveRequest {
        public String direction;
    }

    static class ErrorMsg {
        public String error;

        ErrorMsg(String m) {
            error = m;
        }
    }
}
