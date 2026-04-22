import os

from dotenv import load_dotenv
from sqlalchemy import create_engine, text


def get_engine():
    load_dotenv()

    db_user = os.getenv("POSTGRES_USER")
    db_password = os.getenv("POSTGRES_PASSWORD")
    db_host = os.getenv("POSTGRES_HOST")
    db_port = os.getenv("POSTGRES_PORT", "5432")
    db_name = os.getenv("POSTGRES_DB")

    required_vars = [db_user, db_password, db_host, db_name]
    if not all(required_vars):
        raise ValueError("Variables PostgreSQL manquantes dans le fichier .env")

    return create_engine(
        f"postgresql://{db_user}:{db_password}@{db_host}:{db_port}/{db_name}"
    )


def init_raw_schema():
    engine = get_engine()

    ddl_statements = [

        # ============================
        # SCHEMA
        # ============================
        """
        CREATE SCHEMA IF NOT EXISTS raw;
        """,

        # ============================
        # INGESTION LOG
        # ============================
        """
        CREATE TABLE IF NOT EXISTS raw.ingestion_log (
            file_name TEXT PRIMARY KEY,
            file_path TEXT,
            target_table TEXT,
            row_count INTEGER,
            load_status TEXT,
            load_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );
        """,

        # ============================
        # DIM_AGENT
        # ============================
        """
        CREATE TABLE IF NOT EXISTS raw.dim_agent_raw (
            id SERIAL PRIMARY KEY,
            agent_bk TEXT,
            firstname TEXT,
            lastname TEXT,
            role TEXT,
            is_active BOOLEAN,
            etl_date TIMESTAMP
        );
        """,

        # ============================
        # DIM_ZONE
        # ============================
        """
        CREATE TABLE IF NOT EXISTS raw.dim_zone_raw (
            zone_sk SERIAL PRIMARY KEY,
            zone_bk TEXT,
            zone_name TEXT,
            city TEXT,
            population INTEGER,
            area_km2 DOUBLE PRECISION,
            latitude DOUBLE PRECISION,
            longitude DOUBLE PRECISION,
            etl_date TIMESTAMP
        );
        """,

        # ============================
        # DIM_DEVICE
        # ============================
        """
        CREATE TABLE IF NOT EXISTS raw.dim_device_raw (
            id SERIAL PRIMARY KEY,
            device_bk TEXT,
            model TEXT,
            installation_date TIMESTAMP,
            is_active BOOLEAN,
            etl_date TIMESTAMP
        );
        """,

        # ============================
        # DIM_CONTAINER
        # ============================
        """
        CREATE TABLE IF NOT EXISTS raw.dim_container_raw (
            container_sk SERIAL PRIMARY KEY,
            container_bk TEXT,
            container_type TEXT,
            capacity_l INTEGER,
            zone_bk TEXT,
            latitude DOUBLE PRECISION,
            longitude DOUBLE PRECISION,
            status TEXT,
            date_debut DATE,
            date_fin DATE,
            version INTEGER,
            is_current BOOLEAN,
            etl_date TIMESTAMP
        );
        """,

        # ============================
        # DIM_WASTE_TYPE
        # ============================
        """
        CREATE TABLE IF NOT EXISTS raw.dim_waste_type_raw (
            id SERIAL PRIMARY KEY,
            waste_type_bk TEXT,
            waste_type_name TEXT,
            etl_date TIMESTAMP
        );
        """,

        # ============================================================
        # TOURNEE (une ligne = une tournée)
        # ============================================================
        """
        CREATE TABLE IF NOT EXISTS raw.tournee_raw (
            tour_instance_bk TEXT PRIMARY KEY,
            service_date DATE,
            route_sk INTEGER,
            route_bk TEXT,
            zone_bk TEXT,
            zone_name TEXT,
            city TEXT,
            start_datetime TIMESTAMP,
            end_datetime TIMESTAMP,
            visited_container_count INTEGER,
            zone_container_count INTEGER,
            all_zone_containers_visited BOOLEAN,
            planned_distance_m DOUBLE PRECISION,
            actual_distance_m DOUBLE PRECISION,
            planned_duration_min INTEGER,
            actual_duration_min INTEGER,
            etl_date TIMESTAMP
        );
        """,

        # ============================================================
        # TOURNEE_AGENT
        # (plusieurs agents par tournée)
        # ============================================================
        """
        CREATE TABLE IF NOT EXISTS raw.tournee_agent_raw (
            id SERIAL PRIMARY KEY,
            tour_instance_bk TEXT,
            service_date DATE,
            zone_bk TEXT,
            route_bk TEXT,
            agent_bk TEXT,
            role_in_tour TEXT,
            start_datetime TIMESTAMP,
            end_datetime TIMESTAMP,
            etl_date TIMESTAMP
        );
        """,

        # ============================================================
        # TOURNEE_CONTENEUR
        # (ordre de visite des conteneurs)
        # ============================================================
        """
        CREATE TABLE IF NOT EXISTS raw.tournee_conteneur_raw (
            id SERIAL PRIMARY KEY,
            tour_instance_bk TEXT,
            service_date DATE,
            route_sk INTEGER,
            route_bk TEXT,
            zone_bk TEXT,
            city TEXT,
            container_sk INTEGER,
            container_bk TEXT,
            visit_order INTEGER,
            visit_datetime TIMESTAMP,
            latitude DOUBLE PRECISION,
            longitude DOUBLE PRECISION,
            container_type TEXT,
            capacity_l INTEGER,
            etl_date TIMESTAMP
        );
        """,

        """
            CREATE TABLE IF NOT EXISTS raw.mesures_raw (
            id BIGSERIAL PRIMARY KEY,
            measurement_timestamp TIMESTAMP,
            time_sk INTEGER,
            container_sk INTEGER,
            zone_sk INTEGER,
            taux_remplissage_pct DOUBLE PRECISION,
            volume_litres DOUBLE PRECISION,
            temperature_c DOUBLE PRECISION,
            batterie_pct INTEGER,
            poids_estime_kg DOUBLE PRECISION,
            is_overflow INTEGER,
            waste_type_sk INTEGER,
            device_sk INTEGER
        );
        """
    ]

    with engine.begin() as conn:
        for ddl in ddl_statements:
            conn.execute(text(ddl))

    print("✅ Architecture raw créée avec succès.")


if __name__ == "__main__":
    init_raw_schema()