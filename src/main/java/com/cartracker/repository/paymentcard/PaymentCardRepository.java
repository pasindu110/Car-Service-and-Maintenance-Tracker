package com.cartracker.repository.paymentcard;

import com.cartracker.model.paymentcard.PaymentCard;

import java.util.List;
import java.util.Optional;

/**
 * PaymentCardRepository – defines the contract for PaymentCard persistence.
 *
 * Demonstrates ABSTRACTION: the service layer depends only on this interface
 * and has no knowledge of whether data is stored in files or a database.
 */
public interface PaymentCardRepository {

    /**
     * Persists a new PaymentCard and returns the saved instance.
     */
    PaymentCard save(PaymentCard card);

    /**
     * Retrieves a PaymentCard by its unique ID.
     */
    Optional<PaymentCard> findById(String id);

    /**
     * Returns all stored PaymentCards.
     */
    List<PaymentCard> findAll();

    /**
     * Replaces an existing PaymentCard with the provided updated instance.
     *
     * @return the updated card, or null if the ID was not found
     */
    PaymentCard update(PaymentCard card);

    /**
     * Removes the PaymentCard with the given ID.
     *
     * @return true if deleted, false if the ID did not exist
     */
    boolean deleteById(String id);
}
