-- ============================================================
--  Run this in MySQL Workbench (or any MySQL client).
--  Drops and recreates the feedback table without FK constraints
--  and without the UNIQUE constraint, so any userId / serviceId
--  can be entered and the same user can leave multiple reviews.
-- ============================================================

USE car_tracker_db;

DROP TABLE IF EXISTS feedback;

CREATE TABLE feedback (
    id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    user_id     VARCHAR(36)  NOT NULL,
    service_id  VARCHAR(36)  NOT NULL,
    rating      TINYINT      NOT NULL,
    comment     TEXT,
    created_at  DATETIME     NOT NULL,
    updated_at  DATETIME     NOT NULL,
    CONSTRAINT chk_rating CHECK (rating BETWEEN 1 AND 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
