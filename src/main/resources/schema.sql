-- File: src/main/resources/schema.sql

-- 1. Events Table
CREATE TABLE IF NOT EXISTS events (
    id           BIGSERIAL PRIMARY KEY,
    type         VARCHAR(50)   NOT NULL,
    source       VARCHAR(100)  NULL,
    ip           VARCHAR(45)   NULL,
    country      VARCHAR(5)    NULL,
    severity     VARCHAR(20)   NOT NULL,
    -- CAMBIO: Usamos el estándar SQL en lugar de la abreviatura de Postgres
    timestamp    TIMESTAMP WITH TIME ZONE NOT NULL,
    metadata     TEXT          NULL
);

CREATE INDEX IF NOT EXISTS idx_events_type_time ON events (type, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_events_ip_time ON events (ip, timestamp DESC);

-- 2. Incidents Table
CREATE TABLE IF NOT EXISTS incidents (
    id           BIGSERIAL PRIMARY KEY,
    type         VARCHAR(50)   NOT NULL,
    severity     VARCHAR(20)   NOT NULL,
    status       VARCHAR(20)   NOT NULL,
    -- CAMBIO: Usamos el estándar SQL
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    event_count  INT           DEFAULT 0,
    main_ips     TEXT          NULL,
    countries    TEXT          NULL
);

CREATE INDEX IF NOT EXISTS idx_incidents_status_updated ON incidents (status, updated_at DESC);

-- 3. Beacon State Table
CREATE TABLE IF NOT EXISTS beacon_state (
    id            BIGINT       PRIMARY KEY,
    buzzer_muted  BOOLEAN      NOT NULL DEFAULT FALSE,
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- 4. Seed Data (Universal SQL)
-- Inserta la fila inicial solo si no existe. Funciona en H2 y Postgres.
INSERT INTO beacon_state (id, buzzer_muted, updated_at)
SELECT 1, FALSE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM beacon_state WHERE id = 1);