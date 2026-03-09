# ETL/Transform_data/Transform_dim_zone.py

import pandas as pd
import os
import logging

# ============================================
# LOGGING
# ============================================
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger(__name__)

# ============================================
# TRANSFORM DIM_ZONE
# ============================================
def transform_dim_zone(
    input_csv="csv_files/STG_DIM_ZONE.csv",
    output_csv="csv_files/DIM_ZONE_TRANSFORMED.csv"
):
    try:
        # ----------------------------------
        # 1. Vérification fichier staging
        # ----------------------------------
        if not os.path.exists(input_csv):
            logger.error(f"Fichier staging introuvable : {input_csv}")
            return None

        # ----------------------------------
        # 2. Lecture fichier staging
        # ----------------------------------
        df = pd.read_csv(input_csv, encoding="utf-8")
        logger.info(f"{len(df)} lignes lues depuis staging")

        # ----------------------------------
        # 3. Supprimer clé technique source
        # ----------------------------------
        df.drop(columns=["zone_sk"], inplace=True, errors="ignore")

        # ----------------------------------
        # 4. Nettoyage texte
        # ----------------------------------
        df["zone_bk"] = df["zone_bk"].str.strip()
        df["zone_name"] = df["zone_name"].str.strip()
        df["city"] = df["city"].str.strip()

        # ----------------------------------
        # 5. Conversion numérique
        # ----------------------------------
        df["population"] = pd.to_numeric(df["population"], errors="coerce")
        df["area_km2"] = pd.to_numeric(df["area_km2"], errors="coerce")
        df["latitude"] = pd.to_numeric(df["latitude"], errors="coerce")
        df["longitude"] = pd.to_numeric(df["longitude"], errors="coerce")

        # ----------------------------------
        # 6. Suppression doublons
        # ----------------------------------
        df.drop_duplicates(inplace=True)

        # ----------------------------------
        # 7. Sauvegarde transformé
        # ----------------------------------
        df.to_csv(output_csv, index=False, encoding="utf-8")
        logger.info("✅ Transformation DIM_ZONE terminée")
        logger.info(f"📁 Fichier transformé : {output_csv}")

        return df

    except Exception as e:
        logger.error(f"Erreur transformation : {e}")
        return None

# ============================================
# MAIN
# ============================================
if __name__ == "__main__":
    df = transform_dim_zone()
    if df is not None:
        print("\nAPERÇU DIM_ZONE TRANSFORMED")
        print(df.head())
        print("\nSTRUCTURE")
        print(df.dtypes)
