# ETL/Extract_data/Extract_dim_zone.py

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
# EXTRACT DIM_ZONE
# ============================================
def extract_dim_zone(
    input_csv="csv_files\dim_zone.csv",
    output_csv="csv_files/STG_DIM_ZONE.csv"
):
    try:
        # ----------------------------------
        # 1. Vérification existence fichier
        # ----------------------------------
        if not os.path.exists(input_csv):
            logger.error(f"Fichier introuvable : {input_csv}")
            return None

        # ----------------------------------
        # 2. Lecture CSV brut
        # ----------------------------------
        df = pd.read_csv(input_csv, encoding="utf-8")

        # ----------------------------------
        # 3. Suppression lignes vides
        # ----------------------------------
        df.dropna(how="all", inplace=True)

        logger.info(f"{len(df)} lignes extraites")

        # ----------------------------------
        # 4. Nettoyage colonnes
        # ----------------------------------
        df.columns = df.columns.str.strip()

        # ----------------------------------
        # 5. Sauvegarde STAGING
        # ----------------------------------
        os.makedirs("csv_files", exist_ok=True)
        df.to_csv(output_csv, index=False, encoding="utf-8")

        logger.info("✅ Extraction DIM_ZONE terminée")
        logger.info(f"📁 Fichier staging : {output_csv}")

        return df

    except Exception as e:
        logger.error(f"Erreur extraction : {e}")
        return None

# ============================================
# MAIN
# ============================================
if __name__ == "__main__":
    df = extract_dim_zone()
    if df is not None:
        print("\nAPERÇU DIM_ZONE STAGING")
        print(df.head())
        print("\nSTRUCTURE")
        print(df.dtypes)
