package com.cartracker.service.paymentcard;

import com.cartracker.dto.SavePaymentCardRequest;
import com.cartracker.exception.EntityNotFoundException;
import com.cartracker.exception.ValidationException;
import com.cartracker.model.paymentcard.CardType;
import com.cartracker.model.paymentcard.PaymentCard;
import com.cartracker.repository.paymentcard.PaymentCardRepository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * PaymentCardServiceImpl – concrete implementation of PaymentCardService.
 *
 * Responsibilities:
 *   1. Input validation (cardholder name, card number via Luhn, expiry, CVV, country)
 *   2. Security: mask the card number before storage; never store CVV
 *   3. Delegate persistence to the injected repository
 *
 * Demonstrates POLYMORPHISM (implements PaymentCardService) and
 * ENCAPSULATION (all validation logic is private).
 */
public class PaymentCardServiceImpl implements PaymentCardService {

    private final PaymentCardRepository repository;

    public PaymentCardServiceImpl(PaymentCardRepository repository) {
        this.repository = repository;
    }

    // ── Public service methods ────────────────────────────────────────────────

    @Override
    public PaymentCard saveCard(SavePaymentCardRequest request) {
        validateForCreate(request);
        PaymentCard card = new PaymentCard();
        applyRequest(request, card, true);
        return repository.save(card);
    }

    @Override
    public Optional<PaymentCard> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<PaymentCard> findAll() {
        return repository.findAll();
    }

    @Override
    public PaymentCard updateCard(String id, SavePaymentCardRequest request) {
        PaymentCard existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PaymentCard", id));

        boolean hasNewNumber = hasNewCardNumber(request);
        validateForUpdate(request, hasNewNumber);

        applyRequest(request, existing, hasNewNumber);
        existing.setUpdatedAt(LocalDateTime.now());
        return repository.update(existing);
    }

    @Override
    public boolean deleteCard(String id) {
        repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PaymentCard", id));
        return repository.deleteById(id);
    }

    // ── Validation ────────────────────────────────────────────────────────────

    private void validateForCreate(SavePaymentCardRequest req) {
        validateCardholderName(req.getCardholderName());
        validateCardNumber(req.getCardNumber(), true);
        validateExpiry(req.getExpiry());
        // CVV is never stored – only validate format if the user typed something
        if (req.getCvv() != null && !req.getCvv().trim().isEmpty()) {
            validateCvv(req.getCvv(), false);
        }
        validateCountry(req.getCountry());
    }

    private void validateForUpdate(SavePaymentCardRequest req, boolean hasNewNumber) {
        validateCardholderName(req.getCardholderName());
        if (hasNewNumber) {
            validateCardNumber(req.getCardNumber(), true);
        }
        validateExpiry(req.getExpiry());
        if (req.getCvv() != null && !req.getCvv().trim().isEmpty()) {
            validateCvv(req.getCvv(), false);
        }
        validateCountry(req.getCountry());
    }

    private void validateCardholderName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Cardholder name is required.");
        }
        String trimmed = name.trim();
        if (trimmed.length() < 2 || trimmed.length() > 100) {
            throw new ValidationException("Cardholder name must be 2–100 characters.");
        }
        if (!trimmed.matches("[A-Za-z .'-]+")) {
            throw new ValidationException("Cardholder name may only contain letters, spaces, hyphens, apostrophes, and periods.");
        }
    }

    private void validateCardNumber(String cardNumber, boolean required) {
        String raw = cardNumber == null ? "" : cardNumber.replaceAll("\\D", "");
        if (raw.isEmpty()) {
            if (required) throw new ValidationException("Card number is required.");
            return;
        }
        if (raw.length() < 13 || raw.length() > 19) {
            throw new ValidationException("Card number must be 13–19 digits.");
        }
        if (!isValidLuhn(raw)) {
            throw new ValidationException("Card number is invalid. Please check and try again.");
        }
    }

    private void validateExpiry(String expiry) {
        if (expiry == null || expiry.trim().isEmpty()) {
            throw new ValidationException("Expiry date is required.");
        }
        if (!expiry.trim().matches("\\d{2}\\s*/\\s*\\d{2}")) {
            throw new ValidationException("Expiry must be in MM / YY format.");
        }
        String[] parts = expiry.split("/");
        int month = Integer.parseInt(parts[0].trim());
        int year  = Integer.parseInt(parts[1].trim());

        if (month < 1 || month > 12) {
            throw new ValidationException("Expiry month must be between 01 and 12.");
        }
        YearMonth cardExp = YearMonth.of(2000 + year, month);
        if (cardExp.isBefore(YearMonth.now())) {
            throw new ValidationException("This card has expired.");
        }
    }

    private void validateCvv(String cvv, boolean required) {
        if (cvv == null || cvv.trim().isEmpty()) {
            return; // CVV is never stored; it is always optional at card-save time
        }
        if (!cvv.trim().matches("\\d{3,4}")) {
            throw new ValidationException("CVV must be 3 or 4 digits.");
        }
    }

    private void validateCountry(String country) {
        if (country == null || country.trim().isEmpty()) {
            throw new ValidationException("Country is required.");
        }
    }

    // ── Luhn algorithm ────────────────────────────────────────────────────────

    /**
     * Validates a digit-only card number string using the Luhn algorithm.
     */
    private boolean isValidLuhn(String digits) {
        int sum = 0;
        boolean alternate = false;
        for (int i = digits.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(digits.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }

    // ── Mapping helpers ───────────────────────────────────────────────────────

    /**
     * Returns true when the request carries a real new card number
     * (i.e., has at least 13 actual digits – not just masked dots/spaces).
     */
    private boolean hasNewCardNumber(SavePaymentCardRequest req) {
        if (req.getCardNumber() == null) return false;
        String raw = req.getCardNumber().replaceAll("\\D", "");
        return raw.length() >= 13;
    }

    /**
     * Applies validated request data onto a PaymentCard instance.
     *
     * @param applyNumber whether to update card number fields
     */
    private void applyRequest(SavePaymentCardRequest req, PaymentCard card, boolean applyNumber) {
        card.setCardholderName(req.getCardholderName().trim());

        if (applyNumber) {
            String raw   = req.getCardNumber().replaceAll("\\D", "");
            String last4 = raw.substring(raw.length() - 4);
            card.setLast4Digits(last4);
            card.setMaskedNumber("**** **** **** " + last4);
            card.setCardType(CardType.detect(raw));
        }

        String[] expParts = req.getExpiry().split("/");
        card.setExpiryMonth(Integer.parseInt(expParts[0].trim()));
        card.setExpiryYear(Integer.parseInt(expParts[1].trim()));
        card.setCountry(req.getCountry().trim());
    }
}
