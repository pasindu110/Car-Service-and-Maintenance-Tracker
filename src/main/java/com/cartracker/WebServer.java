package com.cartracker;

import com.cartracker.model.appointment.Appointment;
import com.cartracker.model.common.Status;
import com.cartracker.model.user.Customer;
import com.cartracker.model.user.User;
import com.cartracker.model.vehicle.Vehicle;
import com.cartracker.repository.vehicle.VehicleRepository;
import com.cartracker.service.appointment.AppointmentService;
import com.cartracker.service.user.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WebServer {

    private final UserService userService;
    private final AppointmentService appointmentService;
    private final VehicleRepository vehicleRepository;

    public WebServer(UserService userService, AppointmentService appointmentService, VehicleRepository vehicleRepository) {
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.vehicleRepository = vehicleRepository;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/", new StaticFileHandler());
        server.createContext("/api/signup", new SignUpHandler());
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/appointments", new AppointmentsHandler());
        server.createContext("/api/vehicles", new VehiclesHandler());
        
        server.setExecutor(null);
        server.start();
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
                    String userId = userOpt.get().getId();
                    String name = userOpt.get().getFullName();
                    String userEmail = userOpt.get().getEmail();
                    String response = "{\"success\":true, \"message\":\"Login successful!\", \"id\":\"" + userId + "\", \"name\":\"" + name + "\", \"email\":\"" + userEmail + "\"}";
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

    // ── /api/appointments  (GET = list all, POST = create) ───────────────────
    private class AppointmentsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // CORS headers so the browser page can call this API
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
            exchange.getResponseHeaders().set("Content-Type", "application/json");

            String method = exchange.getRequestMethod();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("GET".equalsIgnoreCase(method)) {
                // Return all appointments as JSON array
                try {
                    List<Appointment> list = appointmentService.findAll();
                    StringBuilder sb = new StringBuilder("[");
                    for (int i = 0; i < list.size(); i++) {
                        if (i > 0) sb.append(",");
                        sb.append(toJson(list.get(i)));
                    }
                    sb.append("]");
                    byte[] bytes = sb.toString().getBytes("UTF-8");
                    exchange.sendResponseHeaders(200, bytes.length);
                    try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
                } catch (Exception e) {
                    sendError(exchange, 500, "Failed to fetch appointments: " + e.getMessage());
                }

            } else if ("POST".equalsIgnoreCase(method)) {
                // Create new appointment from JSON body
                try {
                    String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");

                    // Simple JSON field extractor (no external library needed)
                    String customerId  = extractJson(body, "customerId");
                    String vehicleId   = extractJson(body, "vehicleId");
                    String serviceType = extractJson(body, "serviceType");
                    String scheduledAt = extractJson(body, "scheduledAt");  // "2026-05-20T09:00"
                    String notes       = extractJson(body, "notes");

                    if (customerId.isEmpty() || vehicleId.isEmpty() || serviceType.isEmpty() || scheduledAt.isEmpty()) {
                        sendError(exchange, 400, "Missing required fields: customerId, vehicleId, serviceType, scheduledAt");
                        return;
                    }

                    Appointment appt = new Appointment();
                    appt.setId("APT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                    appt.setCustomerId(customerId);
                    appt.setVehicleId(vehicleId);
                    appt.setServiceType(serviceType);
                    appt.setScheduledAt(LocalDateTime.parse(scheduledAt, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
                    appt.setNotes(notes);
                    appt.setStatus(Status.PENDING);
                    appt.setCreatedAt(LocalDateTime.now());
                    appt.setUpdatedAt(LocalDateTime.now());

                    Appointment saved = appointmentService.book(appt);

                    String response = "{\"success\":true,\"appointment\":" + toJson(saved) + "}";
                    byte[] bytes = response.getBytes("UTF-8");
                    exchange.sendResponseHeaders(201, bytes.length);
                    try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }

                } catch (Exception e) {
                    sendError(exchange, 500, "Failed to create appointment: " + e.getMessage());
                }

            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }

        private void sendError(HttpExchange exchange, int code, String msg) throws IOException {
            String response = "{\"success\":false,\"message\":\"" + msg.replace("\"", "'") + "\"}";
            byte[] bytes = response.getBytes("UTF-8");
            exchange.sendResponseHeaders(code, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
        }

        /** Converts an Appointment to a JSON object string. */
        private String toJson(Appointment a) {
            return "{"
                + "\"id\":\""          + a.getId()          + "\","
                + "\"customerId\":\"" + a.getCustomerId()  + "\","
                + "\"vehicleId\":\""  + a.getVehicleId()   + "\","
                + "\"mechanicId\":\"" + (a.getMechanicId() != null ? a.getMechanicId() : "") + "\","
                + "\"scheduledAt\":\""+ a.getScheduledAt().toString()  + "\","
                + "\"serviceType\":\""+  a.getServiceType() + "\","
                + "\"notes\":\""       + (a.getNotes() != null ? a.getNotes().replace("\"", "'") : "") + "\","
                + "\"status\":\""      + a.getStatus().name() + "\","
                + "\"createdAt\":\""  + a.getCreatedAt().toString()   + "\""
                + "}";
        }

        /** Very lightweight JSON string-field extractor. */
        private String extractJson(String json, String key) {
            String search = "\"" + key + "\"";
            int idx = json.indexOf(search);
            if (idx < 0) return "";
            int colon = json.indexOf(':', idx + search.length());
            if (colon < 0) return "";
            int start = json.indexOf('"', colon + 1);
            if (start < 0) return "";
            int end = json.indexOf('"', start + 1);
            // handle escaped quotes
            while (end > 0 && json.charAt(end - 1) == '\\') end = json.indexOf('"', end + 1);
            if (end < 0) return "";
            return json.substring(start + 1, end);
        }
    }

    // ── /api/vehicles?ownerId=xxx  (GET only) ───────────────────────
    private class VehiclesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Content-Type", "application/json");

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            // Parse ?ownerId=xxx from query string
            String query = exchange.getRequestURI().getQuery(); // e.g. "ownerId=abc-123"
            String ownerId = "";
            if (query != null) {
                for (String param : query.split("&")) {
                    String[] kv = param.split("=", 2);
                    if (kv.length == 2 && kv[0].equals("ownerId")) {
                        ownerId = java.net.URLDecoder.decode(kv[1], "UTF-8");
                    }
                }
            }

            try {
                List<Vehicle> vehicles = ownerId.isEmpty()
                        ? vehicleRepository.findAll()
                        : vehicleRepository.findByOwnerId(ownerId);

                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < vehicles.size(); i++) {
                    if (i > 0) sb.append(",");
                    Vehicle v = vehicles.get(i);
                    sb.append("{"
                        + "\"id\":\"" + v.getId() + "\","
                        + "\"licensePlate\":\"" + v.getLicensePlate() + "\","
                        + "\"make\":\"" + v.getMake() + "\","
                        + "\"model\":\"" + v.getModel() + "\","
                        + "\"year\":" + v.getYear()
                        + "}");
                }
                sb.append("]");
                byte[] bytes = sb.toString().getBytes("UTF-8");
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
            } catch (Exception e) {
                String err = "{\"success\":false,\"message\":\"" + e.getMessage().replace("\"", "'") + "\"}";
                byte[] bytes = err.getBytes("UTF-8");
                exchange.sendResponseHeaders(500, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
            }
        }
    }
}
