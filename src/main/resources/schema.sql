-- ============================================================
--  Car Service and Maintenance Tracker – Database Schema
--  Run this script once to initialise the MySQL database.
-- ============================================================

CREATE DATABASE IF NOT EXISTS car_tracker_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE car_tracker_db;

-- ── Users (base table for Admin / Customer / Mechanic) ────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id           VARCHAR(36)  NOT NULL PRIMARY KEY,
    username     VARCHAR(50)  NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    full_name    VARCHAR(100) NOT NULL,
    phone        VARCHAR(20),
    role         ENUM('ADMIN','CUSTOMER','MECHANIC') NOT NULL,
    active       BOOLEAN      NOT NULL DEFAULT TRUE,
    admin_level  VARCHAR(30),
    address      VARCHAR(255),
    specialisation   VARCHAR(100),
    experience_years INT,
    available        BOOLEAN,
    created_at   DATETIME     NOT NULL,
    updated_at   DATETIME     NOT NULL
);

-- ── Vehicles ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS vehicles (
    id            VARCHAR(36)  NOT NULL PRIMARY KEY,
    owner_id      VARCHAR(36)  NOT NULL,
    license_plate VARCHAR(20)  NOT NULL UNIQUE,
    make          VARCHAR(50)  NOT NULL,
    model         VARCHAR(50)  NOT NULL,
    year          INT          NOT NULL,
    color         VARCHAR(30),
    mileage       DOUBLE       NOT NULL DEFAULT 0,
    fuel_type     VARCHAR(20),
    created_at    DATETIME     NOT NULL,
    updated_at    DATETIME     NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users(id)
);

-- ── Appointments ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS appointments (
    id                VARCHAR(36)  NOT NULL PRIMARY KEY,
    customer_id       VARCHAR(36)  NOT NULL,
    vehicle_id        VARCHAR(36)  NOT NULL,
    mechanic_id       VARCHAR(36),
    scheduled_at      DATETIME     NOT NULL,
    service_type      VARCHAR(100) NOT NULL,
    notes             TEXT,
    status            ENUM('PENDING','CONFIRMED','IN_PROGRESS','COMPLETED','CANCELLED') NOT NULL DEFAULT 'PENDING',
    service_record_id VARCHAR(36),
    created_at        DATETIME     NOT NULL,
    updated_at        DATETIME     NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (vehicle_id)  REFERENCES vehicles(id)
);

-- ── Service Records ───────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS service_records (
    id           VARCHAR(36)  NOT NULL PRIMARY KEY,
    vehicle_id   VARCHAR(36)  NOT NULL,
    mechanic_id  VARCHAR(36),
    service_date DATE         NOT NULL,
    description  TEXT,
    status       ENUM('PENDING','CONFIRMED','IN_PROGRESS','COMPLETED','CANCELLED') NOT NULL DEFAULT 'PENDING',
    total_cost   DOUBLE       NOT NULL DEFAULT 0,
    created_at   DATETIME     NOT NULL,
    updated_at   DATETIME     NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);

-- ── Maintenance Tasks ─────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS maintenance_tasks (
    id                VARCHAR(36)  NOT NULL PRIMARY KEY,
    service_record_id VARCHAR(36)  NOT NULL,
    task_name         VARCHAR(100) NOT NULL,
    task_details      TEXT,
    parts_cost        DOUBLE       NOT NULL DEFAULT 0,
    labour_cost       DOUBLE       NOT NULL DEFAULT 0,
    completed         BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at        DATETIME     NOT NULL,
    updated_at        DATETIME     NOT NULL,
    FOREIGN KEY (service_record_id) REFERENCES service_records(id)
);

-- ── Invoices ──────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS invoices (
    id                VARCHAR(36)  NOT NULL PRIMARY KEY,
    service_record_id VARCHAR(36)  NOT NULL,
    customer_id       VARCHAR(36)  NOT NULL,
    invoice_date      DATE         NOT NULL,
    sub_total         DOUBLE       NOT NULL DEFAULT 0,
    tax_rate          DOUBLE       NOT NULL DEFAULT 0.10,
    total_amount      DOUBLE       NOT NULL DEFAULT 0,
    paid              BOOLEAN      NOT NULL DEFAULT FALSE,
    payment_id        VARCHAR(36),
    created_at        DATETIME     NOT NULL,
    updated_at        DATETIME     NOT NULL,
    FOREIGN KEY (service_record_id) REFERENCES service_records(id),
    FOREIGN KEY (customer_id)       REFERENCES users(id)
);

-- ── Payments ──────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS payments (
    id               VARCHAR(36)  NOT NULL PRIMARY KEY,
    invoice_id       VARCHAR(36)  NOT NULL,
    customer_id      VARCHAR(36)  NOT NULL,
    amount_paid      DOUBLE       NOT NULL,
    payment_method   ENUM('CASH','CARD','ONLINE_TRANSFER') NOT NULL,
    paid_at          DATETIME     NOT NULL,
    reference_number VARCHAR(100),
    created_at       DATETIME     NOT NULL,
    updated_at       DATETIME     NOT NULL,
    FOREIGN KEY (invoice_id)   REFERENCES invoices(id),
    FOREIGN KEY (customer_id)  REFERENCES users(id)
);

-- ── Payment Cards ─────────────────────────────────────────────────────────────
-- Stores masked card details only. Full card number and CVV are NEVER stored.
CREATE TABLE IF NOT EXISTS payment_cards (
    id               VARCHAR(36)  NOT NULL PRIMARY KEY,
    cardholder_name  VARCHAR(100) NOT NULL,
    last4_digits     CHAR(4)      NOT NULL,
    masked_number    VARCHAR(25)  NOT NULL,
    expiry_month     TINYINT      NOT NULL COMMENT '1–12',
    expiry_year      TINYINT      NOT NULL COMMENT '2-digit year e.g. 27',
    card_type        ENUM('VISA','MASTERCARD','AMEX','DISCOVER','UNKNOWN') NOT NULL DEFAULT 'UNKNOWN',
    country          VARCHAR(10)  NOT NULL,
    created_at       DATETIME     NOT NULL,
    updated_at       DATETIME     NOT NULL
);

-- ── Feedback ──────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS feedback (
    id                VARCHAR(36) NOT NULL PRIMARY KEY,
    customer_id       VARCHAR(36) NOT NULL,
    service_record_id VARCHAR(36) NOT NULL,
    rating            TINYINT     NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment           TEXT,
    created_at        DATETIME    NOT NULL,
    updated_at        DATETIME    NOT NULL,
    UNIQUE KEY uq_feedback (customer_id, service_record_id),
    FOREIGN KEY (customer_id)       REFERENCES users(id),
    FOREIGN KEY (service_record_id) REFERENCES service_records(id)
);
