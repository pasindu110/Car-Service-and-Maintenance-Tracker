package com.cartracker.model.billing;

import com.cartracker.model.common.BaseEntity;

import java.time.LocalDateTime;

/**
 * Payment – records a payment transaction against an Invoice.
 *
 * Fields:
 *   - invoiceId       : which invoice this covers
 *   - customerId      : who made the payment
 *   - amountPaid      : amount paid in this transaction
 *   - paymentMethod   : "CASH" | "CARD" | "ONLINE_TRANSFER"
 *   - paidAt          : date-time the payment was processed
 *   - referenceNumber : optional external reference (card txn, transfer ID)
 *
 * Team member: assign to the team member responsible for Billing and Reports module.
 */
public class Payment extends BaseEntity {

    private String        invoiceId;
    private String        customerId;
    private double        amountPaid;
    private String        paymentMethod;    // CASH | CARD | ONLINE_TRANSFER
    private LocalDateTime paidAt;
    private String        referenceNumber;  // optional

    // ── Constructors ──────────────────────────────────────────────────────────

    public Payment() {
        super();
    }

    public Payment(String id, String invoiceId, String customerId,
                   double amountPaid, String paymentMethod) {
        super(id);
        this.invoiceId       = invoiceId;
        this.customerId      = customerId;
        this.amountPaid      = amountPaid;
        this.paymentMethod   = paymentMethod;
        this.paidAt          = LocalDateTime.now();
    }

    // ── toFileString ──────────────────────────────────────────────────────────

    @Override
    public String toFileString() {
        // Format: id|invoiceId|customerId|amountPaid|paymentMethod|paidAt|referenceNumber|createdAt
        return String.join("|", id, invoiceId, customerId,
                String.valueOf(amountPaid), paymentMethod, paidAt.toString(),
                (referenceNumber != null ? referenceNumber : ""),
                createdAt.toString());
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String        getInvoiceId()                         { return invoiceId; }
    public void          setInvoiceId(String invoiceId)         { this.invoiceId = invoiceId; }

    public String        getCustomerId()                        { return customerId; }
    public void          setCustomerId(String customerId)       { this.customerId = customerId; }

    public double        getAmountPaid()                        { return amountPaid; }
    public void          setAmountPaid(double amountPaid)       { this.amountPaid = amountPaid; }

    public String        getPaymentMethod()                     { return paymentMethod; }
    public void          setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getPaidAt()                            { return paidAt; }
    public void          setPaidAt(LocalDateTime paidAt)        { this.paidAt = paidAt; }

    public String        getReferenceNumber()                          { return referenceNumber; }
    public void          setReferenceNumber(String referenceNumber)    { this.referenceNumber = referenceNumber; }

    @Override
    public String toString() {
        return "Payment{id='" + id + "', invoiceId='" + invoiceId +
               "', amount=" + amountPaid + ", method='" + paymentMethod + "'}";
    }
}
