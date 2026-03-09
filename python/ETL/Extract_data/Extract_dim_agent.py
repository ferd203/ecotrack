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
# EXTRACT DIM_WASTE_TYPE
# ============================================
def extract_dim_waste_type(
    input_csv="csv_files\dim_waste_type (1).csv",
    output_csv="csv_files/STG_DIM_WASTE_TYPE.csv"
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

        logger.info(f"{len(df)} lignes extraites")

        # ----------------------------------
        # 3. Nettoyage colonnes
        # ----------------------------------
        df.columns = df.columns.str.strip()

        # Suppression lignes totalement vides
        df.dropna(how="all", inplace=True)

        # ----------------------------------
        # 4. Sauvegarde STAGING
        # ----------------------------------
        os.makedirs("csv_files", exist_ok=True)

        df.to_csv(
            output_csv,
            index=False,
            encoding="utf-8"
        )

        logger.info("✅ Extraction DIM_WASTE_TYPE terminée")
        logger.info(f"📁 Fichier staging : {output_csv}")

        return df

    except Exception as e:
        logger.error(f"Erreur extraction : {e}")
        return None


# ============================================
# MAIN
# ============================================
if __name__ == "__main__":

    df = extract_dim_waste_type()

    if df is not None:
        print("\nAPERÇU DIM_WASTE_TYPE STAGING")
        print(df.head())

        print("\nSTRUCTURE")
        print(df.dtypes)