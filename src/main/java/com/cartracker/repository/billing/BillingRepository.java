package com.cartracker.repository.billing;

import com.cartracker.model.billing.Invoice;
import com.cartracker.model.billing.Payment;

import java.util.List;
import java.util.Optional;

/**
 * BillingRepository – data access interface for Invoice and Payment entities.
 *
 * Team member: assign to the Billing and Reports module owner.
 */
public interface BillingRepository {

    Invoice            saveInvoice(Invoice invoice);
    Optional<Invoice>  findInvoiceById(String id);
    List<Invoice>      findAllInvoices();
    Invoice            updateInvoice(Invoice invoice);
    boolean            deleteInvoiceById(String id);

    Payment            savePayment(Payment payment);
    Optional<Payment>  findPaymentById(String id);
    List<Payment>      findAllPayments();
}
