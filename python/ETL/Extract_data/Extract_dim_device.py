import pandas as pd
from utils.logger import get_logger

logger = get_logger("EXTRACT_DIM_DEVICE")


def extract_dim_device(input_path="csv_files\dim_device (1).csv",
                       output_path="csv_files/DIM_DEVICE_EXTRACTED.csv"):
    """
    Extraction du fichier brut DIM_DEVICE
    """

    try:
        logger.info("🚀 Début extraction DIM_DEVICE")

        df = pd.read_csv(input_path)

        logger.info(f"{len(df)} lignes extraites")

        df.to_csv(output_path, index=False)

        logger.info(f"✅ Fichier extrait sauvegardé : {output_path}")

    except Exception as e:
        logger.error(f"❌ Erreur extraction DIM_DEVICE : {e}")


if __name__ == "__main__":
    extract_dim_device()
