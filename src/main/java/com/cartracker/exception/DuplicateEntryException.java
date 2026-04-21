package com.cartracker.exception;

/**
 * DuplicateEntryException – thrown when a unique constraint is violated.
 *
 * Usage:
 *   throw new DuplicateEntryException("Username", username);
 *   throw new DuplicateEntryException("License plate", plate);
 */
public class DuplicateEntryException extends CarTrackerException {

    public DuplicateEntryException(String field, String value) {
        super(field + " '" + value + "' already exists in the system.");
    }
}
