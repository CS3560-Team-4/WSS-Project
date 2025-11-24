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

        // POST /balancedbrain
        app.post("/balancedbrain", ctx -> {
            Player p = game.getPlayer();
            p.setGold(0);

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
            p.setGold(0);

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
            p.setGold(0);

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

        return playerInfo;
    }

    static Map<String, Object> configureBoardResponse(Terrain[][] board) {
        Map<String, Object> response = new HashMap<>();
        response.put("rows", board.length);
        response.put("cols", board[0].length);
        response.put("board", board);

        return response;
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
