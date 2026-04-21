package com.cartracker.model.billing;

import com.cartracker.model.common.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Invoice – financial document generated after a service is completed.
 *
 * An Invoice summarises all MaintenanceTasks and their costs for
 * a given ServiceRecord, and tracks the payment status.
 *
 * Fields:
 *   - serviceRecordId : the service this invoice is for
 *   - customerId      : who the invoice is billed to
 *   - invoiceDate     : date invoice was issued
 *   - lineItems       : human-readable list of items billed (from tasks)
 *   - subTotal        : total before tax
 *   - taxRate         : e.g. 0.10 for 10%
 *   - totalAmount     : subTotal + tax
 *   - paid            : whether the invoice has been fully paid
 *   - paymentId       : linked Payment record (set after payment)
 *
 * Team member: assign to the team member responsible for Billing and Reports module.
 */
public class Invoice extends BaseEntity {

    private String       serviceRecordId;
    private String       customerId;
    private LocalDate    invoiceDate;
    private List<String> lineItems;
    private double       subTotal;
    private double       taxRate;
    private double       totalAmount;
    private boolean      paid;
    private String       paymentId;  // set once paid

    // ── Constructors ──────────────────────────────────────────────────────────

    public Invoice() {
        super();
        this.lineItems = new ArrayList<>();
        this.paid      = false;
    }

    public Invoice(String id, String serviceRecordId, String customerId,
                   LocalDate invoiceDate, double subTotal, double taxRate) {
        super(id);
        this.serviceRecordId = serviceRecordId;
        this.customerId      = customerId;
        this.invoiceDate     = invoiceDate;
        this.subTotal        = subTotal;
        this.taxRate         = taxRate;
        this.totalAmount     = subTotal + (subTotal * taxRate);
        this.lineItems       = new ArrayList<>();
        this.paid            = false;
    }

    // ── Computed helpers ──────────────────────────────────────────────────────

    public double getTaxAmount() {
        return subTotal * taxRate;
    }

    // ── toFileString ──────────────────────────────────────────────────────────

    @Override
    public String toFileString() {
        // Format: id|serviceRecordId|customerId|invoiceDate|subTotal|taxRate|totalAmount|paid|paymentId|createdAt
        return String.join("|", id, serviceRecordId, customerId,
                invoiceDate.toString(), String.valueOf(subTotal),
                String.valueOf(taxRate), String.valueOf(totalAmount),
                String.valueOf(paid),
                (paymentId != null ? paymentId : ""),
                createdAt.toString());
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String       getServiceRecordId()                          { return serviceRecordId; }
    public void         setServiceRecordId(String serviceRecordId)    { this.serviceRecordId = serviceRecordId; }

    public String       getCustomerId()                               { return customerId; }
    public void         setCustomerId(String customerId)              { this.customerId = customerId; }

    public LocalDate    getInvoiceDate()                              { return invoiceDate; }
    public void         setInvoiceDate(LocalDate invoiceDate)         { this.invoiceDate = invoiceDate; }

    public List<String> getLineItems()                                { return lineItems; }
    public void         setLineItems(List<String> lineItems)          { this.lineItems = lineItems; }
    public void         addLineItem(String item)                      { lineItems.add(item); }

    public double       getSubTotal()                                 { return subTotal; }
    public void         setSubTotal(double subTotal)                  { this.subTotal = subTotal; }

    public double       getTaxRate()                                  { return taxRate; }
    public void         setTaxRate(double taxRate)                    { this.taxRate = taxRate; }

    public double       getTotalAmount()                              { return totalAmount; }
    public void         setTotalAmount(double totalAmount)            { this.totalAmount = totalAmount; }

    public boolean      isPaid()                                      { return paid; }
    public void         setPaid(boolean paid)                         { this.paid = paid; }

    public String       getPaymentId()                                { return paymentId; }
    public void         setPaymentId(String paymentId)                { this.paymentId = paymentId; }

    @Override
    public String toString() {
        return "Invoice{id='" + id + "', total=" + totalAmount + ", paid=" + paid + '}';
    }
}
