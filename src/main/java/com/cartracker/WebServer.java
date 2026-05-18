package com.cartracker;

import com.cartracker.model.user.Customer;
import com.cartracker.model.user.User;
import com.cartracker.model.vehicle.Vehicle;
import com.cartracker.service.user.UserService;
import com.cartracker.service.vehicle.VehicleService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class WebServer {

    private final UserService userService;
    private final VehicleService vehicleService;

    public WebServer(UserService userService, VehicleService vehicleService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new StaticFileHandler());
        server.createContext("/api/signup", new SignUpHandler());
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/vehicles", new VehicleHandler());
        server.createContext("/api/user-id", new UserIdHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("[Web Server] Started at http://localhost:8080");
    }

    // ── Helper: send JSON response ─────────────────────────────────────────────
    private static void sendJson(HttpExchange exchange, int code, String json) throws IOException {
        byte[] bytes = json.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // ── Helper: escape JSON string ─────────────────────────────────────────────
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // ── Helper: vehicle → JSON object ──────────────────────────────────────────
    private static String vehicleToJson(Vehicle v) {
        return "{" +
            "\"id\":\"" + esc(v.getId()) + "\"," +
            "\"ownerId\":\"" + esc(v.getOwnerId()) + "\"," +
            "\"licensePlate\":\"" + esc(v.getLicensePlate()) + "\"," +
            "\"make\":\"" + esc(v.getMake()) + "\"," +
            "\"model\":\"" + esc(v.getModel()) + "\"," +
            "\"year\":" + v.getYear() + "," +
            "\"color\":\"" + esc(v.getColor()) + "\"," +
            "\"mileage\":" + v.getMileage() + "," +
            "\"fuelType\":\"" + esc(v.getFuelType()) + "\"" +
            "}";
    }

    // ── Static File Handler ────────────────────────────────────────────────────
    private class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) path = "/index.html";

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

    // ── UserIdHandler – GET /api/user-id?email=... ────────────────────────────
    private class UserIdHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery(); // e.g. "email=foo%40bar.com"
            String email = "";
            if (query != null) {
                for (String param : query.split("&")) {
                    String[] kv = param.split("=", 2);
                    if (kv.length == 2 && kv[0].equals("email")) {
                        email = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    }
                }
            }
            System.out.println("[UserIdHandler] Looking up user by email: '" + email + "'");
            if (email.isEmpty()) {
                sendJson(exchange, 400, "{\"success\":false,\"message\":\"email param required\"}");
                return;
            }
            java.util.Optional<com.cartracker.model.user.User> uOpt = userService.findByUsername(email);
            if (uOpt.isPresent()) {
                String id = uOpt.get().getId();
                System.out.println("[UserIdHandler] Found user id: " + id);
                sendJson(exchange, 200, "{\"success\":true,\"id\":\"" + esc(id) + "\"}");
            } else {
                System.out.println("[UserIdHandler] No user found for email: " + email);
                sendJson(exchange, 404, "{\"success\":false,\"message\":\"User not found\"}");
            }
        }
    }

    // ── Vehicle Handler – GET /api/vehicles  &  POST /api/vehicles ────────────
    private class VehicleHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            if ("GET".equals(method)) {
                // Check for ?id= to return a single vehicle
                String query = exchange.getRequestURI().getQuery();
                String idParam = null;
                if (query != null) {
                    for (String p : query.split("&")) {
                        String[] kv = p.split("=", 2);
                        if (kv.length == 2 && kv[0].equals("id")) {
                            idParam = java.net.URLDecoder.decode(kv[1], "UTF-8");
                        }
                    }
                }

                if (idParam != null && !idParam.isEmpty()) {
                    // Return single vehicle by id
                    java.util.Optional<Vehicle> vOpt = vehicleService.findById(idParam);
                    if (vOpt.isPresent()) {
                        sendJson(exchange, 200, vehicleToJson(vOpt.get()));
                    } else {
                        sendJson(exchange, 404, "{\"success\":false,\"message\":\"Vehicle not found\"}");
                    }
                    return;
                }

                // Return all vehicles as JSON array
                List<Vehicle> vehicles = vehicleService.findAll();
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < vehicles.size(); i++) {
                    sb.append(vehicleToJson(vehicles.get(i)));
                    if (i < vehicles.size() - 1) sb.append(",");
                }
                sb.append("]");
                sendJson(exchange, 200, sb.toString());

            } else if ("POST".equals(method)) {
                // Parse form-encoded body and save a new vehicle
                String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");
                String[] pairs = body.split("&");
                String make = "", model = "", licensePlate = "", color = "", fuelType = "Petrol", ownerId = "", userEmail = "";
                int year = 0;
                double mileage = 0;

                for (String pair : pairs) {
                    String[] kv = pair.split("=", 2);
                    if (kv.length < 2) continue;
                    String key = kv[0];
                    String val = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    switch (key) {
                        case "make":          make         = val; break;
                        case "model":         model        = val; break;
                        case "year":          try { year   = Integer.parseInt(val); } catch (NumberFormatException ignored) {} break;
                        case "license_plate": licensePlate = val; break;
                        case "color":         color        = val; break;
                        case "fuel_type":     fuelType     = val; break;
                        case "mileage":       try { mileage = Double.parseDouble(val); } catch (NumberFormatException ignored) {} break;
                        case "owner_id":      ownerId      = val; break;
                        case "user_email":    userEmail    = val; break;
                    }
                }

                // Resolve owner_id: prefer explicit id, fall back to email lookup
                if ((ownerId == null || ownerId.isEmpty()) && !userEmail.isEmpty()) {
                    java.util.Optional<com.cartracker.model.user.User> uOpt = userService.findByUsername(userEmail);
                    if (uOpt.isPresent()) ownerId = uOpt.get().getId();
                }

                System.out.println("[VehicleHandler] Received owner_id='" + ownerId + "' userEmail='" + userEmail + "'");

                if (ownerId == null || ownerId.isEmpty()) {
                    sendJson(exchange, 400, "{\"success\":false,\"message\":\"Could not resolve owner. Please log in again.\"");
                    return;
                }

                try {
                    Vehicle v = new Vehicle();
                    v.setMake(make);
                    v.setModel(model);
                    v.setYear(year);
                    v.setLicensePlate(licensePlate);
                    v.setColor(color);
                    v.setFuelType(fuelType);
                    v.setMileage(mileage);
                    v.setOwnerId(ownerId.isEmpty() ? "unknown" : ownerId);

                    Vehicle saved = vehicleService.addVehicle(v);
                    sendJson(exchange, 200,
                        "{\"success\":true,\"message\":\"Vehicle added!\",\"vehicle\":" + vehicleToJson(saved) + "}");
                } catch (Exception e) {
                    sendJson(exchange, 400,
                        "{\"success\":false,\"message\":\"" + esc(e.getMessage()) + "\"}");
                }

            } else if ("PUT".equals(method)) {
                // ── Update vehicle ─────────────────────────────────────────────
                String query = exchange.getRequestURI().getQuery();
                String vehicleId = null;
                if (query != null) {
                    for (String p : query.split("&")) {
                        String[] kv = p.split("=", 2);
                        if (kv.length == 2 && kv[0].equals("id"))
                            vehicleId = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    }
                }
                if (vehicleId == null || vehicleId.isEmpty()) {
                    sendJson(exchange, 400, "{\"success\":false,\"message\":\"Vehicle id required\"}");
                    return;
                }

                java.util.Optional<Vehicle> existing = vehicleService.findById(vehicleId);
                if (!existing.isPresent()) {
                    sendJson(exchange, 404, "{\"success\":false,\"message\":\"Vehicle not found\"}");
                    return;
                }

                String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");
                Vehicle v = existing.get();
                for (String pair : body.split("&")) {
                    String[] kv = pair.split("=", 2);
                    if (kv.length < 2) continue;
                    String key = kv[0];
                    String val = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    switch (key) {
                        case "make":          v.setMake(val); break;
                        case "model":         v.setModel(val); break;
                        case "year":          try { v.setYear(Integer.parseInt(val)); } catch (NumberFormatException ignored) {} break;
                        case "license_plate": v.setLicensePlate(val); break;
                        case "color":         v.setColor(val); break;
                        case "fuel_type":     v.setFuelType(val); break;
                        case "mileage":       try { v.setMileage(Double.parseDouble(val)); } catch (NumberFormatException ignored) {} break;
                    }
                }
                try {
                    Vehicle updated = vehicleService.update(v);
                    sendJson(exchange, 200, "{\"success\":true,\"message\":\"Vehicle updated!\",\"vehicle\":" + vehicleToJson(updated) + "}");
                } catch (Exception e) {
                    sendJson(exchange, 400, "{\"success\":false,\"message\":\"" + esc(e.getMessage()) + "\"}");
                }

            } else if ("DELETE".equals(method)) {
                // ── Delete vehicle ─────────────────────────────────────────────
                String query = exchange.getRequestURI().getQuery();
                String vehicleId = null;
                if (query != null) {
                    for (String p : query.split("&")) {
                        String[] kv = p.split("=", 2);
                        if (kv.length == 2 && kv[0].equals("id"))
                            vehicleId = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    }
                }
                if (vehicleId == null || vehicleId.isEmpty()) {
                    sendJson(exchange, 400, "{\"success\":false,\"message\":\"Vehicle id required\"}");
                    return;
                }
                boolean removed = vehicleService.remove(vehicleId);
                if (removed) {
                    sendJson(exchange, 200, "{\"success\":true,\"message\":\"Vehicle deleted\"}");
                } else {
                    sendJson(exchange, 404, "{\"success\":false,\"message\":\"Vehicle not found\"}");
                }

            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    // ── Sign-Up Handler ────────────────────────────────────────────────────────
    private class SignUpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
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

                    sendJson(exchange, 200, "{\"success\":true, \"message\":\"Account created successfully!\"}");
                } catch (Exception e) {
                    sendJson(exchange, 400, "{\"success\":false, \"message\":\"" + esc(e.getMessage()) + "\"}");
                }
            }
        }
    }

    // ── Login Handler ──────────────────────────────────────────────────────────
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
                    User u = userOpt.get();
                    String name = u.getFullName();
                    String userEmail = u.getEmail();
                    String userId = u.getId();
                    sendJson(exchange, 200,
                        "{\"success\":true, \"message\":\"Login successful!\", \"id\":\"" + esc(userId) + "\", \"name\":\"" + esc(name) + "\", \"email\":\"" + esc(userEmail) + "\"}");
                } else {
                    sendJson(exchange, 401, "{\"success\":false, \"message\":\"Invalid email or password.\"}");
                }
            }
        }
    }
}
