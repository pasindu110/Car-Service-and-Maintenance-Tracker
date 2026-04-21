package com.cartracker.model.feedback;

import com.cartracker.model.common.BaseEntity;

/**
 * Feedback – a star-rating + comment left by a Customer after a service.
 *
 * Demonstrates INHERITANCE: extends BaseEntity for id / timestamp fields.
 *
 * Fields:
 *   - customerId      : the customer who submitted the feedback
 *   - serviceRecordId : the completed service record being reviewed
 *   - rating          : score from 1 (poor) to 5 (excellent)
 *   - comment         : optional free-text comment
 *
 * Team member: assign to the Feedback module owner.
 */
public class Feedback extends BaseEntity {

    private String customerId;
    private String serviceRecordId;
    private int    rating;          // 1 – 5
    private String comment;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Feedback() {
        super();
    }

    public Feedback(String id, String customerId, String serviceRecordId,
                    int rating, String comment) {
        super(id);
        this.customerId      = customerId;
        this.serviceRecordId = serviceRecordId;
        this.rating          = rating;
        this.comment         = (comment != null ? comment : "");
    }

    // ── toFileString ──────────────────────────────────────────────────────────

    /**
     * CSV layout: id|customerId|serviceRecordId|rating|comment|createdAt
     */
    @Override
    public String toFileString() {
        return String.join("|",
                id,
                customerId,
                serviceRecordId,
                String.valueOf(rating),
                comment,
                createdAt.toString());
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getCustomerId()                            { return customerId; }
    public void   setCustomerId(String customerId)           { this.customerId = customerId; }

    public String getServiceRecordId()                       { return serviceRecordId; }
    public void   setServiceRecordId(String serviceRecordId) { this.serviceRecordId = serviceRecordId; }

    public int    getRating()                                { return rating; }
    public void   setRating(int rating)                      { this.rating = rating; }

    public String getComment()                               { return comment; }
    public void   setComment(String comment)                 { this.comment = comment; }

    // ── toString ──────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Feedback{id='" + id + "', customerId='" + customerId +
               "', serviceRecordId='" + serviceRecordId +
               "', rating=" + rating + ", comment='" + comment + "'}";
    }
}
