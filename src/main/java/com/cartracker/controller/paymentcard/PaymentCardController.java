package com.cartracker.controller.paymentcard;

import com.cartracker.dto.SavePaymentCardRequest;
import com.cartracker.exception.EntityNotFoundException;
import com.cartracker.exception.ValidationException;
import com.cartracker.model.paymentcard.PaymentCard;
import com.cartracker.service.paymentcard.PaymentCardService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PaymentCardController – HTTP handler that exposes a REST-style API for
 * PaymentCard CRUD over the com.sun.net.httpserver infrastructure.
 *
 * Routes handled (registered under /api/payment-cards):
 *   GET    /api/payment-cards          → list all cards as JSON array
 *   POST   /api/payment-cards          → create a new card
 *   GET    /api/payment-cards/{id}     → get a single card by ID
 *   PUT    /api/payment-cards/{id}     → update an existing card
 *   DELETE /api/payment-cards/{id}     → delete a card
 *
 * All request bodies and responses use JSON.
 * JSON is built/parsed manually (no external library dependency).
 */
public class PaymentCardController implements HttpHandler {

    private final PaymentCardService service;

    private static final Pattern ID_IN_PATH =
            Pattern.compile("^/api/payment-cards/([^/]+)$");

    public PaymentCardController(PaymentCardService service) {
        this.service = service;
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        addCorsHeaders(exchange);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        String method = exchange.getRequestMethod();
        String path   = exchange.getRequestURI().getPath();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        Matcher m  = ID_IN_PATH.matcher(path);
        String  id = m.matches() ? m.group(1) : null;

        try {
            if (id == null) {
                // /api/payment-cards
                switch (method.toUpperCase()) {
                    case "GET":  handleListAll(exchange); break;
                    case "POST": handleCreate(exchange);  break;
                    default:     sendError(exchange, 405, "Method not allowed."); break;
                }
            } else {
                // /api/payment-cards/{id}
                switch (method.toUpperCase()) {
                    case "GET":    handleGetById(exchange, id); break;
                    case "PUT":    handleUpdate(exchange, id);  break;
                    case "DELETE": handleDelete(exchange, id);  break;
                    default:       sendError(exchange, 405, "Method not allowed."); break;
                }
            }
        } catch (ValidationException e) {
            sendError(exchange, 400, e.getMessage());
        } catch (EntityNotFoundException e) {
            sendError(exchange, 404, e.getMessage());
        } catch (Exception e) {
            System.err.println("[PaymentCardController] Unhandled error: " + e.getMessage());
            sendError(exchange, 500, "An unexpected server error occurred.");
        }
    }

    // ── Route handlers ────────────────────────────────────────────────────────

    private void handleListAll(HttpExchange exchange) throws IOException {
        List<PaymentCard> cards = service.findAll();
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < cards.size(); i++) {
            sb.append(toJson(cards.get(i)));
            if (i < cards.size() - 1) sb.append(",");
        }
        sb.append("]");
        sendResponse(exchange, 200, sb.toString());
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        SavePaymentCardRequest req = parseRequestBody(body);
        PaymentCard saved = service.saveCard(req);
        sendResponse(exchange, 201,
                "{\"success\":true,\"id\":\"" + esc(saved.getId()) + "\"}");
    }

    private void handleGetById(HttpExchange exchange, String id) throws IOException {
        Optional<PaymentCard> opt = service.findById(id);
        if (opt.isPresent()) {
            sendResponse(exchange, 200, toJson(opt.get()));
        } else {
            sendError(exchange, 404, "Payment card not found: " + id);
        }
    }

    private void handleUpdate(HttpExchange exchange, String id) throws IOException {
        String body = readBody(exchange);
        SavePaymentCardRequest req = parseRequestBody(body);
        PaymentCard updated = service.updateCard(id, req);
        sendResponse(exchange, 200, toJson(updated));
    }

    private void handleDelete(HttpExchange exchange, String id) throws IOException {
        service.deleteCard(id);
        sendResponse(exchange, 200,
                "{\"success\":true,\"message\":\"Payment card deleted successfully.\"}");
    }

    // ── JSON serialisation ────────────────────────────────────────────────────

    private String toJson(PaymentCard c) {
        String expMonth = String.format("%02d", c.getExpiryMonth());
        String expYear  = String.format("%02d", c.getExpiryYear());
        return "{"
                + "\"id\":\""            + esc(c.getId())            + "\","
                + "\"cardholderName\":\"" + esc(c.getCardholderName()) + "\","
                + "\"maskedNumber\":\""  + esc(c.getMaskedNumber())   + "\","
                + "\"last4Digits\":\""   + esc(c.getLast4Digits())    + "\","
                + "\"expiryMonth\":"     + c.getExpiryMonth()         + ","
                + "\"expiryYear\":"      + c.getExpiryYear()          + ","
                + "\"expiryDisplay\":\"" + expMonth + "/" + expYear   + "\","
                + "\"cardType\":\""      + c.getCardType().name()     + "\","
                + "\"country\":\""       + esc(c.getCountry())        + "\""
                + "}";
    }

    // ── JSON parsing ──────────────────────────────────────────────────────────

    private SavePaymentCardRequest parseRequestBody(String json) {
        SavePaymentCardRequest req = new SavePaymentCardRequest();
        req.setCardholderName(extractString(json, "cardholderName"));
        req.setCardNumber(extractString(json, "cardNumber"));
        req.setExpiry(extractString(json, "expiry"));
        req.setCvv(extractString(json, "cvv"));
        req.setCountry(extractString(json, "country"));
        return req;
    }

    /**
     * Extracts the value of a JSON string field by key using a simple regex.
     * Handles basic escape sequences (\", \\).
     */
    private String extractString(String json, String key) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");
        Matcher m = p.matcher(json);
        if (!m.find()) return "";
        return m.group(1)
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\/", "/");
    }

    // ── I/O helpers ───────────────────────────────────────────────────────────

    private String readBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    private void sendResponse(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void sendError(HttpExchange exchange, int status, String message) throws IOException {
        sendResponse(exchange, status,
                "{\"success\":false,\"message\":\"" + esc(message) + "\"}");
    }

    private void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin",  "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    /** Escapes characters that would break a JSON string literal. */
    private String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
