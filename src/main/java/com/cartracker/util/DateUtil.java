package com.cartracker.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DateUtil – helper methods for date/time parsing and formatting.
 *
 * Centralises all date patterns so the team uses consistent formats
 * when serialising to / deserialising from flat files.
 *
 * Formats used:
 *   - DATE_FORMAT     : "yyyy-MM-dd"           (for LocalDate)
 *   - DATETIME_FORMAT : "yyyy-MM-dd HH:mm:ss"  (for LocalDateTime)
 */
public class DateUtil {

    public static final String DATE_PATTERN     = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter DATE_FMT     = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    // ── Private constructor – utility class ───────────────────────────────────
    private DateUtil() {}

    // ── LocalDate helpers ─────────────────────────────────────────────────────

    public static String    formatDate(LocalDate date)          { return date.format(DATE_FMT); }
    public static LocalDate parseDate(String dateStr)           { return LocalDate.parse(dateStr, DATE_FMT); }

    // ── LocalDateTime helpers ─────────────────────────────────────────────────

    public static String        formatDateTime(LocalDateTime dt) { return dt.format(DATETIME_FMT); }
    public static LocalDateTime parseDateTime(String dtStr)      { return LocalDateTime.parse(dtStr, DATETIME_FMT); }

    // ── Convenience ───────────────────────────────────────────────────────────

    public static String    todayString()    { return formatDate(LocalDate.now()); }
    public static String    nowString()      { return formatDateTime(LocalDateTime.now()); }
}
