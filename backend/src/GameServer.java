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

            Map<String, Object> response = new HashMap<>();
            response.put("rows", board.length);
            response.put("cols", board[0].length);
            response.put("board", board);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        });

        // POST /reset
        // hard resets entire game
        app.post("/reset", ctx -> {
            game.reset();
            
            Terrain[][] board= game.getMap().getBoard();

            Map<String, Object> response = new HashMap<>();
            response.put("rows", board.length);
            response.put("cols", board[0].length);
            response.put("board", board);

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        });

        // POST /move
        // **expected return
        // body: {"direction": "up|down|left|right"}
        app.post("/move", ctx -> {
            MoveRequest move = gson.fromJson(ctx.body(), MoveRequest.class);
            game.movePlayer(move.direction);

            // Get the latest board from map
            Terrain[][] board = game.getMap().getBoard();

            // configure response
            Map<String, Object> response = new HashMap<>();
            response.put("rows", board.length);
            response.put("cols", board[0].length);
            response.put("board", board);

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        });

        // POST /brain
        // for brain hints, not a total AI takeover
        app.post("/brain", ctx -> {
            Vision vision = new CautiousVision(game);
            Brain brain = new BalancedBrain(game, vision);

            // chosen move that the brain decides
            Move chosen = brain.decideMove();

            Map<String, Object> response = new HashMap<>();
            response.put("brainMove", chosen.name());

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        });
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
