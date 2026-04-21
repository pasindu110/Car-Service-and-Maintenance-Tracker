package com.cartracker.dto;

/**
 * InvoiceResponse – DTO for presenting invoice details to the user.
 *
 * Keeps the presentation format decoupled from the Invoice domain model.
 * Team member: used by BillingController when displaying invoice information.
 */
public class InvoiceResponse {

    private String invoiceId;
    private String customerId;
    private String invoiceDate;
    private double subTotal;
    private double taxAmount;
    private double totalAmount;
    private boolean paid;
    private String paymentMethod;

    public InvoiceResponse() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String  getInvoiceId()                       { return invoiceId; }
    public void    setInvoiceId(String invoiceId)       { this.invoiceId = invoiceId; }

    public String  getCustomerId()                      { return customerId; }
    public void    setCustomerId(String customerId)     { this.customerId = customerId; }

    public String  getInvoiceDate()                     { return invoiceDate; }
    public void    setInvoiceDate(String invoiceDate)   { this.invoiceDate = invoiceDate; }

    public double  getSubTotal()                        { return subTotal; }
    public void    setSubTotal(double subTotal)         { this.subTotal = subTotal; }

    public double  getTaxAmount()                       { return taxAmount; }
    public void    setTaxAmount(double taxAmount)       { this.taxAmount = taxAmount; }

    public double  getTotalAmount()                     { return totalAmount; }
    public void    setTotalAmount(double totalAmount)   { this.totalAmount = totalAmount; }

    public boolean isPaid()                             { return paid; }
    public void    setPaid(boolean paid)                { this.paid = paid; }

    public String  getPaymentMethod()                        { return paymentMethod; }
    public void    setPaymentMethod(String paymentMethod)    { this.paymentMethod = paymentMethod; }
}
