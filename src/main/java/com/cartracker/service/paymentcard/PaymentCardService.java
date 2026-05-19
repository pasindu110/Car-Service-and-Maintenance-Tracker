package com.cartracker.service.paymentcard;

import com.cartracker.dto.SavePaymentCardRequest;
import com.cartracker.model.paymentcard.PaymentCard;

import java.util.List;
import java.util.Optional;

/**
 * PaymentCardService – defines the business operations for payment card management.
 *
 * Demonstrates ABSTRACTION: callers depend on this interface;
 * the concrete implementation is swappable (file-based, DB-backed, etc.).
 */
public interface PaymentCardService {

    /**
     * Validates and saves a new payment card.
     *
     * @param request raw form data from the frontend
     * @return the persisted PaymentCard
     * @throws com.cartracker.exception.ValidationException if input is invalid
     */
    PaymentCard saveCard(SavePaymentCardRequest request);

    /**
     * Retrieves a single payment card by its ID.
     */
    Optional<PaymentCard> findById(String id);

    /**
     * Returns all stored payment cards.
     */
    List<PaymentCard> findAll();

    /**
     * Validates and updates an existing payment card.
     *
     * @param id      ID of the card to update
     * @param request updated form data
     * @return the updated PaymentCard
     * @throws com.cartracker.exception.EntityNotFoundException if the card does not exist
     * @throws com.cartracker.exception.ValidationException     if input is invalid
     */
    PaymentCard updateCard(String id, SavePaymentCardRequest request);

    /**
     * Deletes the payment card with the given ID.
     *
     * @return true if deleted
     * @throws com.cartracker.exception.EntityNotFoundException if the card does not exist
     */
    boolean deleteCard(String id);
}
