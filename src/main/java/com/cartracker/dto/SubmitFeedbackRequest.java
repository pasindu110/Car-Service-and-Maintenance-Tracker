package com.cartracker.dto;

/**
 * SubmitFeedbackRequest – DTO (Data Transfer Object) used when a customer
 * submits feedback for a completed service.
 *
 * Keeps raw HTTP / UI input separated from the domain model.
 *
 * Fields:
 *   - customerId      : ID of the customer submitting feedback
 *   - serviceRecordId : ID of the service record being reviewed
 *   - rating          : integer from 1 (poor) to 5 (excellent)
 *   - comment         : optional free-text feedback
 */
public class SubmitFeedbackRequest {

    private String customerId;
    private String serviceRecordId;
    private int    rating;
    private String comment;

    // ── Constructors ──────────────────────────────────────────────────────────

    public SubmitFeedbackRequest() {}

    public SubmitFeedbackRequest(String customerId, String serviceRecordId,
                                  int rating, String comment) {
        this.customerId      = customerId;
        this.serviceRecordId = serviceRecordId;
        this.rating          = rating;
        this.comment         = comment;
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
}
