package com.cartracker.repository.feedback;

import com.cartracker.model.feedback.Feedback;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * FileFeedbackRepository – flat-file implementation of FeedbackRepository.
 *
 * Demonstrates POLYMORPHISM: implements FeedbackRepository so it can be
 * swapped for a DatabaseFeedbackRepository without changing callers.
 *
 * Storage: one record per line in feedbacks.txt
 * CSV format: id|customerId|serviceRecordId|rating|comment|createdAt
 *
 * Team member: assign to the Feedback module owner.
 */
public class FileFeedbackRepository implements FeedbackRepository {

    /** Path to the flat-file storage (configure via AppConfig if needed). */
    private static final String FILE_PATH = "src/main/resources/data/feedbacks.txt";

    // ── Save ─────────────────────────────────────────────────────────────────

    @Override
    public Feedback save(Feedback feedback) {
        // TODO: Call FileUtil.appendLine(FILE_PATH, feedback.toFileString())
        // Placeholder: write directly via BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(feedback.toFileString());
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Could not save feedback: " + e.getMessage(), e);
        }
        return feedback;
    }

    // ── FindById ──────────────────────────────────────────────────────────────

    @Override
    public Optional<Feedback> findById(String id) {
        return findAll().stream()
                .filter(f -> f.getId().equals(id))
                .findFirst();
    }

    // ── FindAll ───────────────────────────────────────────────────────────────

    @Override
    public List<Feedback> findAll() {
        List<Feedback> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    list.add(parseLine(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read feedbacks file: " + e.getMessage(), e);
        }
        return list;
    }

    // ── FindByCustomerId ──────────────────────────────────────────────────────

    @Override
    public List<Feedback> findByCustomerId(String customerId) {
        return findAll().stream()
                .filter(f -> f.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    // ── FindByServiceRecordId ─────────────────────────────────────────────────

    @Override
    public List<Feedback> findByServiceRecordId(String serviceRecordId) {
        return findAll().stream()
                .filter(f -> f.getServiceRecordId().equals(serviceRecordId))
                .collect(Collectors.toList());
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Override
    public Feedback update(Feedback updated) {
        List<Feedback> all = findAll();
        boolean found = false;
        StringBuilder sb = new StringBuilder();

        for (Feedback f : all) {
            if (f.getId().equals(updated.getId())) {
                sb.append(updated.toFileString()).append(System.lineSeparator());
                found = true;
            } else {
                sb.append(f.toFileString()).append(System.lineSeparator());
            }
        }

        if (!found) throw new RuntimeException("Feedback not found: " + updated.getId());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException("Could not update feedback: " + e.getMessage(), e);
        }
        return updated;
    }

    // ── DeleteById ────────────────────────────────────────────────────────────

    @Override
    public boolean deleteById(String id) {
        List<Feedback> all = findAll();
        List<Feedback> remaining = all.stream()
                .filter(f -> !f.getId().equals(id))
                .collect(Collectors.toList());

        if (remaining.size() == all.size()) return false; // not found

        StringBuilder sb = new StringBuilder();
        remaining.forEach(f -> sb.append(f.toFileString()).append(System.lineSeparator()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException("Could not delete feedback: " + e.getMessage(), e);
        }
        return true;
    }

    // ── Private helper ────────────────────────────────────────────────────────

    /**
     * Parses a single CSV line back into a Feedback object.
     * Format: id|customerId|serviceRecordId|rating|comment|createdAt
     */
    private Feedback parseLine(String line) {
        String[] parts = line.split("\\|", -1);
        // parts[0]=id, [1]=customerId, [2]=serviceRecordId, [3]=rating, [4]=comment, [5]=createdAt
        Feedback f = new Feedback(
                parts[0],
                parts[1],
                parts[2],
                Integer.parseInt(parts[3]),
                parts[4]
        );
        f.setCreatedAt(LocalDateTime.parse(parts[5]));
        return f;
    }
}
