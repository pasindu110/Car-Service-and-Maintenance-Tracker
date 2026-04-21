package com.cartracker.repository.feedback;

import com.cartracker.model.feedback.Feedback;

import java.util.List;
import java.util.Optional;

/**
 * FeedbackRepository – data access interface for Feedback entities.
 *
 * Demonstrates INTERFACE / ABSTRACTION: the service layer depends only on
 * this interface, so the storage back-end (file, DB, etc.) can be swapped
 * without touching business logic.
 *
 * Team member: assign to the Feedback module owner.
 */
public interface FeedbackRepository {

    /** Persist a new Feedback record and return the saved entity. */
    Feedback          save(Feedback feedback);

    /** Retrieve a single Feedback by its unique ID. */
    Optional<Feedback> findById(String id);

    /** Retrieve all Feedback records. */
    List<Feedback>    findAll();

    /** Retrieve all Feedback submitted by a specific customer. */
    List<Feedback>    findByCustomerId(String customerId);

    /** Retrieve all Feedback linked to a specific service record. */
    List<Feedback>    findByServiceRecordId(String serviceRecordId);

    /** Update an existing Feedback record (e.g. edit comment). */
    Feedback          update(Feedback feedback);

    /** Delete a Feedback record by ID. Returns true if deleted. */
    boolean           deleteById(String id);
}
