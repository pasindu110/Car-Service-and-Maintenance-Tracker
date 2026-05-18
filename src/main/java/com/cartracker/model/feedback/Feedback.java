package com.cartracker.model.feedback;

import com.cartracker.model.common.BaseEntity;

/**
 * Feedback – a star-rating + optional comment left by a User after a service.
 *
 * Demonstrates INHERITANCE: extends BaseEntity for id / timestamp fields.
 * Demonstrates ENCAPSULATION: all fields are private; access via getters/setters.
 *
 * Fields:
 *   - feedbackId      : unique identifier (inherited as 'id' from BaseEntity)
 *   - userId          : the user who submitted the feedback
 *   - serviceId       : the service record being reviewed
 *   - rating          : score from 1 (poor) to 5 (excellent)
 *   - comment         : optional free-text comment
 *
 * Connects with:
 *   - User Module   (userId references users table)
 *   - Service Module (serviceId references service_records table)
 */
public class Feedback extends BaseEntity {

    private String userId;          // FK → users.id
    private String serviceId;       // FK → service_records.id
    private int    rating;          // 1 – 5 stars
    private String comment;         // optional free-text

    // ── Constructors ──────────────────────────────────────────────────────────

    public Feedback() {
        super();
    }

    /**
     * Full constructor used when loading from DB or file.
     *
     * @param feedbackId unique ID (becomes BaseEntity.id)
     * @param userId     ID of the user submitting feedback
     * @param serviceId  ID of the service record being reviewed
     * @param rating     1–5 star rating
     * @param comment    optional comment (null is stored as empty string)
     */
    public Feedback(String feedbackId, String userId, String serviceId,
                    int rating, String comment) {
        super(feedbackId);
        this.userId    = userId;
        this.serviceId = serviceId;
        this.rating    = rating;
        this.comment   = (comment != null ? comment.trim() : "");
    }

    // ── toFileString ──────────────────────────────────────────────────────────

    /**
     * Serialises the Feedback to a pipe-delimited line for flat-file storage.
     *
     * Format: feedbackId|userId|serviceId|rating|comment|createdAt|updatedAt
     */
    @Override
    public String toFileString() {
        return String.join("|",
                id,
                userId,
                serviceId,
                String.valueOf(rating),
                comment,
                createdAt  != null ? createdAt.toString()  : "",
                updatedAt  != null ? updatedAt.toString()  : ""
        );
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    /** Returns the feedback ID (same as BaseEntity.getId()). */
    public String getFeedbackId()                          { return id; }

    public String getUserId()                              { return userId; }
    public void   setUserId(String userId)                 { this.userId = userId; }

    public String getServiceId()                           { return serviceId; }
    public void   setServiceId(String serviceId)           { this.serviceId = serviceId; }

    public int    getRating()                              { return rating; }
    public void   setRating(int rating)                    { this.rating = rating; }

    public String getComment()                             { return comment; }
    public void   setComment(String comment)               { this.comment = comment != null ? comment.trim() : ""; }

    // ── toString ──────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId='" + id + '\'' +
                ", userId='"   + userId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", rating="    + rating +
                ", comment='"  + comment + '\'' +
                '}';
    }
}
