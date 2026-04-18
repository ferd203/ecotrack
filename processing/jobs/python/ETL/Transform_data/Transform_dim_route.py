# ETL/Transform_data/Transform_dim_route.py 

import pandas as pd
import logging
import os

# ============================================
# CONFIGURATION LOGGING
# ============================================

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger(__name__)

# ============================================
# TRANSFORM FUNCTION
# ============================================

def transform_dim_route(
        input_path="staging/DIM_Route_STAGING.csv",
        output_path="csv_files/DIM_ROUTE_TRANSFORMED.csv"
):
    """
    Transformation des données DIM_ROUTE :
    - Conversion date
    - Normalisation distances
    - Conversion durées
    - Suppression doublons
    - Export CSV transformé
    """

    try:
        logger.info("🚀 Début transformation DIM_ROUTE")

        # 1️⃣ Lecture fichier staging
        df = pd.read_csv(input_path)
        logger.info(f"{len(df)} lignes à transformer")

        # ===============================
        # Conversion date
        # ===============================
        df["route_date"] = pd.to_datetime(df["route_date"]).dt.date

        # ===============================
        # Normalisation distances
        # ===============================
        df["planned_distance_m"] = df["planned_distance_m"].round(2)
        df["actual_distance_m"] = df["actual_distance_m"].round(2)

        # ===============================
        # Durées en entier
        # ===============================
        df["planned_duration_min"] = df["planned_duration_min"].astype(int)
        df["actual_duration_min"] = df["actual_duration_min"].astype(int)

        # ===============================
        # Suppression doublons BK
        # ===============================
        df = df.drop_duplicates(subset=["route_bk"])

        # ===============================
        # Sauvegarde fichier transformé
        # ===============================
        os.makedirs(os.path.dirname(output_path), exist_ok=True)
        df.to_csv(output_path, index=False)

        logger.info(f"✅ Transformation terminée : {len(df)} routes sauvegardées.")
        logger.info(f"📁 Fichier généré : {output_path}")

        return df

    except Exception as e:
        logger.error(f"❌ Erreur transformation DIM_ROUTE : {e}")
        raise


# ============================================
# EXECUTION STANDALONE
# ============================================

if __name__ == "__main__":

    df = transform_dim_route()

    print(df.head())
