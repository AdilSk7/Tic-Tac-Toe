Tic-Tac-Toe Web UI

Run a small embedded HTTP server that uses the existing game logic classes.

Build & run (no external libs required):

1. Compile all Java files:

```bash
javac *.java
```

2. Run the web server:

```bash
java WebServer
```

3. Open http://localhost:8000/ in your browser.

Notes:
- The server serves files from the `web/` folder and provides simple JSON endpoints at `/api/*`.
- Do not rename the existing game logic files; the server uses them as-is.
