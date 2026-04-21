package com.cartracker.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * IdGenerator – generates unique IDs for domain entities.
 *
 * Two strategies are provided:
 *   1. UUID-based  : globally unique across systems (recommended for files)
 *   2. Sequential  : simple auto-increment (readable, good for demos)
 *
 * TEAM GUIDELINES:
 *   - Call IdGenerator.generateUUID()      for production-style IDs.
 *   - Call IdGenerator.generateSequential() for readable demo IDs.
 *   - When switching to a database, remove this class and let the DB handle IDs.
 */
public class IdGenerator {

    private static final AtomicInteger counter = new AtomicInteger(1000);

    // ── Private constructor – utility class ───────────────────────────────────
    private IdGenerator() {}

    // ── UUID strategy ─────────────────────────────────────────────────────────

    /**
     * Returns a new random UUID string, e.g. "550e8400-e29b-41d4-a716-446655440000".
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    // ── Sequential strategy ───────────────────────────────────────────────────

    /**
     * Returns the next sequential integer ID as a String.
     * Starts at 1000 for readability. Not thread-safe across JVM restarts.
     *
     * @param prefix optional prefix, e.g. "USR", "VEH", "SVC"
     */
    public static String generateSequential(String prefix) {
        int next = counter.getAndIncrement();
        return (prefix != null && !prefix.isEmpty()) ? prefix + "-" + next
                                                     : String.valueOf(next);
    }

    /** Resets the sequential counter – useful for tests only. */
    public static void resetCounter() {
        counter.set(1000);
    }
}
