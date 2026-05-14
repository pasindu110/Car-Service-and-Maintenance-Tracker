package com.cartracker.model.paymentcard;

import com.cartracker.model.common.BaseEntity;
import com.cartracker.util.IdGenerator;

import java.time.LocalDateTime;

/**
 * PaymentCard – represents a saved payment card belonging to a customer.
 *
 * Security: the full card number is NEVER stored. Only the last 4 digits and
 * a masked display string are persisted. CVV is never stored at all.
 *
 * Demonstrates INHERITANCE (extends BaseEntity) and ENCAPSULATION (private
 * fields exposed only via getters/setters).
 *
 * File format (pipe-delimited):
 *   id|cardholderName|last4Digits|maskedNumber|expiryMonth|expiryYear|cardType|country|createdAt|updatedAt
 */
public class PaymentCard extends BaseEntity {

    private String   cardholderName;
    private String   last4Digits;
    private String   maskedNumber;   // e.g. "**** **** **** 4242"
    private int      expiryMonth;    // 1–12
    private int      expiryYear;     // 2-digit year, e.g. 27
    private CardType cardType;
    private String   country;

    // ── Constructors ──────────────────────────────────────────────────────────

    public PaymentCard() {
        this.id        = IdGenerator.generateUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public PaymentCard(String id) {
        super(id);
    }

    // ── Serialisation ─────────────────────────────────────────────────────────

    @Override
    public String toFileString() {
        return String.join("|",
                id,
                cardholderName != null ? cardholderName : "",
                last4Digits    != null ? last4Digits    : "",
                maskedNumber   != null ? maskedNumber   : "",
                String.valueOf(expiryMonth),
                String.valueOf(expiryYear),
                cardType       != null ? cardType.name() : CardType.UNKNOWN.name(),
                country        != null ? country        : "",
                createdAt.toString(),
                updatedAt.toString()
        );
    }

    /**
     * Reconstructs a PaymentCard from a single pipe-delimited file line.
     *
     * @param line raw line from payment_cards.txt
     * @return PaymentCard instance, or null if the line is malformed
     */
    public static PaymentCard fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.split("\\|", -1);
        if (parts.length < 10) return null;

        try {
            PaymentCard card = new PaymentCard(parts[0]);
            card.cardholderName = parts[1];
            card.last4Digits    = parts[2];
            card.maskedNumber   = parts[3];
            card.expiryMonth    = Integer.parseInt(parts[4]);
            card.expiryYear     = Integer.parseInt(parts[5]);
            card.cardType       = CardType.valueOf(parts[6]);
            card.country        = parts[7];
            card.createdAt      = LocalDateTime.parse(parts[8]);
            card.updatedAt      = LocalDateTime.parse(parts[9]);
            return card;
        } catch (Exception e) {
            System.err.println("[PaymentCard] Failed to parse line: " + line + " – " + e.getMessage());
            return null;
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String   getCardholderName()                            { return cardholderName; }
    public void     setCardholderName(String cardholderName)       { this.cardholderName = cardholderName; }

    public String   getLast4Digits()                               { return last4Digits; }
    public void     setLast4Digits(String last4Digits)             { this.last4Digits = last4Digits; }

    public String   getMaskedNumber()                              { return maskedNumber; }
    public void     setMaskedNumber(String maskedNumber)           { this.maskedNumber = maskedNumber; }

    public int      getExpiryMonth()                               { return expiryMonth; }
    public void     setExpiryMonth(int expiryMonth)                { this.expiryMonth = expiryMonth; }

    public int      getExpiryYear()                                { return expiryYear; }
    public void     setExpiryYear(int expiryYear)                  { this.expiryYear = expiryYear; }

    public CardType getCardType()                                  { return cardType; }
    public void     setCardType(CardType cardType)                 { this.cardType = cardType; }

    public String   getCountry()                                   { return country; }
    public void     setCountry(String country)                     { this.country = country; }

    // ── toString ──────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "PaymentCard{id='" + id + "', holder='" + cardholderName +
               "', number='" + maskedNumber + "', expiry=" + expiryMonth + "/" + expiryYear +
               ", type=" + cardType + ", country='" + country + "'}";
    }
}
