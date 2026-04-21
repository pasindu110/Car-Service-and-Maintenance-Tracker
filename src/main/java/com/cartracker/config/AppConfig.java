package com.cartracker.config;

/**
 * AppConfig – application-wide configuration constants.
 *
 * Keep magic numbers and configurable values here instead of scattering
 * them across classes. Later, load these from application.properties.
 *
 * Team usage: import AppConfig and reference these constants anywhere.
 */
public final class AppConfig {

    // ── File paths ─────────────────────────────────────────────────────────────
    public static final String DATA_DIR     = "src/main/resources/data/";
    public static final String USERS_FILE   = "users.txt";
    public static final String VEHICLES_FILE= "vehicles.txt";
    public static final String SERVICES_FILE= "services.txt";
    public static final String APPTS_FILE   = "appointments.txt";
    public static final String INVOICES_FILE= "invoices.txt";

    // ── Business rules ─────────────────────────────────────────────────────────
    public static final double DEFAULT_TAX_RATE  = 0.10;   // 10% VAT/GST
    public static final int    MAX_VEHICLES_PER_CUSTOMER = 10;

    // ── ID prefixes ───────────────────────────────────────────────────────────
    public static final String USER_PREFIX     = "USR";
    public static final String VEHICLE_PREFIX  = "VEH";
    public static final String SERVICE_PREFIX  = "SVC";
    public static final String APPT_PREFIX     = "APT";
    public static final String INVOICE_PREFIX  = "INV";
    public static final String PAYMENT_PREFIX  = "PAY";
    public static final String TASK_PREFIX     = "TSK";

    // ── Private constructor – utility class, do not instantiate ───────────────
    private AppConfig() {}
}
