package com.cartracker.handler;

import com.cartracker.controller.feedback.FeedbackController;
import com.cartracker.model.feedback.Feedback;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

/**
 * FeedbackHttpHandler – routes HTTP requests to FeedbackController.
 *
 * Register this handler in WebServer.start():
 *   server.createContext("/api/feedback", new FeedbackHttpHandler(feedbackController));
 *
 * Routes handled:
 *   POST   /api/feedback                         → createFeedback
 *   GET    /api/feedback                         → getAllFeedbacks
 *   GET    /api/feedback/{id}                    → getFeedback
 *   PUT    /api/feedback/{id}                    → updateFeedback
 *   DELETE /api/feedback/{id}                    → deleteFeedback
 *   GET    /api/feedback/user/{userId}           → getFeedbackByUser
 *   GET    /api/feedback/service/{serviceId}     → getFeedbackByService
 *   GET    /api/feedback/service/{serviceId}/average → getAverageRating
 *
 * Request/Response format: JSON (hand-crafted strings to avoid needing a library).
 * For a real project, replace with Jackson or Gson.
 */
public class FeedbackHttpHandler implements HttpHandler {

    private final FeedbackController feedbackController;

    public FeedbackHttpHandler(FeedbackController feedbackController) {
        this.feedbackController = feedbackController;
    }

    private void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
// ... (2 more header lines)
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path   = exchange.getRequestURI().getPath(); // e.g. /api/feedback/abc-123

        // Strip /api/feedback prefix → remaining path segments
        String remaining = path.replaceFirst("^/api/feedback", "").replaceFirst("^/", "");
        // remaining examples: "", "abc-123", "user/uid-1", "service/svc-1", "service/svc-1/average"

        try {
            if (method.equals("GET") && remaining.isEmpty()) {
                // GET /api/feedback  →  getAllFeedbacks
                handleGetAll(exchange);

            } else if (method.equals("POST") && remaining.isEmpty()) {
                // POST /api/feedback  →  createFeedback
                handleCreate(exchange);

            } else if (method.equals("GET") && remaining.startsWith("user/")) {
                // GET /api/feedback/user/{userId}
                String userId = remaining.substring("user/".length());
                handleGetByUser(exchange, userId);

            } else if (method.equals("GET") && remaining.contains("/average")) {
                // GET /api/feedback/service/{serviceId}/average
                String serviceId = remaining.replace("service/", "").replace("/average", "");
                handleGetAverageRating(exchange, serviceId);

            } else if (method.equals("GET") && remaining.startsWith("service/")) {
                // GET /api/feedback/service/{serviceId}
                String serviceId = remaining.substring("service/".length());
                handleGetByService(exchange, serviceId);

            } else if (method.equals("GET") && !remaining.isEmpty()) {
                // GET /api/feedback/{id}
                handleGetById(exchange, remaining);

            } else if (method.equals("PUT") && !remaining.isEmpty()) {
                // PUT /api/feedback/{id}
                handleUpdate(exchange, remaining);

            } else if (method.equals("DELETE") && !remaining.isEmpty()) {
                // DELETE /api/feedback/{id}
                handleDelete(exchange, remaining);

            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            }
        } catch (Exception e) {
            sendResponse(exchange, 500,
                    "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    // ── Route handlers ────────────────────────────────────────────────────────

    private void handleGetAll(HttpExchange ex) throws IOException {
        List<Feedback> list = feedbackController.getAllFeedbacks();
        sendResponse(ex, 200, feedbackListToJson(list));
    }

    private void handleGetById(HttpExchange ex, String id) throws IOException {
        Optional<Feedback> opt = feedbackController.getFeedback(id);
        if (opt.isPresent()) {
            sendResponse(ex, 200, feedbackToJson(opt.get()));
        } else {
            sendResponse(ex, 404, "{\"error\":\"Feedback not found\"}");
        }
    }

    private void handleCreate(HttpExchange ex) throws IOException {
        String body = new String(ex.getRequestBody().readAllBytes());
        // Expected JSON: {"userId":"u1","serviceId":"s1","rating":5,"comment":"Great!"}
        String userId    = extractJsonString(body, "userId");
        String serviceId = extractJsonString(body, "serviceId");
        int    rating    = extractJsonInt(body, "rating");
        String comment   = extractJsonString(body, "comment");

        Feedback created = feedbackController.createFeedback(
                new com.cartracker.dto.FeedbackRequest(userId, serviceId, rating, comment));
        sendResponse(ex, 201, feedbackToJson(created));
    }

    private void handleUpdate(HttpExchange ex, String id) throws IOException {
        String body      = new String(ex.getRequestBody().readAllBytes());
        int    newRating = extractJsonInt(body, "rating");
        String newComment = extractJsonString(body, "comment");

        Feedback updated = feedbackController.updateFeedback(id, newRating, newComment);
        sendResponse(ex, 200, feedbackToJson(updated));
    }

    private void handleDelete(HttpExchange ex, String id) throws IOException {
        boolean deleted = feedbackController.deleteFeedback(id);
        if (deleted) {
            sendResponse(ex, 200, "{\"success\":true,\"message\":\"Feedback deleted\"}");
        } else {
            sendResponse(ex, 404, "{\"error\":\"Feedback not found\"}");
        }
    }

    private void handleGetByUser(HttpExchange ex, String userId) throws IOException {
        List<Feedback> list = feedbackController.getFeedbackByUser(userId);
        sendResponse(ex, 200, feedbackListToJson(list));
    }

    private void handleGetByService(HttpExchange ex, String serviceId) throws IOException {
        List<Feedback> list = feedbackController.getFeedbackByService(serviceId);
        sendResponse(ex, 200, feedbackListToJson(list));
    }

    private void handleGetAverageRating(HttpExchange ex, String serviceId) throws IOException {
        double avg = feedbackController.getAverageRating(serviceId);
        sendResponse(ex, 200, "{\"serviceId\":\"" + serviceId + "\",\"averageRating\":" + avg + "}");
    }

    // ── JSON helpers (no external library required) ───────────────────────────

    private String feedbackToJson(Feedback f) {
        return "{" +
               "\"feedbackId\":\""  + f.getId()        + "\"," +
               "\"userId\":\""      + f.getUserId()     + "\"," +
               "\"serviceId\":\""   + f.getServiceId()  + "\"," +
               "\"rating\":"        + f.getRating()     + ","   +
               "\"comment\":\""     + escapeJson(f.getComment()) + "\"," +
               "\"createdAt\":\""   + (f.getCreatedAt()  != null ? f.getCreatedAt().toString()  : "") + "\"," +
               "\"updatedAt\":\""   + (f.getUpdatedAt()  != null ? f.getUpdatedAt().toString()  : "") + "\"" +
               "}";
    }

    private String feedbackListToJson(List<Feedback> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(feedbackToJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    /** Minimal JSON string extractor – suitable for simple flat JSON bodies. */
    private String extractJsonString(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start == -1) return "";
        start += search.length();
        int end = json.indexOf("\"", start);
        return end == -1 ? "" : json.substring(start, end);
    }

    private int extractJsonInt(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return 0;
        start += search.length();
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) {
            end++;
        }
        try {
            return Integer.parseInt(json.substring(start, end));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }

    // ── HTTP response helper ──────────────────────────────────────────────────

    private void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
