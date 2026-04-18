
DROP SCHEMA IF EXISTS dw CASCADE;
CREATE SCHEMA dw;

CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS fuzzystrmatch;

-- Dimension Temps
DROP TABLE IF EXISTS dw.dim_time CASCADE;
CREATE TABLE dw.dim_time (
    time_sk INTEGER PRIMARY KEY,
    date_bk DATE UNIQUE NOT NULL,
    day SMALLINT,
    month SMALLINT,
    month_name VARCHAR(20),
    quarter SMALLINT,
    year SMALLINT,
    day_of_week SMALLINT,
    is_weekend BOOLEAN NOT NULL
);

-- Dimension Zone
DROP TABLE IF EXISTS dw.dim_zone CASCADE;
CREATE TABLE dw.dim_zone (
    zone_sk SERIAL PRIMARY KEY,
    zone_bk VARCHAR(50) UNIQUE NOT NULL,
    zone_name VARCHAR(100),
    city VARCHAR(100),
    population INTEGER,
    area_km2 NUMERIC(10,2),
    longitude NUMERIC(9,6),
    latitude NUMERIC(9,6),
    centroid GEOMETRY(POINT, 4326)
);

CREATE INDEX idx_zone_geom ON dw.dim_zone USING GIST (geom);

-- Dimension Type de Déchet
DROP TABLE IF EXISTS dw.dim_waste_type CASCADE;
CREATE TABLE dw.dim_waste_type (
    waste_type_sk SERIAL PRIMARY KEY,
    waste_type_bk VARCHAR(50) UNIQUE NOT NULL,
    waste_type_name VARCHAR(100) NOT NULL
);

-- Dimension Agent
DROP TABLE IF EXISTS dw.dim_agent CASCADE;
CREATE TABLE dw.dim_agent (
    agent_sk SERIAL PRIMARY KEY,
    agent_bk VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(150),
    role VARCHAR(50),
    is_active BOOLEAN NOT NULL
);

-- Dimension Device
DROP TABLE IF EXISTS dw.dim_device CASCADE;
CREATE TABLE dw.dim_device (
    device_sk SERIAL PRIMARY KEY,
    device_bk VARCHAR(50) UNIQUE NOT NULL,
    model VARCHAR(100),
    installation_date DATE,
    is_active BOOLEAN NOT NULL
);

-- Dimension Route
DROP TABLE IF EXISTS dw.dim_route CASCADE;
CREATE TABLE dw.dim_route (
    route_sk SERIAL PRIMARY KEY,
    route_bk VARCHAR(50) UNIQUE NOT NULL,
    route_date DATE NOT NULL,
    planned_distance_m NUMERIC(10,2),
    planned_duration_min INTEGER,
    path GEOMETRY(LINESTRING, 4326)
);
CREATE INDEX idx_route_path ON dw.dim_route USING GIST (path);

-- Dimension Conteneur (SCD Type 2)
DROP TABLE IF EXISTS dw.dim_container CASCADE;
CREATE TABLE dw.dim_container (
    container_sk SERIAL PRIMARY KEY,
    container_bk VARCHAR(50) NOT NULL,
    container_type VARCHAR(50),
    capacity_l INTEGER,
    zone_bk VARCHAR(50),
    longitude NUMERIC(9,6),
    latitude NUMERIC(9,6),
    location GEOMETRY(POINT, 4326),
    status VARCHAR(30),
    date_debut DATE NOT NULL,
    date_fin DATE,
    version INTEGER NOT NULL,
    is_current BOOLEAN NOT NULL,
    CONSTRAINT uq_container_version UNIQUE (container_bk, version),
    CONSTRAINT chk_container_dates CHECK (date_fin IS NULL OR date_fin > date_debut)
);
CREATE INDEX idx_container_location ON dw.dim_container USING GIST (location);
CREATE INDEX idx_container_current ON dw.dim_container (container_bk, is_current);

-- Table de faits
DROP TABLE IF EXISTS dw.fact_measurement CASCADE;


CREATE TABLE dw.fact_measurement (
    measurement_sk BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    time_sk INTEGER NOT NULL,
    container_sk INTEGER NOT NULL,
    zone_sk INTEGER NOT NULL,
    waste_type_sk INTEGER,
    agent_sk INTEGER,
    device_sk INTEGER,
    route_sk INTEGER,
    taux_remplissage_pct NUMERIC(5,2),
    volume_litres NUMERIC(10,2),
    temperature_c NUMERIC(5,2),
    battery_pct SMALLINT,
	poids_estime_kg NUMERIC(9,2),
    is_overflow BOOLEAN,
    measurement_timestamp TIMESTAMP NOT NULL,
    
    CONSTRAINT fk_time FOREIGN KEY (time_sk) REFERENCES dw.dim_time(time_sk),
    CONSTRAINT fk_container FOREIGN KEY (container_sk) REFERENCES dw.dim_container(container_sk),
    CONSTRAINT fk_zone FOREIGN KEY (zone_sk) REFERENCES dw.dim_zone(zone_sk),
    CONSTRAINT fk_waste FOREIGN KEY (waste_type_sk) REFERENCES dw.dim_waste_type(waste_type_sk),
    CONSTRAINT fk_agent FOREIGN KEY (agent_sk) REFERENCES dw.dim_agent(agent_sk),
    CONSTRAINT fk_device FOREIGN KEY (device_sk) REFERENCES dw.dim_device(device_sk),
    CONSTRAINT fk_route FOREIGN KEY (route_sk) REFERENCES dw.dim_route(route_sk)
);

-- Index
CREATE INDEX idx_fact_time ON dw.fact_measurement (time_sk);
CREATE INDEX idx_fact_container ON dw.fact_measurement (container_sk);
CREATE INDEX idx_fact_zone ON dw.fact_measurement (zone_sk);
CREATE INDEX idx_fact_route ON dw.fact_measurement (route_sk);
