package com.cartracker.controller.feedback;

import com.cartracker.dto.SubmitFeedbackRequest;
import com.cartracker.model.feedback.Feedback;
import com.cartracker.service.feedback.FeedbackService;

import java.util.List;
import java.util.Optional;

/**
 * FeedbackController – entry point for all User Feedback operations.
 *
 * Demonstrates SEPARATION OF CONCERNS: the controller only delegates to the
 * service; it does NOT contain business logic or data access code.
 *
 * In a CLI application, call these methods from your main menu/UI class.
 * In a web application, map HTTP requests to these methods.
 *
 * Team member: assign to the Feedback module owner.
 */
public class FeedbackController {

    private final FeedbackService feedbackService;

    // ── Constructor (Dependency Injection) ────────────────────────────────────

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // ── Submit Feedback ───────────────────────────────────────────────────────

    /**
     * POST /feedback  (or menu option: "Submit Feedback")
     *
     * Accepts a SubmitFeedbackRequest DTO, delegates to service, and returns
     * the created Feedback entity.
     */
    public Feedback submitFeedback(SubmitFeedbackRequest request) {
        System.out.println("[FeedbackController] Submitting feedback from customer: "
                + request.getCustomerId());

        return feedbackService.submit(
                request.getCustomerId(),
                request.getServiceRecordId(),
                request.getRating(),
                request.getComment()
        );
    }

    // ── Get Feedback by ID ────────────────────────────────────────────────────

    /**
     * GET /feedback/{id}
     */
    public Optional<Feedback> getFeedbackById(String feedbackId) {
        System.out.println("[FeedbackController] Looking up feedback: " + feedbackId);
        return feedbackService.findById(feedbackId);
    }

    // ── Get All Feedback by Customer ──────────────────────────────────────────

    /**
     * GET /feedback/customer/{customerId}
     */
    public List<Feedback> getFeedbackByCustomer(String customerId) {
        System.out.println("[FeedbackController] Fetching all feedback for customer: " + customerId);
        return feedbackService.findByCustomer(customerId);
    }

    // ── Get All Feedback for a Service Record ─────────────────────────────────

    /**
     * GET /feedback/service-record/{serviceRecordId}
     */
    public List<Feedback> getFeedbackForServiceRecord(String serviceRecordId) {
        System.out.println("[FeedbackController] Fetching feedback for service record: " + serviceRecordId);
        return feedbackService.findByServiceRecord(serviceRecordId);
    }

    // ── Get All Feedback ──────────────────────────────────────────────────────

    /**
     * GET /feedback
     */
    public List<Feedback> getAllFeedback() {
        System.out.println("[FeedbackController] Fetching all feedback records.");
        return feedbackService.findAll();
    }

    // ── Get Average Rating ────────────────────────────────────────────────────

    /**
     * GET /feedback/average/{serviceRecordId}
     *
     * Returns the calculated average star rating for a given service record.
     */
    public double getAverageRating(String serviceRecordId) {
        double avg = feedbackService.getAverageRating(serviceRecordId);
        System.out.println("[FeedbackController] Average rating for service record "
                + serviceRecordId + ": " + avg);
        return avg;
    }

    // ── Edit Feedback ─────────────────────────────────────────────────────────

    /**
     * PUT /feedback/{feedbackId}
     *
     * Allows a customer to update their rating and/or comment.
     */
    public Feedback editFeedback(String feedbackId, int newRating, String newComment) {
        System.out.println("[FeedbackController] Editing feedback: " + feedbackId);
        return feedbackService.edit(feedbackId, newRating, newComment);
    }

    // ── Delete Feedback ───────────────────────────────────────────────────────

    /**
     * DELETE /feedback/{feedbackId}
     *
     * Removes a feedback entry permanently. Returns true if deleted.
     */
    public boolean deleteFeedback(String feedbackId) {
        System.out.println("[FeedbackController] Deleting feedback: " + feedbackId);
        return feedbackService.delete(feedbackId);
    }
}
