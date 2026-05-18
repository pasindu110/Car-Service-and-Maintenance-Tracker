package com.cartracker.dto;

/**
 * FeedbackRequest – DTO (Data Transfer Object) used when a user
 * creates or updates feedback for a completed service.
 *
 * Keeps raw HTTP / UI input separated from the domain Feedback model.
 *
 * Fields:
 *   - userId    : ID of the user submitting feedback       (User Module)
 *   - serviceId : ID of the service record being reviewed  (Service Module)
 *   - rating    : integer from 1 (poor) to 5 (excellent)
 *   - comment   : optional free-text feedback
 *
 * Replaces the old SubmitFeedbackRequest which used customerId/serviceRecordId.
 * The field names now match the Feedback entity exactly.
 */
public class FeedbackRequest {

    private String userId;
    private String serviceId;
    private int    rating;
    private String comment;

    // ── Constructors ──────────────────────────────────────────────────────────

    public FeedbackRequest() {}

    public FeedbackRequest(String userId, String serviceId, int rating, String comment) {
        this.userId    = userId;
        this.serviceId = serviceId;
        this.rating    = rating;
        this.comment   = comment;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getUserId()                      { return userId; }
    public void   setUserId(String userId)         { this.userId = userId; }

    public String getServiceId()                   { return serviceId; }
    public void   setServiceId(String serviceId)   { this.serviceId = serviceId; }

    public int    getRating()                      { return rating; }
    public void   setRating(int rating)            { this.rating = rating; }

    public String getComment()                     { return comment; }
    public void   setComment(String comment)       { this.comment = comment; }

    @Override
    public String toString() {
        return "FeedbackRequest{userId='" + userId + "', serviceId='" + serviceId +
               "', rating=" + rating + ", comment='" + comment + "'}";
    }
}
