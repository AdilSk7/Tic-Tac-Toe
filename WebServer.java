import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class WebServer {
    private static final Map<String, GameState> games = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        // Read PORT from environment (cloud platforms set this dynamically)
        String portEnv = System.getenv("PORT");
        int PORT = (portEnv != null && !portEnv.isEmpty()) ? Integer.parseInt(portEnv) : 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
        server.createContext("/", WebServer::handleRoot);
        server.createContext("/static", WebServer::handleStatic);
        server.createContext("/api/new", WebServer::handleNew);
        server.createContext("/api/state", WebServer::handleState);
        server.createContext("/api/move", WebServer::handleMove);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Tic-Tac-Toe web server running on port " + PORT);
    }

    private static void handleRoot(HttpExchange ex) throws IOException {
        Path p = Paths.get("web/index.html");
        if (!Files.exists(p)) {
            sendString(ex, 404, "Index not found");
            return;
        }
        byte[] bytes = Files.readAllBytes(p);
        ex.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
        sendBytes(ex, 200, bytes);
    }

    private static void handleStatic(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath(); // /static/...
        String relative = path.replaceFirst("/static/", "");
        Path p = Paths.get("web", relative);
        if (!Files.exists(p) || Files.isDirectory(p)) {
            sendString(ex, 404, "Not found");
            return;
        }
        String ct = guessContentType(relative);
        ex.getResponseHeaders().add("Content-Type", ct);
        byte[] bytes = Files.readAllBytes(p);
        sendBytes(ex, 200, bytes);
    }

    private static void handleNew(HttpExchange ex) throws IOException {
        Map<String, String> params = parseQuery(ex.getRequestURI().getQuery());
        String player1 = params.getOrDefault("player1", "Player 1");
        boolean ai = "1".equals(params.get("ai")) || "true".equalsIgnoreCase(params.get("ai"));
        String player2 = params.get("player2");
        GameState gs = new GameState(ai, player1, player2);
        String id = UUID.randomUUID().toString();
        games.put(id, gs);
        String resp = "{\"id\":\"" + id + "\",\"state\":" + gs.toJson() + "}";
        ex.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        sendString(ex, 200, resp);
    }

    private static void handleState(HttpExchange ex) throws IOException {
        Map<String, String> params = parseQuery(ex.getRequestURI().getQuery());
        String id = params.get("id");
        if (id == null || !games.containsKey(id)) {
            sendString(ex, 404, "{\"error\":\"not found\"}");
            return;
        }
        GameState gs = games.get(id);
        ex.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        sendString(ex, 200, gs.toJson());
    }

    private static void handleMove(HttpExchange ex) throws IOException {
        Map<String, String> params = parseQuery(ex.getRequestURI().getQuery());
        String id = params.get("id");
        if (id == null || !games.containsKey(id)) {
            sendString(ex, 404, "{\"error\":\"not found\"}");
            return;
        }
        GameState gs = games.get(id);
        if (gs.finished) {
            sendString(ex, 200, gs.toJson());
            return;
        }
        try {
            int row = Integer.parseInt(params.getOrDefault("row", "-1"));
            int col = Integer.parseInt(params.getOrDefault("col", "-1"));
            boolean moved = gs.makeMove(row, col);
            if (moved && gs.vsComputer && !gs.finished && gs.currentPlayer == 'O') {
                gs.computerTurn();
            }
            sendString(ex, 200, gs.toJson());
        } catch (Exception e) {
            sendString(ex, 400, "{\"error\":\"bad request\"}");
        }
    }

    private static void sendString(HttpExchange ex, int code, String s) throws IOException {
        byte[] bytes = s.getBytes("UTF-8");
        sendBytes(ex, code, bytes);
    }

    private static void sendBytes(HttpExchange ex, int code, byte[] bytes) throws IOException {
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static Map<String, String> parseQuery(String q) {
        Map<String, String> map = new HashMap<>();
        if (q == null || q.isEmpty()) return map;
        for (String part : q.split("&")) {
            int idx = part.indexOf('=');
            try {
                if (idx >= 0) {
                    String k = URLDecoder.decode(part.substring(0, idx), "UTF-8");
                    String v = URLDecoder.decode(part.substring(idx + 1), "UTF-8");
                    map.put(k, v);
                } else {
                    String k = URLDecoder.decode(part, "UTF-8");
                    map.put(k, "");
                }
            } catch (UnsupportedEncodingException e) {
                // ignore
            }
        }
        return map;
    }

    private static String guessContentType(String name) {
        if (name.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (name.endsWith(".css")) return "text/css; charset=utf-8";
        if (name.endsWith(".html")) return "text/html; charset=utf-8";
        if (name.endsWith(".png")) return "image/png";
        return "application/octet-stream";
    }
}
