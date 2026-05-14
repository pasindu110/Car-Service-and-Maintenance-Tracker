package com.cartracker.model.paymentcard;

/**
 * CardType – enumeration of supported payment card brands.
 *
 * Uses static factory method detect() to identify the brand from a raw card number.
 */
public enum CardType {
    VISA,
    MASTERCARD,
    AMEX,
    DISCOVER,
    UNKNOWN;

    /**
     * Detects the card brand from a raw card number string.
     *
     * @param rawNumber digits-only or formatted card number
     * @return matching CardType or UNKNOWN
     */
    public static CardType detect(String rawNumber) {
        if (rawNumber == null) return UNKNOWN;
        String digits = rawNumber.replaceAll("\\D", "");
        if (digits.startsWith("4"))               return VISA;
        if (digits.matches("^5[1-5].*"))          return MASTERCARD;
        if (digits.matches("^3[47].*"))           return AMEX;
        if (digits.matches("^6(?:011|5).*"))      return DISCOVER;
        return UNKNOWN;
    }
}
