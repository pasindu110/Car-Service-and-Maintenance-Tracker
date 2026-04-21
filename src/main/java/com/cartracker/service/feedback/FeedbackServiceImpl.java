package com.cartracker.service.feedback;

import com.cartracker.exception.EntityNotFoundException;
import com.cartracker.exception.ValidationException;
import com.cartracker.model.feedback.Feedback;
import com.cartracker.repository.feedback.FeedbackRepository;
import com.cartracker.util.IdGenerator;

import java.util.List;
import java.util.Optional;

/**
 * FeedbackServiceImpl – concrete implementation of FeedbackService.
 *
 * Demonstrates ENCAPSULATION: all business rules (rating validation,
 * duplicate guard, average calculation) are hidden inside this class.
 *
 * OOP Concepts used:
 *   - Dependency Injection via constructor (FeedbackRepository injected)
 *   - Polymorphism: FeedbackRepository reference works with any implementation
 *   - Encapsulation: validation logic is private
 *
 * Team member: assign to the Feedback module owner.
 */
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    // ── Constructor (Dependency Injection) ────────────────────────────────────

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    // ── Submit ────────────────────────────────────────────────────────────────

    @Override
    public Feedback submit(String customerId, String serviceRecordId,
                           int rating, String comment) {

        // Business rule: rating must be between 1 and 5
        validateRating(rating);

        // Business rule: a customer may only submit ONE feedback per service record
        boolean alreadySubmitted = feedbackRepository
                .findByServiceRecordId(serviceRecordId)
                .stream()
                .anyMatch(f -> f.getCustomerId().equals(customerId));

        if (alreadySubmitted) {
            throw new ValidationException(
                    "Customer " + customerId + " has already submitted feedback for service record " + serviceRecordId);
        }

        // Build and persist the feedback entity
        String id = IdGenerator.generateUUID();
        Feedback feedback = new Feedback(id, customerId, serviceRecordId, rating,
                comment != null ? comment.trim() : "");

        return feedbackRepository.save(feedback);
    }

    // ── FindById ──────────────────────────────────────────────────────────────

    @Override
    public Optional<Feedback> findById(String id) {
        return feedbackRepository.findById(id);
    }

    // ── FindByCustomer ────────────────────────────────────────────────────────

    @Override
    public List<Feedback> findByCustomer(String customerId) {
        return feedbackRepository.findByCustomerId(customerId);
    }

    // ── FindByServiceRecord ───────────────────────────────────────────────────

    @Override
    public List<Feedback> findByServiceRecord(String serviceRecordId) {
        return feedbackRepository.findByServiceRecordId(serviceRecordId);
    }

    // ── FindAll ───────────────────────────────────────────────────────────────

    @Override
    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    // ── GetAverageRating ──────────────────────────────────────────────────────

    @Override
    public double getAverageRating(String serviceRecordId) {
        List<Feedback> feedbacks = feedbackRepository.findByServiceRecordId(serviceRecordId);
        if (feedbacks.isEmpty()) return 0.0;

        double sum = feedbacks.stream()
                .mapToInt(Feedback::getRating)
                .sum();
        return sum / feedbacks.size();
    }

    // ── Edit ──────────────────────────────────────────────────────────────────

    @Override
    public Feedback edit(String feedbackId, int newRating, String newComment) {
        validateRating(newRating);

        Feedback existing = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found: " + feedbackId));

        existing.setRating(newRating);
        existing.setComment(newComment != null ? newComment.trim() : "");

        return feedbackRepository.update(existing);
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Override
    public boolean delete(String feedbackId) {
        boolean deleted = feedbackRepository.deleteById(feedbackId);
        if (!deleted) {
            throw new EntityNotFoundException("Feedback not found: " + feedbackId);
        }
        return true;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /** Validates that the rating is in the range [1, 5]. */
    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be between 1 and 5. Received: " + rating);
        }
    }
}
