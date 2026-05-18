package com.cartracker.service.feedback;

import com.cartracker.model.feedback.Feedback;

import java.util.List;
import java.util.Optional;

/**
 * FeedbackService – business-logic interface for the Feedback module.
 *
 * Demonstrates ABSTRACTION: the controller depends only on this interface,
 * not on the concrete implementation, keeping layers loosely coupled.
 *
 * CRUD methods:
 *   createFeedback  → createFeedback()
 *   getFeedback     → getFeedback()
 *   getAllFeedbacks  → getAllFeedbacks()
 *   updateFeedback  → updateFeedback()
 *   deleteFeedback  → deleteFeedback()
 *
 * Cross-module connections:
 *   - getFeedbackByUser()    (User Module)
 *   - getFeedbackByService() (Service Module)
 *   - getAverageRating()     (Service Module — star-rating summary)
 */
public interface FeedbackService {

    // ── createFeedback ────────────────────────────────────────────────────────

    /**
     * Create and persist new feedback for a completed service.
     *
     * Business rules enforced:
     *   • rating must be between 1 and 5 (inclusive)
     *   • a user may submit only ONE feedback per service record
     *
     * @param userId    ID of the user submitting feedback  (User Module)
     * @param serviceId ID of the service record being reviewed (Service Module)
     * @param rating    star rating 1–5
     * @param comment   optional free-text (null is accepted)
     * @return the persisted Feedback entity
     * @throws com.cartracker.exception.ValidationException if rating is out of range
     *         or duplicate feedback is detected
     */
    Feedback createFeedback(String userId, String serviceId, int rating, String comment);

    // ── getFeedback ───────────────────────────────────────────────────────────

    /**
     * Retrieve a single feedback record by its ID.
     *
     * @param feedbackId the unique feedback ID
     * @return Optional containing the Feedback, or empty if not found
     */
    Optional<Feedback> getFeedback(String feedbackId);

    // ── getAllFeedbacks ────────────────────────────────────────────────────────

    /**
     * Retrieve every feedback record in the system (admin view).
     *
     * @return list of all Feedback entities; empty list if none
     */
    List<Feedback> getAllFeedbacks();

    // ── updateFeedback ────────────────────────────────────────────────────────

    /**
     * Update the rating and/or comment of an existing feedback record.
     *
     * @param feedbackId the ID of the feedback to update
     * @param newRating  new rating value (must be 1–5)
     * @param newComment new comment text (null clears the comment)
     * @return the updated Feedback entity
     * @throws com.cartracker.exception.EntityNotFoundException if feedback not found
     * @throws com.cartracker.exception.ValidationException     if rating is invalid
     */
    Feedback updateFeedback(String feedbackId, int newRating, String newComment);

    // ── deleteFeedback ────────────────────────────────────────────────────────

    /**
     * Permanently delete a feedback record.
     *
     * @param feedbackId the ID of the feedback to delete
     * @return true if deleted; false if not found
     * @throws com.cartracker.exception.EntityNotFoundException if feedback not found
     */
    boolean deleteFeedback(String feedbackId);

    // ── Cross-module helpers ──────────────────────────────────────────────────

    /**
     * Retrieve all feedback submitted by a specific user.
     * Integration point: User Module.
     *
     * @param userId the user's ID
     * @return list of that user's feedback records
     */
    List<Feedback> getFeedbackByUser(String userId);

    /**
     * Retrieve all feedback for a specific service record.
     * Integration point: Service Module.
     *
     * @param serviceId the service record's ID
     * @return list of feedback for that service
     */
    List<Feedback> getFeedbackByService(String serviceId);

    /**
     * Calculate the average star rating for a service record.
     * Integration point: Service Module.
     *
     * @param serviceId the service record's ID
     * @return average rating (1.0–5.0), or 0.0 if no feedback exists
     */
    double getAverageRating(String serviceId);
}