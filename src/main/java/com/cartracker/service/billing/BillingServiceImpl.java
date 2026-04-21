package com.cartracker.service.billing;

import com.cartracker.model.billing.Invoice;
import com.cartracker.model.billing.Payment;
import com.cartracker.repository.billing.BillingRepository;

import java.util.List;
import java.util.Optional;

/**
 * BillingServiceImpl – placeholder implementation.
 *
 * Team member: assign to the Billing and Reports module owner.
 */
public class BillingServiceImpl implements BillingService {

    private final BillingRepository billingRepository;

    public BillingServiceImpl(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    @Override
    public Invoice generateInvoice(String serviceRecordId) {
        // TODO: Load ServiceRecord, sum all MaintenanceTasks costs
        // TODO: Apply tax rate from AppConfig, generate Invoice, save
        return null;
    }

    @Override
    public Optional<Invoice> findInvoiceById(String invoiceId) {
        return Optional.empty(); // TODO
    }

    @Override
    public List<Invoice> findInvoicesByCustomer(String customerId) {
        return List.of(); // TODO
    }

    @Override
    public List<Invoice> findUnpaidInvoices() {
        return List.of(); // TODO
    }

    @Override
    public Payment recordPayment(Payment payment) {
        // TODO: Validate invoice exists, mark invoice.paid = true, save Payment
        return billingRepository.savePayment(payment);
    }

    @Override
    public String generateRevenueReport(String fromDate, String toDate) {
        // TODO: Filter payments by date range, calculate totals, return formatted string
        return "[Revenue Report: " + fromDate + " – " + toDate + "] TODO: implement";
    }
}
