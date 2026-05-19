package com.cartracker.repository.paymentcard;

import com.cartracker.model.paymentcard.PaymentCard;
import com.cartracker.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * FilePaymentCardRepository – flat-file implementation of PaymentCardRepository.
 *
 * Data is persisted in: src/main/resources/data/payment_cards.txt
 * Each line is one pipe-delimited PaymentCard record (via toFileString()).
 *
 * An in-memory cache is populated at startup and kept in sync on every write,
 * so reads are always O(1) / O(n) without hitting the disk.
 */
public class FilePaymentCardRepository implements PaymentCardRepository {

    private static final String FILE_NAME = "payment_cards.txt";

    private final List<PaymentCard> cache = new ArrayList<>();

    public FilePaymentCardRepository() {
        loadFromFile();
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private void loadFromFile() {
        List<String> lines = FileUtil.readAllLines(FILE_NAME);
        for (String line : lines) {
            PaymentCard card = PaymentCard.fromFileString(line);
            if (card != null) {
                cache.add(card);
            }
        }
    }

    private void persistToFile() {
        List<String> lines = new ArrayList<>();
        for (PaymentCard card : cache) {
            lines.add(card.toFileString());
        }
        FileUtil.writeAllLines(FILE_NAME, lines);
    }

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @Override
    public PaymentCard save(PaymentCard card) {
        cache.add(card);
        FileUtil.appendLine(FILE_NAME, card.toFileString());
        return card;
    }

    @Override
    public Optional<PaymentCard> findById(String id) {
        return cache.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<PaymentCard> findAll() {
        return List.copyOf(cache);
    }

    @Override
    public PaymentCard update(PaymentCard updated) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId().equals(updated.getId())) {
                cache.set(i, updated);
                persistToFile();
                return updated;
            }
        }
        return null;
    }

    @Override
    public boolean deleteById(String id) {
        boolean removed = cache.removeIf(c -> c.getId().equals(id));
        if (removed) {
            persistToFile();
        }
        return removed;
    }
}
