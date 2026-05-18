package com.cartracker.repository.feedback;

import com.cartracker.model.feedback.Feedback;

import java.util.List;
import java.util.Optional;

/**
 * FeedbackRepository – data-access interface for Feedback entities.
 *
 * Demonstrates ABSTRACTION / INTERFACE SEGREGATION:
 *   The service layer depends only on this interface; any storage backend
 *   (flat-file, MySQL, H2, …) can be swapped in by providing a new
 *   implementation without touching business logic.
 *
 * CRUD operations exposed:
 *   createFeedback  → save()
 *   getFeedback     → findById()
 *   getAllFeedbacks  → findAll()
 *   updateFeedback  → update()
 *   deleteFeedback  → deleteById()
 *
 * Additional query helpers for cross-module connections:
 *   - findByUserId()    (User Module integration)
 *   - findByServiceId() (Service Module integration)
 */
public interface FeedbackRepository {

    // ── Create ────────────────────────────────────────────────────────────────

    /**
     * Persist a new Feedback record.
     *
     * @param feedback fully populated Feedback entity (id must be pre-generated)
     * @return the saved entity (same reference)
     */
    Feedback save(Feedback feedback);

    // ── Read ──────────────────────────────────────────────────────────────────

    /**
     * Retrieve a single Feedback by its unique feedbackId.
     *
     * @param feedbackId the UUID of the feedback record
     * @return Optional containing the Feedback, or empty if not found
     */
    Optional<Feedback> findById(String feedbackId);

    /**
     * Retrieve ALL feedback records (admin view).
     *
     * @return unmodifiable list of all Feedback entities; empty list if none
     */
    List<Feedback> findAll();

    /**
     * Retrieve all feedback submitted by a specific user.
     * (User Module integration point)
     *
     * @param userId the ID of the user
     * @return list of Feedback; empty if none found
     */
    List<Feedback> findByUserId(String userId);

    /**
     * Retrieve all feedback for a specific service record.
     * (Service Module integration point)
     *
     * @param serviceId the ID of the service record
     * @return list of Feedback; empty if none found
     */
    List<Feedback> findByServiceId(String serviceId);

    // ── Update ────────────────────────────────────────────────────────────────

    /**
     * Persist changes to an existing Feedback record.
     * The feedbackId in the entity is used to locate the record.
     *
     * @param feedback entity with updated fields (rating, comment)
     * @return the updated entity
     */
    Feedback update(Feedback feedback);

    // ── Delete ────────────────────────────────────────────────────────────────

    /**
     * Remove a feedback record permanently.
     *
     * @param feedbackId the ID of the feedback to delete
     * @return true if a record was deleted; false if not found
     */
    boolean deleteById(String feedbackId);
}
