import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.ServiceUnavailableResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

public class GameServer {
    // static final Gson gson = new Gson();
    static final Gson gson = new GsonBuilder()
        .registerTypeHierarchyAdapter(Terrain.class, (JsonSerializer<Terrain>) (src, typeOfSrc, context) -> 
            new JsonPrimitive(src.stringRep)).setPrettyPrinting().create();
    static final String SESSION_HEADER = "X-Game-Session";
    static final int MAX_ACTIVE_SESSIONS = 1_000;
    static final long SESSION_TTL_MILLIS = TimeUnit.HOURS.toMillis(4);
    static final long SESSION_CLEANUP_INTERVAL_MILLIS = TimeUnit.MINUTES.toMillis(5);
    static final Pattern SESSION_ID_PATTERN = Pattern.compile(
        "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$"
    );
    static final ConcurrentHashMap<String, GameSession> sessions = new ConcurrentHashMap<>();
    static final AtomicLong nextSessionCleanupAt = new AtomicLong(0);

    public static void main(String[] args) {
        String host = System.getenv().getOrDefault("HOST", "127.0.0.1");
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        String[] allowedOrigins = Arrays.stream(System.getenv().getOrDefault(
                "CORS_ALLOWED_ORIGINS",
                "http://localhost:5173,https://cs3560-team-4.github.io"
            ).split(","))
            .map(String::trim)
            .filter(origin -> !origin.isEmpty())
            .toArray(String[]::new);

        var app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> cors.add(it -> {
                for (String origin : allowedOrigins) {
                    it.allowHost(origin);
                }
            }));
        }).start(host, port);

        app.get("/health", ctx -> sendJson(ctx, Map.of("status", "ok")));

        // GET /state
        app.get("/state", sessionHandler((ctx, game) -> {
            Terrain[][] board = game.getMap().getBoard();

            // board info
            Map<String, Object> response = configureBoardResponse(board);
        
            // player info
            Player p = game.getPlayer();
            Map<String, Object> playerInfo = configurePlayerInfo(p);
            response.put("player", playerInfo);

            // game info
            configureGameInfo(response, game);

            // add vision tiles
            Vision vision = p.getVision();
            response.put("visibleTiles", vision.getVisibleCoordinates());

            // get trader info
            response.put("activeTrader", 
                game.getActiveTrader() == null ? null : new TraderDTO(game.getActiveTrader()));
            response.put("activeOffer", 
                game.getActiveOffer() == null ? null : new TradeOfferDTO(game.getActiveOffer()));

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        }));

        // POST /reset
        // hard resets entire game
        app.post("/reset", sessionHandler((ctx, game) -> {
            game.reset();
            
            Terrain[][] board = game.getMap().getBoard();

            // board info
            Map<String, Object> response = configureBoardResponse(board);

            // player info
            Player p = game.getPlayer();
            Map<String, Object> playerInfo = configurePlayerInfo(p);
            response.put("player", playerInfo);
            Vision vision = p.getVision();
            response.put("visibleTiles", vision.getVisibleCoordinates());

            // game info
            configureGameInfo(response, game);

             // get trader info
            response.put("activeTrader", 
                game.getActiveTrader() == null ? null : new TraderDTO(game.getActiveTrader()));
            response.put("activeOffer", 
                game.getActiveOffer() == null ? null : new TradeOfferDTO(game.getActiveOffer()));

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        }));

        // POST /nextlevel
        // hard resets entire game
        app.post("/nextlevel", sessionHandler((ctx, game) -> {
            game.nextLevel();
            
            Terrain[][] board = game.getMap().getBoard();

            // board info
            Map<String, Object> response = configureBoardResponse(board);

            // player info
            Player p = game.getPlayer();
            Map<String, Object> playerInfo = configurePlayerInfo(p);
            response.put("player", playerInfo);
            Vision vision = p.getVision();
            response.put("visibleTiles", vision.getVisibleCoordinates());

            // game info
            configureGameInfo(response, game);

             // get trader info
            response.put("activeTrader", 
                game.getActiveTrader() == null ? null : new TraderDTO(game.getActiveTrader()));
            response.put("activeOffer", 
                game.getActiveOffer() == null ? null : new TradeOfferDTO(game.getActiveOffer()));

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        }));

        // POST /move
        // **expected return
        // body: {"direction": "up|down|left|right"}
        app.post("/move", sessionHandler((ctx, game) -> {

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

            // update visible tiles
            Vision vision = p.getVision();
            response.put("visibleTiles", vision.getVisibleCoordinates());

            // game info
            configureGameInfo(response, game);

             // get trader info
            response.put("activeTrader", 
                game.getActiveTrader() == null ? null : new TraderDTO(game.getActiveTrader()));
            response.put("activeOffer", 
                game.getActiveOffer() == null ? null : new TradeOfferDTO(game.getActiveOffer()));

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        }));

        //--------------------------------------------------------------------------------
        //**POST brains
        // for brain hints, not a total AI takeover

        int hintCost = 5;
        // POST /balancedbrain
        app.post("/balancedbrain", sessionHandler((ctx, game) -> {
            Player p = game.getPlayer();
            p.setGold(p.getGold() - hintCost);

            // Use the player's existing vision
            Vision vision = p.getVision();
            Brain brain = new BalancedBrain(game, p, vision);

            // chosen move that the brain decides
            Move chosen = brain.makeMove();

            Map<String, Object> response = new HashMap<>();
            response.put("brainMove", chosen.name());
            response.put("visibleTiles", vision.getVisibleCoordinates());
            System.out.println("Balanced Brain says: " + chosen.name());

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        }));

        // POST /explorerbrain
        app.post("/explorerbrain", sessionHandler((ctx, game) -> {
            Player p = game.getPlayer();
            p.setGold(p.getGold() - hintCost);

            Vision vision = p.getVision();
            Brain brain = new ExplorerBrain(game, p, vision);

            // chosen move that the brain decides
            Move chosen = brain.makeMove();

            Map<String, Object> response = new HashMap<>();
            response.put("brainMove", chosen.name());
            response.put("visibleTiles", vision.getVisibleCoordinates());
            System.out.println("Explorer Brain says: " + chosen.name());

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        }));

        // POST /greedybrain
        app.post("/greedybrain", sessionHandler((ctx, game) -> {
            Player p = game.getPlayer();
            p.setGold(p.getGold() - hintCost);

            Vision vision = p.getVision();
            Brain brain = new GreedyBrain(game, p, vision);

            // chosen move that the brain decides
            Move chosen = brain.makeMove();

            Map<String, Object> response = new HashMap<>();
            response.put("brainMove", chosen.name());
            response.put("visibleTiles", vision.getVisibleCoordinates());
            System.out.println("Greedy Brain says: " + chosen.name());

            ctx.contentType("application/json");
            ctx.result(gson.toJson(response));
        }));

        //**Trade endpoints
        // POST /begintrade
        app.post("/begintrade", sessionHandler((ctx, game) -> {
            Trader t = game.getActiveTrader();
            TradeOffer offer = game.getActiveOffer();

            Map<String, Object> response = new HashMap<>();
            response.put("trader", new TraderDTO(t));
            response.put("offer", new TradeOfferDTO(offer));

            sendJson(ctx, response);
        }));

        // POST /accepttrade
        app.post("/accepttrade", sessionHandler((ctx, game) -> {
            TradeOffer offer = game.getActiveOffer();
            Player p = game.getPlayer();

            // Player pays
            p.setGold(p.getGold() - offer.goldCost);

            // Player receives effects
            offer.offeredItem.use(p);

            Terrain tile = game.getMap().getTerrain(p.getPosX(), p.getPosY());
            tile.removeTileObject();

            tile.stringRep = p.terrainStringBuffer;

            // End trade
            game.clearTrade();

            sendJson(ctx, Map.of("success", true));
        }));

        // POST /rejecttrade
        app.post("/rejecttrade", sessionHandler((ctx, game) -> {
            Player p = game.getPlayer();

            Trader t = game.getActiveTrader();
            t.rejectTrade(); // maybe reduce mood/patience

            Terrain tile = game.getMap().getTerrain(p.getPosX(), p.getPosY());
            tile.removeTileObject();

            tile.stringRep = p.terrainStringBuffer;
            
            // End trade
            game.clearTrade();

            sendJson(ctx, Map.of("success", true));
        }));

        //** For setting player vision *//
        // POST /cautious-vision
        app.post("/cautious-vision", sessionHandler((ctx, game) -> {
            Player p = game.getPlayer();
            p.setVision(new CautiousVision(game));
        }));

        // POST /keen-vision
        app.post("/keen-vision", sessionHandler((ctx, game) -> {
            Player p = game.getPlayer();
            p.setVision(new KeenVision(game));
        }));

        // POST /narrow-vision
        app.post("/narrow-vision", sessionHandler((ctx, game) -> {
            Player p = game.getPlayer();
            p.setVision(new NarrowVision(game));
        }));

        // POST /queen-vision
        app.post("/queen-vision", sessionHandler((ctx, game) -> {
            Player p = game.getPlayer();
            p.setVision(new QueenVision(game));
        }));
    }

    static Handler sessionHandler(BiConsumer<Context, GameState> handler) {
        return ctx -> {
            GameSession session = sessionFor(ctx);
            synchronized (session.game) {
                handler.accept(ctx, session.game);
            }
        };
    }

    static GameSession sessionFor(Context ctx) {
        String sessionId = ctx.header(SESSION_HEADER);
        if (sessionId == null || !SESSION_ID_PATTERN.matcher(sessionId).matches()) {
            throw new BadRequestResponse("Missing or invalid " + SESSION_HEADER + " header");
        }

        long now = System.currentTimeMillis();
        cleanupExpiredSessions(now);

        GameSession existingSession = sessions.get(sessionId);
        if (existingSession != null) {
            existingSession.lastAccessMillis = now;
            return existingSession;
        }

        if (sessions.size() >= MAX_ACTIVE_SESSIONS) {
            throw new ServiceUnavailableResponse("Too many active game sessions");
        }

        GameSession session = sessions.computeIfAbsent(sessionId, ignored -> new GameSession(now));
        session.lastAccessMillis = now;
        return session;
    }

    static void cleanupExpiredSessions(long now) {
        long scheduledCleanup = nextSessionCleanupAt.get();
        if (now < scheduledCleanup || !nextSessionCleanupAt.compareAndSet(
                scheduledCleanup,
                now + SESSION_CLEANUP_INTERVAL_MILLIS
            )) {
            return;
        }

        sessions.entrySet().removeIf(
            entry -> now - entry.getValue().lastAccessMillis > SESSION_TTL_MILLIS
        );
    }

    static class GameSession {
        final GameState game = new GameState();
        volatile long lastAccessMillis;

        GameSession(long lastAccessMillis) {
            this.lastAccessMillis = lastAccessMillis;
        }
    }

    static void sendJson(Context ctx, Object response) {
        ctx.contentType("application/json");
        ctx.result(gson.toJson(response));
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

    static void configureGameInfo(Map<String, Object> response, GameState game) {
        response.put("level", game.getLevel());
        response.put("currentscore", game.getCurrentScore());
        response.put("highscore", game.getHighScore());
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

    static class TradeOfferDTO {
        public String itemType;
        public int price;

        TradeOfferDTO(TradeOffer offer) {
            this.itemType = offer.offeredItem.getType().name();
            this.price = offer.goldCost;
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
