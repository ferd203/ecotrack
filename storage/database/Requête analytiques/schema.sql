-- =========================================================
-- ECOTRACK – DATA WAREHOUSE
-- STAR SCHEMA + POSTGIS
-- Script rejouable (DROP + CREATE)
-- =========================================================

-- ------------------------------
-- Création du Schéma de la base de donées et EXTENSIONS
-- ------------------------------
CREATE SCHEMA DW;
CREATE EXTENSION IF NOT EXISTS postgis;

-- ------------------------------
-- DROP TABLES (ordre inverse des dépendances)
-- ------------------------------
DROP TABLE IF EXISTS FACT_MEASUREMENT CASCADE;

DROP TABLE IF EXISTS DIM_CONTAINER CASCADE;
DROP TABLE IF EXISTS DIM_ROUTE CASCADE;
DROP TABLE IF EXISTS DIM_DEVICE CASCADE;
DROP TABLE IF EXISTS DIM_AGENT CASCADE;
DROP TABLE IF EXISTS DIM_WASTE_TYPE CASCADE;
DROP TABLE IF EXISTS DIM_ZONE CASCADE;
DROP TABLE IF EXISTS DIM_TIME CASCADE;

-- =========================================================
-- DIMENSIONS
-- =========================================================

-- ------------------------------
-- DIM_TIME
-- ------------------------------
CREATE TABLE DW.DIM_TIME (
  time_sk INT PRIMARY KEY,
  date_bk DATE UNIQUE,
  day INT,
  month INT,
  month_name VARCHAR(20),
  quarter INT,
  year INT,
  day_of_week INT,
  is_weekend BOOLEAN
);

-- ------------------------------
-- DIM_ZONE
-- ------------------------------
CREATE TABLE DIM_ZONE (
    zone_sk SERIAL PRIMARY KEY,
    zone_bk VARCHAR(50) NOT NULL,
    zone_name VARCHAR(100),
    city VARCHAR(50),
    population INTEGER,
    area_km2 DOUBLE PRECISION,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    geom GEOMETRY(Point, 4326),
);

-- ------------------------------
-- DIM_WASTE_TYPE
-- ------------------------------
CREATE TABLE DW.DIM_WASTE_TYPE (
    waste_type_sk SERIAL PRIMARY KEY,
    waste_type_bk VARCHAR(20) NOT NULL,
    waste_type_name VARCHAR(100),
);

-- ------------------------------
-- DIM_AGENT
-- ------------------------------
CREATE TABLE DW.DIM_AGENT (
    agent_sk SERIAL PRIMARY KEY,
    agent_bk VARCHAR(50) UNIQUE,
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    role VARCHAR(50),
    is_active BOOLEAN,
    etl_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- optionnel
);
-- ------------------------------
-- DIM_DEVICE
-- ------------------------------
CREATE TABLE DW.DIM_DEVICE (
    device_id SERIAL PRIMARY KEY,
    device_bk VARCHAR(50) NOT NULL,
    model VARCHAR(50),
    installation_date DATE,
    is_active BOOLEAN,
);

-- ------------------------------
-- DIM_ROUTE
-- ------------------------------
DROP TABLE IF EXISTS DW.DIM_ROUTE CASCADE;

CREATE TABLE DW.DIM_ROUTE (
    route_sk SERIAL PRIMARY KEY,
    route_bk VARCHAR(50) NOT NULL UNIQUE,
    route_date DATE NOT NULL,
    planned_distance_m NUMERIC(12,2),
    planned_duration_min INTEGER,
    actual_distance_m NUMERIC(12,2),
    actual_duration_min INTEGER,
    geom GEOMETRY(LineString, 4326)
);

============================================================
-- TABLE : DW.DIM_CONTAINER
-- SCD TYPE 2 + SPATIAL
-- ============================================================

