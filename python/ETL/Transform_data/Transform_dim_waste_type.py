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
# TRANSFORM DIM_WASTE_TYPE
# ============================================
def transform_dim_waste_type(
    input_csv="csv_files/DIM_WASTE_TYPE_EXTRACTED.csv",
    output_csv="csv_files/DIM_WASTE_TYPE_TRANSFORMED.csv"
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
        # 3. Suppression clé technique source
        # ----------------------------------
        df.drop(columns=["waste_type_sk"], inplace=True, errors="ignore")

        # ----------------------------------
        # 4. Nettoyage données
        # ----------------------------------
        df["waste_type_bk"] = df["waste_type_bk"].str.strip()
        df["waste_type_name"] = df["waste_type_name"].str.strip()

        # Suppression doublons éventuels
        df.drop_duplicates(inplace=True)


        # ----------------------------------
        # 6. Sauvegarde fichier transformé
        # ----------------------------------
        df.to_csv(
            output_csv,
            index=False,
            encoding="utf-8"
        )

        logger.info("✅ Transformation DIM_WASTE_TYPE terminée")
        logger.info(f"📁 Fichier transformé : {output_csv}")

        return df

    except Exception as e:
        logger.error(f"Erreur transformation : {e}")
        return None


# ============================================
# MAIN
# ============================================
if __name__ == "__main__":

    df = transform_dim_waste_type()

    if df is not None:
        print("\nAPERÇU DIM_WASTE_TYPE TRANSFORMED")
        print(df.head())

        print("\nSTRUCTURE")
        print(df.dtypes)
