import pandas as pd
from utils.logger import get_logger

logger = get_logger("EXTRACT_DIM_WASTE_TYPE")


def extract_dim_waste_type(
    input_path="csv_files\dim_waste_type (1).csv",
    output_path="csv_files/DIM_WASTE_TYPE_EXTRACTED.csv"
):
    try:
        logger.info("🚀 Début extraction DIM_WASTE_TYPE")

        df = pd.read_csv(input_path)

        logger.info(f"{len(df)} lignes extraites")

        df.to_csv(output_path, index=False)

        logger.info(f"✅ Extraction sauvegardée : {output_path}")

    except Exception as e:
        logger.error(f"❌ Erreur extraction DIM_WASTE_TYPE : {e}")


if __name__ == "__main__":
    extract_dim_waste_type()
