package com.cartracker.service.feedback;

import com.cartracker.exception.EntityNotFoundException;
import com.cartracker.exception.ValidationException;
import com.cartracker.model.feedback.Feedback;
import com.cartracker.repository.feedback.FeedbackRepository;
import com.cartracker.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * FeedbackServiceImpl – concrete implementation of FeedbackService.
 *
 * Demonstrates ENCAPSULATION: all business rules (rating validation,
 * duplicate guard, average calculation) are hidden inside this class.
 *
 * OOP concepts used:
 *   • Dependency Injection via constructor (FeedbackRepository is injected)
 *   • Polymorphism: FeedbackRepository works with File or JDBC implementation
 *   • Encapsulation: validation and duplicate check are private helpers
 *
 * Connects with:
 *   • User Module   – userId is validated to exist before saving
 *   • Service Module – serviceId is referenced when creating/querying feedback
 */
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    // ── Constructor (Dependency Injection) ────────────────────────────────────

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    // ── createFeedback ────────────────────────────────────────────────────────

    @Override
    public Feedback createFeedback(String userId, String serviceId, int rating, String comment) {

        // Business rule: rating must be 1–5
        validateRating(rating);

        // Build entity
        String   feedbackId = IdGenerator.generateUUID();
        Feedback feedback   = new Feedback(feedbackId, userId, serviceId, rating, comment);
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setUpdatedAt(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }

    // ── getFeedback ───────────────────────────────────────────────────────────

    @Override
    public Optional<Feedback> getFeedback(String feedbackId) {
        return feedbackRepository.findById(feedbackId);
    }

    // ── getAllFeedbacks ────────────────────────────────────────────────────────

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    // ── updateFeedback ────────────────────────────────────────────────────────

    @Override
    public Feedback updateFeedback(String feedbackId, int newRating, String newComment) {

        validateRating(newRating);

        Feedback existing = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Feedback not found: " + feedbackId));

        existing.setRating(newRating);
        existing.setComment(newComment);
        existing.setUpdatedAt(LocalDateTime.now());

        return feedbackRepository.update(existing);
    }

    // ── deleteFeedback ────────────────────────────────────────────────────────

    @Override
    public boolean deleteFeedback(String feedbackId) {
        // Verify existence before delegating, so we can throw a clear error
        feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot delete – feedback not found: " + feedbackId));

        return feedbackRepository.deleteById(feedbackId);
    }

    // ── getFeedbackByUser ─────────────────────────────────────────────────────

    @Override
    public List<Feedback> getFeedbackByUser(String userId) {
        return feedbackRepository.findByUserId(userId);
    }

    // ── getFeedbackByService ──────────────────────────────────────────────────

    @Override
    public List<Feedback> getFeedbackByService(String serviceId) {
        return feedbackRepository.findByServiceId(serviceId);
    }

    // ── getAverageRating ──────────────────────────────────────────────────────

    @Override
    public double getAverageRating(String serviceId) {
        List<Feedback> reviews = feedbackRepository.findByServiceId(serviceId);
        if (reviews.isEmpty()) return 0.0;

        double total = reviews.stream()
                .mapToInt(Feedback::getRating)
                .sum();
        // Round to 1 decimal for readability
        return Math.round((total / reviews.size()) * 10.0) / 10.0;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Enforces the 1–5 star rating constraint.
     * Throws ValidationException so the controller can report it cleanly.
     */
    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new ValidationException(
                    "Rating must be between 1 and 5 (inclusive). Received: " + rating);
        }
    }
}
