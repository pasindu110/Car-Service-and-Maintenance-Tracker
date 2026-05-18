package com.cartracker.controller.feedback;

import com.cartracker.dto.FeedbackRequest;
import com.cartracker.model.feedback.Feedback;
import com.cartracker.service.feedback.FeedbackService;

import java.util.List;
import java.util.Optional;

/**
 * FeedbackController – entry point for all Feedback CRUD operations.
 *
 * Demonstrates SEPARATION OF CONCERNS:
 *   • The controller ONLY delegates to the service layer.
 *   • It contains NO business logic and NO data-access code.
 *
 * Exposed operations (maps to CRUD):
 *   createFeedback()   → POST   /api/feedback
 *   getFeedback()      → GET    /api/feedback/{id}
 *   getAllFeedbacks()   → GET    /api/feedback
 *   updateFeedback()   → PUT    /api/feedback/{id}
 *   deleteFeedback()   → DELETE /api/feedback/{id}
 *
 * Cross-module queries:
 *   getFeedbackByUser()    → GET /api/feedback/user/{userId}
 *   getFeedbackByService() → GET /api/feedback/service/{serviceId}
 *   getAverageRating()     → GET /api/feedback/service/{serviceId}/average
 *
 * In a CLI application, call these methods directly from your main menu.
 * In a web application, wire the HTTP handler to these methods
 * (see FeedbackHttpHandler for the built-in HttpServer integration).
 */
public class FeedbackController {

    private final FeedbackService feedbackService;

    // ── Constructor (Dependency Injection) ────────────────────────────────────

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // ── createFeedback ────────────────────────────────────────────────────────

    /**
     * POST /api/feedback
     *
     * Accepts a FeedbackRequest DTO, delegates to the service, and returns
     * the newly created Feedback entity.
     *
     * @param request DTO containing userId, serviceId, rating, comment
     * @return created Feedback entity
     */
    public Feedback createFeedback(FeedbackRequest request) {
        System.out.println("[FeedbackController] createFeedback – userId=" + request.getUserId()
                + ", serviceId=" + request.getServiceId()
                + ", rating="   + request.getRating());

        return feedbackService.createFeedback(
                request.getUserId(),
                request.getServiceId(),
                request.getRating(),
                request.getComment()
        );
    }

    // ── getFeedback ───────────────────────────────────────────────────────────

    /**
     * GET /api/feedback/{feedbackId}
     *
     * @param feedbackId the UUID of the feedback record
     * @return Optional containing the Feedback, or empty if not found
     */
    public Optional<Feedback> getFeedback(String feedbackId) {
        System.out.println("[FeedbackController] getFeedback – id=" + feedbackId);
        return feedbackService.getFeedback(feedbackId);
    }

    // ── getAllFeedbacks ────────────────────────────────────────────────────────

    /**
     * GET /api/feedback
     *
     * Returns every feedback record in the system (admin use).
     *
     * @return list of all Feedback entities
     */
    public List<Feedback> getAllFeedbacks() {
        System.out.println("[FeedbackController] getAllFeedbacks");
        return feedbackService.getAllFeedbacks();
    }

    // ── updateFeedback ────────────────────────────────────────────────────────

    /**
     * PUT /api/feedback/{feedbackId}
     *
     * Allows a user to change their star rating and/or comment.
     *
     * @param feedbackId the ID of the feedback to update
     * @param newRating  updated rating (1–5)
     * @param newComment updated comment text
     * @return the updated Feedback entity
     */
    public Feedback updateFeedback(String feedbackId, int newRating, String newComment) {
        System.out.println("[FeedbackController] updateFeedback – id=" + feedbackId
                + ", newRating=" + newRating);
        return feedbackService.updateFeedback(feedbackId, newRating, newComment);
    }

    // ── deleteFeedback ────────────────────────────────────────────────────────

    /**
     * DELETE /api/feedback/{feedbackId}
     *
     * Permanently removes a feedback record.
     *
     * @param feedbackId the ID of the feedback to delete
     * @return true if deleted; false if not found
     */
    public boolean deleteFeedback(String feedbackId) {
        System.out.println("[FeedbackController] deleteFeedback – id=" + feedbackId);
        return feedbackService.deleteFeedback(feedbackId);
    }

    // ── Cross-module query helpers ────────────────────────────────────────────

    /**
     * GET /api/feedback/user/{userId}
     *
     * Returns all feedback submitted by a specific user.
     * Integration point: User Module.
     *
     * @param userId the user's ID
     * @return list of Feedback records belonging to that user
     */
    public List<Feedback> getFeedbackByUser(String userId) {
        System.out.println("[FeedbackController] getFeedbackByUser – userId=" + userId);
        return feedbackService.getFeedbackByUser(userId);
    }

    /**
     * GET /api/feedback/service/{serviceId}
     *
     * Returns all feedback for a specific service record.
     * Integration point: Service Module.
     *
     * @param serviceId the service record's ID
     * @return list of Feedback records for that service
     */
    public List<Feedback> getFeedbackByService(String serviceId) {
        System.out.println("[FeedbackController] getFeedbackByService – serviceId=" + serviceId);
        return feedbackService.getFeedbackByService(serviceId);
    }

    /**
     * GET /api/feedback/service/{serviceId}/average
     *
     * Returns the average star rating for a service record.
     * Integration point: Service Module.
     *
     * @param serviceId the service record's ID
     * @return average rating (1.0–5.0), or 0.0 if no reviews exist
     */
    public double getAverageRating(String serviceId) {
        double avg = feedbackService.getAverageRating(serviceId);
        System.out.println("[FeedbackController] getAverageRating – serviceId=" + serviceId + " → " + avg);
        return avg;
    }
}
