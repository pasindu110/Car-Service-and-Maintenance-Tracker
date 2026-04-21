package com.cartracker.repository.billing;

import com.cartracker.model.billing.Invoice;
import com.cartracker.model.billing.Payment;
import com.cartracker.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * FileBillingRepository – flat-file implementation.
 * Invoices → invoices.txt | Payments → a section inside invoices.txt or separate file.
 *
 * Team member: assign to the Billing and Reports module owner.
 */
public class FileBillingRepository implements BillingRepository {

    private static final String          INV_FILE  = "invoices.txt";
    private final        List<Invoice>   invCache  = new ArrayList<>();
    private final        List<Payment>   payCache  = new ArrayList<>();

    public FileBillingRepository() { /* TODO: loadFromFile() */ }

    private void persistInvoices() {
        List<String> lines = new ArrayList<>();
        invCache.forEach(i -> lines.add(i.toFileString()));
        FileUtil.writeAllLines(INV_FILE, lines);
    }

    @Override public Invoice           saveInvoice(Invoice i)      { invCache.add(i); FileUtil.appendLine(INV_FILE, i.toFileString()); return i; }
    @Override public Optional<Invoice> findInvoiceById(String id)  { return invCache.stream().filter(i -> i.getId().equals(id)).findFirst(); }
    @Override public List<Invoice>     findAllInvoices()           { return List.copyOf(invCache); }
    @Override public Invoice           updateInvoice(Invoice i)    { deleteInvoiceById(i.getId()); invCache.add(i); persistInvoices(); return i; }
    @Override public boolean           deleteInvoiceById(String id){ boolean r = invCache.removeIf(i -> i.getId().equals(id)); if(r) persistInvoices(); return r; }

    @Override public Payment           savePayment(Payment p)      { payCache.add(p); return p; }
    @Override public Optional<Payment> findPaymentById(String id)  { return payCache.stream().filter(p -> p.getId().equals(id)).findFirst(); }
    @Override public List<Payment>     findAllPayments()           { return List.copyOf(payCache); }
}
