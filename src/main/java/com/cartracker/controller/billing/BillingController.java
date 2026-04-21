package com.cartracker.controller.billing;

import com.cartracker.model.billing.Invoice;
import com.cartracker.model.billing.Payment;
import com.cartracker.service.billing.BillingService;

import java.util.List;
import java.util.Scanner;

/**
 * BillingController – handles user-facing interactions for Billing and Reports.
 *
 * Team member: assign to the Billing and Reports module owner.
 */
public class BillingController {

    private final BillingService billingService;
    private final Scanner scanner = new Scanner(System.in);

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    public void showMenu() {
        System.out.println("\n=== Billing & Reports ===");
        System.out.println("1. Generate Invoice");
        System.out.println("2. View Invoice");
        System.out.println("3. Record Payment");
        System.out.println("4. View Revenue Report");
        System.out.println("5. List Unpaid Invoices");
        System.out.println("0. Back");
    }

    /** Admin triggers invoice generation after a service record is completed. */
    public void generateInvoice() {
        System.out.print("Enter Service Record ID: ");
        String serviceRecordId = scanner.nextLine().trim();
        Invoice invoice = billingService.generateInvoice(serviceRecordId);
        System.out.println("Invoice generated: " + invoice);
    }

    /** Customer or admin views an invoice by ID. */
    public void viewInvoice() {
        System.out.print("Enter Invoice ID: ");
        String invoiceId = scanner.nextLine().trim();
        billingService.findInvoiceById(invoiceId)
                .ifPresentOrElse(
                        inv -> System.out.println(inv),
                        ()  -> System.out.println("Invoice not found.")
                );
    }

    /** Record a payment against an invoice. */
    public void recordPayment() {
        System.out.print("Enter Invoice ID to pay: ");
        String invoiceId = scanner.nextLine().trim();
        Payment payment = new Payment();
        payment.setInvoiceId(invoiceId); // TODO: populate remaining payment fields from user input
        Payment saved = billingService.recordPayment(payment);
        System.out.println("Payment recorded: " + saved);
    }

    /** Admin selects a date range and prints a revenue summary report. */
    public void viewRevenueReport() {
        System.out.print("From date (YYYY-MM-DD): ");
        String from = scanner.nextLine().trim();
        System.out.print("To date   (YYYY-MM-DD): ");
        String to = scanner.nextLine().trim();
        String report = billingService.generateRevenueReport(from, to);
        System.out.println(report);
    }

    /** Admin sees all outstanding (unpaid) invoices. */
    public void listUnpaidInvoices() {
        List<Invoice> unpaid = billingService.findUnpaidInvoices();
        if (unpaid.isEmpty()) {
            System.out.println("No unpaid invoices.");
        } else {
            unpaid.forEach(System.out::println);
        }
    }
}
