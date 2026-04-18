# ETL/Extract_data/Extract_dim_container.py

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
# EXTRACT FUNCTION
# ============================================
def extract_dim_route(
        input_path="csv_files\dim_route.csv",
        output_path="staging/DIM_Route_STAGING.csv"
):

    try:
        logger.info("🚀 Début extraction DIM_ROUTE")

        # 1️⃣ Lecture CSV source
        df = pd.read_csv(input_path, sep=",", encoding="utf-8")

        logger.info(f"{len(df)} lignes extraites")

        # 2️⃣ Nettoyage simple noms colonnes
        df.columns = df.columns.str.strip().str.lower()

        # 3️⃣ Création dossier staging si absent
        os.makedirs(os.path.dirname(output_path), exist_ok=True)

        # 4️⃣ Sauvegarde vers staging
        df.to_csv(output_path, index=False, encoding="utf-8")

        logger.info("✅ Extraction DIM_ROUTE terminée avec succès")

    except Exception as e:
        logger.error(f"❌ Erreur extraction DIM_ROUTE : {e}")


# ============================================
# MAIN
# ============================================
if __name__ == "__main__":
    extract_dim_route()
