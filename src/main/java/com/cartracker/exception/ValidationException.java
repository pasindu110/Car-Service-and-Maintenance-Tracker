package com.cartracker.exception;

/**
 * ValidationException – thrown when user input fails business rule validation.
 *
 * Usage:
 *   throw new ValidationException("Email address is invalid.");
 */
public class ValidationException extends CarTrackerException {

    public ValidationException(String message) {
        super("Validation failed: " + message);
    }
}
