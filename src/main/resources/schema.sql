-- File: src/main/resources/schema.sql

-- -----------------------------------------
-- Table: events
-- Stores individual security events received via POST /api/events.
-- -----------------------------------------
CREATE TABLE IF NOT EXISTS events (
    id           BIGSERIAL PRIMARY KEY,
    type         VARCHAR(50)   NOT NULL,   -- e.g., 'LOGIN_FAILED', 'HTTP_ERROR'
    source       VARCHAR(100)  NULL,
    ip           VARCHAR(45)   NULL,       -- Using 45 to accommodate IPv6
    country      VARCHAR(5)    NULL,
    severity     VARCHAR(20)   NOT NULL,   -- e.g., 'LOW', 'MEDIUM', 'HIGH'
    timestamp    TIMESTAMPTZ   NOT NULL,   -- Timezone-aware timestamp (matches ZonedDateTime in Java)
    metadata     TEXT          NULL
);

-- Indexing for fast lookups in Incident detection logic (T2.3.2)
-- We frequently query events by type, timestamp, and potentially IP.
CREATE INDEX IF NOT EXISTS idx_events_type_time ON events (type, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_events_ip_time ON events (ip, timestamp DESC);


-- -----------------------------------------
-- Table: incidents
-- Stores aggregated incidents created by the IncidentService.
-- -----------------------------------------
CREATE TABLE IF NOT EXISTS incidents (
    id           BIGSERIAL PRIMARY KEY,
    type         VARCHAR(50)   NOT NULL,   -- e.g., 'BRUTE_FORCE_LOGIN', 'HTTP_ERROR_SPIKE'
    severity     VARCHAR(20)   NOT NULL,   -- e.g., 'LOW', 'MEDIUM', 'HIGH', 'CRITICAL'
    status       VARCHAR(20)   NOT NULL,   -- e.g., 'OPEN', 'RESOLVED'
    created_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    event_count  INT           DEFAULT 0,
    main_ips     TEXT          NULL,       -- Comma-separated list of main IPs
    countries    TEXT          NULL        -- Comma-separated list of affected countries
);

-- Indexing for fast lookups in Risk calculation (T2.4.3)
-- We frequently query incidents by status and updated_at.
CREATE INDEX IF NOT EXISTS idx_incidents_status_updated ON incidents (status, updated_at DESC);


-- -----------------------------------------
-- Table: beacon_state
-- Holds the single global state row for the beacon (ID=1).
-- -----------------------------------------
CREATE TABLE IF NOT EXISTS beacon_state (
    id            BIGINT       PRIMARY KEY, -- ID must be 1
    buzzer_muted  BOOLEAN      NOT NULL DEFAULT FALSE,
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- Seed row for beacon_state
-- Ensures the required row (ID=1) is present when the schema is created.
INSERT INTO beacon_state (id, buzzer_muted)
VALUES (1, FALSE)
ON CONFLICT (id) DO NOTHING; -- Prevents errors if the row already exists