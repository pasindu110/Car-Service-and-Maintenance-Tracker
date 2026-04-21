package com.cartracker.service.feedback;

import com.cartracker.model.feedback.Feedback;

import java.util.List;
import java.util.Optional;

/**
 * FeedbackService – business logic interface for the User Feedback module.
 *
 * Demonstrates ABSTRACTION: controllers depend on this interface, not the
 * concrete implementation, keeping layers decoupled.
 *
 * Team member: assign to the Feedback module owner.
 */
public interface FeedbackService {

    /**
     * Submit new feedback for a completed service.
     *
     * @param customerId      ID of the customer submitting feedback
     * @param serviceRecordId ID of the completed service record
     * @param rating          Score from 1 (poor) to 5 (excellent)
     * @param comment         Optional free-text comment
     * @return the saved Feedback entity
     */
    Feedback submit(String customerId, String serviceRecordId, int rating, String comment);

    /** Retrieve a single feedback entry by ID. */
    Optional<Feedback> findById(String id);

    /** Retrieve all feedback submitted by a specific customer. */
    List<Feedback> findByCustomer(String customerId);

    /** Retrieve all feedback linked to a specific service record. */
    List<Feedback> findByServiceRecord(String serviceRecordId);

    /** Retrieve all feedback records. */
    List<Feedback> findAll();

    /**
     * Calculate the average rating across all feedback for a service record.
     *
     * @param serviceRecordId the service record ID
     * @return average rating, or 0.0 if no feedback exists
     */
    double getAverageRating(String serviceRecordId);

    /**
     * Edit an existing feedback entry (update rating and/or comment).
     *
     * @param feedbackId the ID of the feedback to edit
     * @param newRating  new rating value (1-5)
     * @param newComment new comment text
     * @return the updated Feedback entity
     */
    Feedback edit(String feedbackId, int newRating, String newComment);

    /**
     * Delete a feedback record permanently.
     *
     * @param feedbackId the ID of the feedback to remove
     * @return true if deleted, false if not found
     */
    boolean delete(String feedbackId);
}
