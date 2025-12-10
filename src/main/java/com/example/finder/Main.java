package com.example.finder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main{
    
    private static final List<LostItem> ITEMS = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8000"));

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // API for lost & found items
        server.createContext("/api/items", new ItemsHandler());

        // Static file handler (serves index.html, css, js)
        server.createContext("/", new StaticFileHandler("public"));

        server.setExecutor(null); // default executor
        System.out.println("Finder server running at http://localhost:" + port);
        server.start();
    }

    static class ItemsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange.getResponseHeaders());

            String method = exchange.getRequestMethod();
            if ("OPTIONS".equalsIgnoreCase(method)) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            try {
                if ("GET".equalsIgnoreCase(method)) {
                    handleGet(exchange);
                } else if ("POST".equalsIgnoreCase(method)) {
                    handlePost(exchange);
                } else {
                    sendTextResponse(exchange, 405, "Method Not Allowed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendTextResponse(exchange, 500, "Server error: " + e.getMessage());
            }
        }

        private void handleGet(HttpExchange exchange) throws IOException {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            synchronized (ITEMS) {
                for (int i = 0; i < ITEMS.size(); i++) {
                    if (i > 0) sb.append(",");
                    sb.append(ITEMS.get(i).toJson());
                }
            }
            sb.append("]");

            byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private void handlePost(HttpExchange exchange) throws IOException {
            String body = readBody(exchange.getRequestBody());
            Map<String, String> params = parseFormEncoded(body);

            String title = params.getOrDefault("title", "").trim();
            String description = params.getOrDefault("description", "").trim();
            String place = params.getOrDefault("place", "").trim();
            String date = params.getOrDefault("date", "").trim();
            String contact = params.getOrDefault("contact", "").trim();

            if (title.isEmpty() || description.isEmpty()) {
                sendTextResponse(exchange, 400, "Title and description are required.");
                return;
            }

            LostItem item = new LostItem(title, description, place, date, contact);
            ITEMS.add(item);

            byte[] bytes = item.toJson().getBytes(StandardCharsets.UTF_8);
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(201, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    static class StaticFileHandler implements HttpHandler {
        private final Path rootDir;

        StaticFileHandler(String rootDir) {
            this.rootDir = Paths.get(rootDir).toAbsolutePath().normalize();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange.getResponseHeaders());

            String pathStr = exchange.getRequestURI().getPath();
            if (pathStr.equals("/")) {
                pathStr = "/index.html";
            }

            Path filePath = rootDir.resolve("." + pathStr).normalize();

            if (!filePath.startsWith(rootDir) || !Files.exists(filePath) || Files.isDirectory(filePath)) {
                sendTextResponse(exchange, 404, "Not Found");
                return;
            }

            String contentType = guessContentType(filePath.toString());
            byte[] bytes = Files.readAllBytes(filePath);

            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    static class LostItem {
        private static int NEXT_ID = 1;
        private final int id;
        private final String title;
        private final String description;
        private final String place;
        private final String date;
        private final String contact;
        private final String createdAt;

        LostItem(String title, String description, String place, String date, String contact) {
            this.id = NEXT_ID++;
            this.title = title;
            this.description = description;
            this.place = place;
            this.date = date;
            this.contact = contact;
            this.createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        }

        String escape(String s) {
            if (s == null) return "";
            return s.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n");
        }

        String toJson() {
            return "{" +
                    "\"id\":" + id + "," +
                    "\"title\":\"" + escape(title) + "\"," +
                    "\"description\":\"" + escape(description) + "\"," +
                    "\"place\":\"" + escape(place) + "\"," +
                    "\"date\":\"" + escape(date) + "\"," +
                    "\"contact\":\"" + escape(contact) + "\"," +
                    "\"createdAt\":\"" + escape(createdAt) + "\"" +
                    "}";
        }
    }

    private static String readBody(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

    private static Map<String, String> parseFormEncoded(String body) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        if (body == null || body.isEmpty()) return map;
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] parts = pair.split("=", 2);
            String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8.name());
            String value = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8.name()) : "";
            map.put(key, value);
        }
        return map;
    }

    private static void sendTextResponse(HttpExchange exchange, int status, String text) throws IOException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void addCorsHeaders(Headers headers) {
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
    }

    private static String guessContentType(String filename) {
        if (filename.endsWith(".html")) return "text/html; charset=utf-8";
        if (filename.endsWith(".css")) return "text/css; charset=utf-8";
        if (filename.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (filename.endsWith(".png")) return "image/png";
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
        if (filename.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }
}
