package com.cartracker.exception;

/**
 * CarTrackerException – base unchecked exception for the application.
 *
 * All custom exceptions should extend this class so callers only need
 * to catch one type for general error handling.
 *
 * Usage:
 *   throw new CarTrackerException("Something went wrong: " + detail);
 */
public class CarTrackerException extends RuntimeException {

    public CarTrackerException(String message) {
        super(message);
    }

    public CarTrackerException(String message, Throwable cause) {
        super(message, cause);
    }
}
