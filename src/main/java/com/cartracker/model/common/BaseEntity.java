package com.cartracker.model.common;

import java.time.LocalDateTime;

/**
 * BaseEntity – abstract base class shared by ALL domain entities.
 *
 * Demonstrates INHERITANCE: every entity (User, Vehicle, etc.) extends this.
 * Provides common fields so team members don't duplicate boilerplate.
 *
 * Fields every entity gets automatically:
 *   - id          : unique identifier (String UUID or auto-incremented int)
 *   - createdAt   : when the record was first created
 *   - updatedAt   : last modification timestamp
 */
public abstract class BaseEntity {

    protected String id;            // Unique identifier
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    protected BaseEntity() {
        // TODO: Use IdGenerator.generate() to auto-assign id
        // TODO: Set createdAt = LocalDateTime.now()
    }

    protected BaseEntity(String id) {
        this.id        = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ── Abstract hook ─────────────────────────────────────────────────────────

    /**
     * Every entity must be serialisable to a single CSV/text line
     * for flat-file persistence.
     */
    public abstract String toFileString();

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getId()                    { return id; }
    public void   setId(String id)           { this.id = id; }

    public LocalDateTime getCreatedAt()                     { return createdAt; }
    public void          setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt()                     { return updatedAt; }
    public void          setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ── toString ──────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "BaseEntity{id='" + id + "', createdAt=" + createdAt + '}';
    }
}
