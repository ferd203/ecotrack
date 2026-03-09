# ETL/Transform_data/Transform_dim_container.py

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
def transform_dim_container(
        input_path="staging/DIM_CONTAINER_STAGING.csv",
        output_path="csv_files/DIM_CONTAINER_TRANSFORMED.csv"
):

    try:
        logger.info("🚀 Début transformation DIM_CONTAINER")

        # 1️⃣ Lecture fichier staging
        df = pd.read_csv(input_path)

        logger.info(f"{len(df)} lignes à transformer")

        # --------------------------------------------------
        # 2️⃣ Nettoyage colonnes texte
        # --------------------------------------------------
        df["container_bk"] = df["container_bk"].str.strip()
        df["container_type"] = df["container_type"].str.strip()
        df["zone_bk"] = df["zone_bk"].str.strip().str.upper()
        df["status"] = df["status"].str.strip().str.upper()

        # --------------------------------------------------
        # 3️⃣ Conversion types numériques
        # --------------------------------------------------
        df["capacity_l"] = df["capacity_l"].astype(int)
        df["latitude"] = df["latitude"].astype(float)
        df["longitude"] = df["longitude"].astype(float)

        # --------------------------------------------------
        # 4️⃣ Dates
        # --------------------------------------------------
        df["date_debut"] = pd.to_datetime(df["date_debut"]).dt.date

        df["date_fin"] = pd.to_datetime(
            df["date_fin"],
            errors="coerce"
        ).dt.date

        # --------------------------------------------------
        # 5️⃣ SCD Colonnes
        # --------------------------------------------------
        df["version"] = df["version"].astype(int)
        df["is_current"] = df["is_current"].astype(bool)

        # Sécurité : toute ligne avec date_fin NULL = current
        df.loc[df["date_fin"].isna(), "is_current"] = True

        # --------------------------------------------------
        # 6️⃣ Sauvegarde fichier transformé
        # --------------------------------------------------
        os.makedirs(os.path.dirname(output_path), exist_ok=True)

        df.to_csv(output_path, index=False)

        logger.info("✅ Transformation DIM_CONTAINER terminée avec succès")

    except Exception as e:
        logger.error(f"❌ Erreur transformation DIM_CONTAINER : {e}")


# ============================================
# MAIN
# ============================================
if __name__ == "__main__":
    transform_dim_container()
