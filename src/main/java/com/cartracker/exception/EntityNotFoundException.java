package com.cartracker.exception;

/**
 * EntityNotFoundException – thrown when a requested entity does not exist.
 *
 * Usage:
 *   throw new EntityNotFoundException("User", userId);
 */
public class EntityNotFoundException extends CarTrackerException {

    // Used when you have a pre-formatted message, e.g. "Feedback not found: abc123"
    public EntityNotFoundException(String message) {
        super(message);
    }

    // Used when you have a separate entity type and id, e.g. ("Feedback", "abc123")
    public EntityNotFoundException(String entityType, String id) {
        super(entityType + " with id '" + id + "' was not found.");
    }
}