CREATE TABLE DW.DIM_CONTAINER (

    -- Clé technique (utilisée par la FACT)
    container_sk SERIAL PRIMARY KEY,

    -- Clé métier
    container_bk VARCHAR(50) NOT NULL,

    -- Attributs métier
    container_type VARCHAR(50),
    capacity_l INTEGER,
    zone_bk VARCHAR(50),
    status VARCHAR(20),

    -- Coordonnées
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,

    -- Géométrie PostGIS
    geom GEOMETRY(Point, 4326),

    -- SCD TYPE 2
    date_debut DATE NOT NULL,
    date_fin DATE,
    version INTEGER NOT NULL,
    is_current BOOLEAN NOT NULL DEFAULT TRUE,

    -- Audit
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- optionnel
);

-- =========================================================
-- FACT TABLE 
-- =========================================================

-- ------------------------------
-- FACT_MEASUREMENT sans Partitionnement
-- ------------------------------

-- 1. Supprimer la vue d'abord (elle tient un lock sur la table)
DROP VIEW IF EXISTS DW.VW_ML_DATASET CASCADE;

-- ========================================================
-- TRUNCATE PRÉALABLE (éviter les doublons)
-- ========================================================
TRUNCATE TABLE DW.FACT_MEASUREMENT RESTART IDENTITY;

-- ========================================================
-- DROP + RECREATE FACT_MEASUREMENT
-- ========================================================
DROP TABLE IF EXISTS DW.FACT_MEASUREMENT CASCADE;

CREATE TABLE DW.FACT_MEASUREMENT (
    measurement_sk          BIGSERIAL PRIMARY KEY,

    -- Dimensions (FK)
    time_sk                 INT          NOT NULL REFERENCES DW.DIM_TIME(time_sk),
    container_sk            INT          NOT NULL REFERENCES DW.DIM_CONTAINER(container_sk),
    zone_sk                 INT          NOT NULL REFERENCES DW.DIM_ZONE(zone_sk),
    waste_type_sk           INT          REFERENCES DW.DIM_WASTE_TYPE(waste_type_sk),
    agent_sk                INT          REFERENCES DW.DIM_AGENT(agent_sk),
    device_sk               INT          REFERENCES DW.DIM_DEVICE(device_id),
    route_sk                INT          REFERENCES DW.DIM_ROUTE(route_sk),

    -- Timestamp brut de la mesure IoT
    measurement_timestamp   TIMESTAMP,

    -- Métriques capteurs
    taux_remplissage_pct    NUMERIC(5,2),
    volume_litres           NUMERIC(9,2),
    temperature_c           NUMERIC(5,2),
    batterie_pct            SMALLINT,
    poids_estime_kg         NUMERIC(9,2),
    is_overflow             SMALLINT,
    nb_signalements_actifs  SMALLINT
);

-- ========================================================
-- INDEX PERFORMANCE
-- ========================================================

-- Filtres temporels
CREATE INDEX idx_fact_time
    ON DW.FACT_MEASUREMENT(time_sk);

-- Filtres conteneur
CREATE INDEX idx_fact_container
    ON DW.FACT_MEASUREMENT(container_sk);

-- Index composite (le plus utilisé en analytique)
CREATE INDEX idx_fact_container_time
    ON DW.FACT_MEASUREMENT(container_sk, time_sk);

-- Filtres zone
CREATE INDEX idx_fact_zone
    ON DW.FACT_MEASUREMENT(zone_sk);

-- Filtres dimensions optionnelles
CREATE INDEX idx_fact_waste_type
    ON DW.FACT_MEASUREMENT(waste_type_sk);

CREATE INDEX idx_fact_agent
    ON DW.FACT_MEASUREMENT(agent_sk);

CREATE INDEX idx_fact_device
    ON DW.FACT_MEASUREMENT(device_sk);

CREATE INDEX idx_fact_route
    ON DW.FACT_MEASUREMENT(route_sk);

-- Filtres métriques (utiles pour le ML et les alertes)
CREATE INDEX idx_fact_overflow
    ON DW.FACT_MEASUREMENT(is_overflow);

CREATE INDEX idx_fact_battery
    ON DW.FACT_MEASUREMENT(batterie_pct);

CREATE INDEX idx_fact_fillrate
    ON DW.FACT_MEASUREMENT(taux_remplissage_pct);