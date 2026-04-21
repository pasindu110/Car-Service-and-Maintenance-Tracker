package com.cartracker.service.billing;

import com.cartracker.model.billing.Invoice;
import com.cartracker.model.billing.Payment;

import java.util.List;
import java.util.Optional;

/**
 * BillingService – service interface for the Billing and Reports module.
 */
public interface BillingService {

    Invoice          generateInvoice(String serviceRecordId);
    Optional<Invoice> findInvoiceById(String invoiceId);
    List<Invoice>    findInvoicesByCustomer(String customerId);
    List<Invoice>    findUnpaidInvoices();
    Payment          recordPayment(Payment payment);
    String           generateRevenueReport(String fromDate, String toDate);
}
