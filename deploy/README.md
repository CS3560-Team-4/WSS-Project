# Web deployment

The production topology is:

```text
GitHub Pages  https://<owner>.github.io/<repository>/
       |
       +----> https://netricsports.us/game-api/*
                         |
                         +----> http://127.0.0.1:8080/*
```

This application currently uses REST only. There is no WebSocket endpoint, so
`/game-ws` should not be added to Nginx unless WebSocket functionality is added
later.

## 1. Install the backend

The server needs Java 21. Build the executable jar from the repository root:

```bash
./backend/mvnw -f backend/pom.xml clean package
```

Copy the jar and service definition to the server:

```bash
sudo install -d -m 755 /opt/wss-game
sudo install -m 644 backend/target/game-server.jar /opt/wss-game/game-server.jar
sudo install -m 644 deploy/systemd/wss-game.service /etc/systemd/system/wss-game.service
sudo systemctl daemon-reload
sudo systemctl enable --now wss-game
curl --fail http://127.0.0.1:8080/health
```

The backend runs detached, restarts automatically, and starts again after a
server reboot. It is independently controllable without affecting Netric:

```bash
sudo systemctl stop wss-game       # take the game backend down
sudo systemctl start wss-game      # bring it back up
sudo systemctl restart wss-game    # restart it after an update
sudo systemctl disable wss-game    # do not start it on the next boot
sudo systemctl enable wss-game     # start it on future boots
sudo systemctl status wss-game
```

If the Pages site belongs to a different GitHub owner, change
`CORS_ALLOWED_ORIGINS` in the service to its origin (scheme plus hostname, with
no repository path), then run `systemctl daemon-reload` and restart the service.
Multiple origins can be comma-separated.

## 2. Add the isolated Nginx route

Netric owns `/etc/nginx/sites-available/netric`, so the game deployment must not
replace that file. Install only the game-owned snippet:

```bash
sudo install -m 644 deploy/nginx/game-locations.conf /etc/nginx/snippets/wss-game.conf
```

Then add this one line inside the existing `server` block in
`../netric/ops/nginx/netric.conf`, outside its `/api/` location:

```nginx
include /etc/nginx/snippets/wss-game.conf;
```

The Netric deployment script owns and regenerates the active site from that
template. Adding the include there keeps `/api/`, port `8000`, its static files,
certificate changes, services, and health checks under Netric's control while
the separate snippet owns only `/game-api/` and port `8080`.

Redeploy Netric, or add the same include to its current active server block,
then validate and reload:

```bash
sudo nginx -t
sudo systemctl reload nginx
curl --fail https://netricsports.us/game-api/health
```

No additional DNS record or certificate is required.

## 3. Enable GitHub Pages

In the GitHub repository, open **Settings > Pages** and set **Source** to
**GitHub Actions**. Push the repository's `main` branch, or manually run the
**Deploy frontend to GitHub Pages** workflow.

The workflow automatically uses the repository name as Vite's base path. For
example, a repository named `wss-game` owned by `username` is published at
`https://username.github.io/wss-game/`. This repository's current remote is
`CS3560-Team-4/WSS-Project`, so without renaming or moving it, its URL is
`https://cs3560-team-4.github.io/WSS-Project/`.

## Updating the backend

After backend changes, rebuild and replace the jar, then restart:

```bash
./backend/mvnw -f backend/pom.xml clean package
sudo install -m 644 backend/target/game-server.jar /opt/wss-game/game-server.jar
sudo systemctl restart wss-game
curl --fail https://netricsports.us/game-api/health
```

The backend holds a separate in-memory game for each browser tab. Inactive games
expire after four hours, and restarting the backend resets all active games.
