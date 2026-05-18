package com.cartracker.repository.feedback;

import com.cartracker.config.AppConfig;
import com.cartracker.model.feedback.Feedback;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * FileFeedbackRepository – flat-file implementation of FeedbackRepository.
 *
 * Demonstrates POLYMORPHISM: can be swapped for JdbcFeedbackRepository
 * without changing any caller (service, controller).
 *
 * Storage file: src/main/resources/data/feedbacks.txt
 * Line format : feedbackId|userId|serviceId|rating|comment|createdAt|updatedAt
 *
 * Thread-safety: single-threaded use only (typical for student projects).
 * For multi-threaded use, add synchronization on write methods.
 */
public class FileFeedbackRepository implements FeedbackRepository {

    // File path uses AppConfig.DATA_DIR so it stays consistent with the rest of the project
    private static final String FILE_PATH =
            AppConfig.DATA_DIR + AppConfig.FEEDBACKS_FILE;

    // ── Save ─────────────────────────────────────────────────────────────────

    @Override
    public Feedback save(Feedback feedback) {
        ensureFileExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(feedback.toFileString());
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("FileFeedbackRepository: could not save feedback – " + e.getMessage(), e);
        }
        return feedback;
    }

    // ── FindById ──────────────────────────────────────────────────────────────

    @Override
    public Optional<Feedback> findById(String feedbackId) {
        return findAll().stream()
                .filter(f -> f.getId().equals(feedbackId))
                .findFirst();
    }

    // ── FindAll ───────────────────────────────────────────────────────────────

    @Override
    public List<Feedback> findAll() {
        ensureFileExists();
        List<Feedback> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    list.add(parseLine(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("FileFeedbackRepository: could not read feedbacks file – " + e.getMessage(), e);
        }
        return Collections.unmodifiableList(list);
    }

    // ── FindByUserId ──────────────────────────────────────────────────────────

    @Override
    public List<Feedback> findByUserId(String userId) {
        return findAll().stream()
                .filter(f -> f.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    // ── FindByServiceId ───────────────────────────────────────────────────────

    @Override
    public List<Feedback> findByServiceId(String serviceId) {
        return findAll().stream()
                .filter(f -> f.getServiceId().equals(serviceId))
                .collect(Collectors.toList());
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Override
    public Feedback update(Feedback updated) {
        List<Feedback> all = new ArrayList<>(findAll()); // mutable copy
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

        if (!found) {
            throw new RuntimeException("FileFeedbackRepository: feedback not found for update – id=" + updated.getId());
        }

        writeAll(sb.toString());
        return updated;
    }

    // ── DeleteById ────────────────────────────────────────────────────────────

    @Override
    public boolean deleteById(String feedbackId) {
        List<Feedback> all = new ArrayList<>(findAll());
        List<Feedback> remaining = all.stream()
                .filter(f -> !f.getId().equals(feedbackId))
                .collect(Collectors.toList());

        if (remaining.size() == all.size()) {
            return false; // nothing was removed
        }

        StringBuilder sb = new StringBuilder();
        remaining.forEach(f -> sb.append(f.toFileString()).append(System.lineSeparator()));
        writeAll(sb.toString());
        return true;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Parses one pipe-delimited line back into a Feedback object.
     * Format: feedbackId|userId|serviceId|rating|comment|createdAt|updatedAt
     */
    private Feedback parseLine(String line) {
        String[] p = line.split("\\|", -1);
        // Guard against old format (6 columns without updatedAt)
        Feedback f = new Feedback(
                p[0],                          // feedbackId
                p[1],                          // userId
                p[2],                          // serviceId
                Integer.parseInt(p[3]),        // rating
                p[4]                           // comment
        );
        if (p.length > 5 && !p[5].isEmpty()) {
            f.setCreatedAt(LocalDateTime.parse(p[5]));
        }
        if (p.length > 6 && !p[6].isEmpty()) {
            f.setUpdatedAt(LocalDateTime.parse(p[6]));
        }
        return f;
    }

    /** Overwrites the entire file with the given content. */
    private void writeAll(String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("FileFeedbackRepository: could not write feedbacks file – " + e.getMessage(), e);
        }
    }

    /** Creates the file (and parent directories) if they do not yet exist. */
    private void ensureFileExists() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("FileFeedbackRepository: could not create feedbacks.txt – " + e.getMessage(), e);
            }
        }
    }
}
