# ETL/Load_data/Load_dim_route.py

import pandas as pd
import logging
import random
from psycopg2.extras import execute_values
from utils.db import get_connection

# ============================================
# CONFIGURATION LOGGING
# ============================================

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger(__name__)

# ============================================
# GÉNÉRATION GEOMETRY LINESTRING
# ============================================

def generate_random_linestring():
    """
    Génère une LineString simulée (SRID 4326).
    Coordonnées centrées sur une zone exemple.
    """

    base_lat = 46.81
    base_lon = -71.21

    points = []

    # Entre 3 et 8 points pour simuler une tournée
    for _ in range(random.randint(3, 8)):
        lat = base_lat + random.uniform(-0.05, 0.05)
        lon = base_lon + random.uniform(-0.05, 0.05)
        points.append(f"{lon} {lat}")

    return f"LINESTRING({', '.join(points)})"

# ============================================
# LOAD FUNCTION
# ============================================

def load_dim_route(
        input_path="csv_files/DIM_ROUTE_TRANSFORMED.csv"
):
    """
    Chargement des routes transformées vers DW.DIM_ROUTE
    avec génération de la géométrie PostGIS.
    """

    try:
        logger.info("🚀 Début chargement DIM_ROUTE")

        # 1️⃣ Lecture fichier transformé
        df = pd.read_csv(input_path)

        logger.info(f"{len(df)} routes à charger")

        conn = get_connection()
        cur = conn.cursor()

        records = []

        # 2️⃣ Préparation des données + génération geom
        for _, row in df.iterrows():

            geom_wkt = generate_random_linestring()

            records.append((
                row["route_bk"],
                row["route_date"],
                row["planned_distance_m"],
                row["planned_duration_min"],
                row["actual_distance_m"],
                row["actual_duration_min"],
                geom_wkt
            ))

        # 3️⃣ Insert en batch optimisé
        execute_values(
            cur,
            """
            INSERT INTO DW.DIM_ROUTE (
                route_bk,
                route_date,
                planned_distance_m,
                planned_duration_min,
                actual_distance_m,
                actual_duration_min,
                geom
            )
            VALUES %s
            ON CONFLICT (route_bk) DO NOTHING
            """,
            records,
            template="""
            (
                %s, %s, %s, %s, %s, %s,
                public.ST_GeomFromText(%s, 4326)
            )
            """,
            page_size=500
        )

        conn.commit()
        cur.close()
        conn.close()

        logger.info("✅ Chargement DIM_ROUTE terminé avec succès")

    except Exception as e:
        logger.error(f"❌ Erreur chargement DIM_ROUTE : {e}")
        raise


# ============================================
# EXECUTION STANDALONE
# ============================================

if __name__ == "__main__":

    load_dim_route()
