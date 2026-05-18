package com.cartracker;

import com.cartracker.model.user.Customer;
import com.cartracker.model.user.User;
import com.cartracker.service.user.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import com.cartracker.controller.feedback.FeedbackController;
import com.cartracker.handler.FeedbackHttpHandler;
import com.cartracker.repository.feedback.JdbcFeedbackRepository;
import com.cartracker.service.feedback.FeedbackServiceImpl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Optional;

public class WebServer {

    private final UserService userService;

    public WebServer(UserService userService) {
        this.userService = userService;
    }

    public void start() throws IOException
    {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new StaticFileHandler());
        server.createContext("/api/signup", new SignUpHandler());
        server.createContext("/api/login",  new LoginHandler());

        // ── Feedback Module ────────────────────────────────────────────────────
        JdbcFeedbackRepository feedbackRepo       = new JdbcFeedbackRepository();
        FeedbackServiceImpl    feedbackService    = new FeedbackServiceImpl(feedbackRepo);
        FeedbackController     feedbackController = new FeedbackController(feedbackService);
        server.createContext("/api/feedback", new FeedbackHttpHandler(feedbackController));

        server.setExecutor(null);
        server.start();
        // CORS is applied inside each handler via addCorsHeaders()
        System.out.println("[Web Server] Started at http://localhost:8080");
    }



    private class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) {
                path = "/index.html";
            }

            File file = new File("src/main/resources/web" + path);
            if (file.exists() && !file.isDirectory()) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } else {
                String response = "404 Not Found";
                exchange.sendResponseHeaders(404, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }

    private class SignUpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                // Simple form-data parsing (e.g., name=John&email=john@mail.com&password=123)
                String[] pairs = body.split("&");
                String name = "", email = "", password = "";
                for (String pair : pairs) {
                    String[] kv = pair.split("=");
                    if (kv.length == 2) {
                        String key = kv[0];
                        String val = java.net.URLDecoder.decode(kv[1], "UTF-8");
                        if (key.equals("name")) name = val;
                        else if (key.equals("email")) email = val;
                        else if (key.equals("password")) password = val;
                    }
                }

                try {
                    Customer newCustomer = new Customer();
                    newCustomer.setFullName(name);
                    newCustomer.setEmail(email);
                    newCustomer.setPassword(password);
                    newCustomer.setCreatedAt(LocalDateTime.now());
                    newCustomer.setUpdatedAt(LocalDateTime.now());

                    userService.register(newCustomer);

                    String response = "{\"success\":true, \"message\":\"Account created successfully!\"}";
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } catch (Exception e) {
                    String response = "{\"success\":false, \"message\":\"" + e.getMessage() + "\"}";
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(400, response.length());
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            }
        }
    }

    private class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                String[] pairs = body.split("&");
                String email = "", password = "";
                for (String pair : pairs) {
                    String[] kv = pair.split("=");
                    if (kv.length == 2) {
                        String key = kv[0];
                        String val = java.net.URLDecoder.decode(kv[1], "UTF-8");
                        if (key.equals("email")) email = val;
                        else if (key.equals("password")) password = val;
                    }
                }

                Optional<User> userOpt = userService.login(email, password);
                if (userOpt.isPresent()) {
                    String name = userOpt.get().getFullName();
                    String userEmail = userOpt.get().getEmail();
                    String response = "{\"success\":true, \"message\":\"Login successful!\", \"name\":\"" + name + "\", \"email\":\"" + userEmail + "\"}";
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, response.length());
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } else {
                    String response = "{\"success\":false, \"message\":\"Invalid email or password.\"}";
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(401, response.length());
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            }
        }
    }
}


