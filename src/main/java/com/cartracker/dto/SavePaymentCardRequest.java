package com.cartracker.dto;

/**
 * SavePaymentCardRequest – DTO that carries raw form data from the frontend
 * to the PaymentCardService for validation and persistence.
 *
 * Note: cvv is accepted but is NEVER stored. It is used only for
 * format validation during card creation/update.
 */
public class SavePaymentCardRequest {

    private String cardholderName;
    private String cardNumber;   // Raw digits (may be empty on update = keep existing)
    private String expiry;       // Format: "MM / YY"
    private String cvv;          // 3–4 digits – validated but never persisted
    private String country;      // ISO country code, e.g. "LK", "US"

    // ── Constructors ──────────────────────────────────────────────────────────

    public SavePaymentCardRequest() {}

    public SavePaymentCardRequest(String cardholderName, String cardNumber,
                                  String expiry, String cvv, String country) {
        this.cardholderName = cardholderName;
        this.cardNumber     = cardNumber;
        this.expiry         = expiry;
        this.cvv            = cvv;
        this.country        = country;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getCardholderName()                          { return cardholderName; }
    public void   setCardholderName(String cardholderName)     { this.cardholderName = cardholderName; }

    public String getCardNumber()                              { return cardNumber; }
    public void   setCardNumber(String cardNumber)             { this.cardNumber = cardNumber; }

    public String getExpiry()                                  { return expiry; }
    public void   setExpiry(String expiry)                     { this.expiry = expiry; }

    public String getCvv()                                     { return cvv; }
    public void   setCvv(String cvv)                           { this.cvv = cvv; }

    public String getCountry()                                 { return country; }
    public void   setCountry(String country)                   { this.country = country; }

    @Override
    public String toString() {
        return "SavePaymentCardRequest{holder='" + cardholderName +
               "', number='****', expiry='" + expiry + "', country='" + country + "'}";
    }
}
