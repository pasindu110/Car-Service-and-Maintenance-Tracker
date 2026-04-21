package com.cartracker.util;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileUtil – centralised utility for all flat-file (text) I/O operations.
 *
 * All data files live under: src/main/resources/data/
 * Each entity is stored as one pipe-delimited (|) line per record.
 *
 * TEAM USAGE:
 *   - Use readAllLines()  to load all records from a file at startup.
 *   - Use writeAllLines() to persist the full in-memory list to disk.
 *   - Use appendLine()    to add a single new record without rewriting the file.
 *
 * Future: Replace these methods with JDBC calls when switching to a database.
 */
public class FileUtil {

    /** Base path to the data directory (relative to project root). */
    private static final String DATA_DIR = "src/main/resources/data/";

    // ── Private constructor – utility class, do not instantiate ───────────────
    private FileUtil() {}

    // ── Read ──────────────────────────────────────────────────────────────────

    /**
     * Reads all non-empty, non-comment lines from a data file.
     *
     * @param fileName e.g. "users.txt"
     * @return list of raw lines
     */
    public static List<String> readAllLines(String fileName) {
        List<String> lines = new ArrayList<>();
        Path path = Paths.get(DATA_DIR + fileName);

        if (!Files.exists(path)) {
            System.out.println("[FileUtil] File not found: " + path);
            return lines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("[FileUtil] Error reading " + fileName + ": " + e.getMessage());
        }

        return lines;
    }

    // ── Write (overwrite) ─────────────────────────────────────────────────────

    /**
     * Overwrites a data file with the provided list of lines.
     *
     * @param fileName e.g. "users.txt"
     * @param lines    list of serialised entity strings
     */
    public static void writeAllLines(String fileName, List<String> lines) {
        Path path = Paths.get(DATA_DIR + fileName);
        ensureDirectoryExists(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("[FileUtil] Error writing " + fileName + ": " + e.getMessage());
        }
    }

    // ── Append ────────────────────────────────────────────────────────────────

    /**
     * Appends a single serialised entity line to the end of a data file.
     *
     * @param fileName e.g. "users.txt"
     * @param line     serialised entity
     */
    public static void appendLine(String fileName, String line) {
        Path path = Paths.get(DATA_DIR + fileName);
        ensureDirectoryExists(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("[FileUtil] Error appending to " + fileName + ": " + e.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static void ensureDirectoryExists(Path filePath) {
        Path parent = filePath.getParent();
        if (parent != null && !Files.exists(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                System.err.println("[FileUtil] Cannot create directory: " + parent);
            }
        }
    }
}
