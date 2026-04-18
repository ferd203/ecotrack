# ETL/Load_data/Load_dim_zone.py

import pandas as pd
import logging
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
# LOAD DIM_ZONE
# ============================================
def load_dim_zone(input_csv="csv_files/DIM_ZONE_TRANSFORMED.csv"):

    try:
        # ----------------------------------
        # 1. Lecture du fichier transformé
        # ----------------------------------
        df = pd.read_csv(input_csv, encoding="utf-8")
        logger.info(f"{len(df)} lignes à charger dans DIM_ZONE")

        # Sécurité : suppression coordonnées nulles
        df = df.dropna(subset=["latitude", "longitude"])

        # ----------------------------------
        # 2. Connexion base de données
        # ----------------------------------
        conn = get_connection()
        cur = conn.cursor()

        # ----------------------------------
        # 3. Insertion avec fonctions PostGIS
        # ----------------------------------
        for _, row in df.iterrows():

            cur.execute("""
                INSERT INTO DW.DIM_ZONE
                (
                    zone_bk,
                    zone_name,
                    city,
                    population,
                    area_km2,
                    latitude,
                    longitude,
                    geom
                )
                VALUES (
                    %s, %s, %s, %s, %s, %s, %s,
                    public.ST_SetSRID(
                        public.ST_MakePoint(
                            %s::double precision,
                            %s::double precision
                        ),
                        4326
                    )
                )
            """, (
                row["zone_bk"],
                row["zone_name"],
                row["city"],
                row["population"],
                row["area_km2"],
                float(row["latitude"]),
                float(row["longitude"]),
                float(row["longitude"]),  # IMPORTANT : longitude en premier
                float(row["latitude"])
            ))

        # ----------------------------------
        # 4. Commit
        # ----------------------------------
        conn.commit()
        cur.close()
        conn.close()

        logger.info(f"✅ Chargement DIM_ZONE terminé : {len(df)} lignes insérées")

    except Exception as e:
        logger.error(f"❌ Erreur lors du chargement DIM_ZONE : {e}")


# ============================================
# MAIN
# ============================================
if __name__ == "__main__":
    logger.info("🚀 Début du chargement DIM_ZONE")
    load_dim_zone()
