import pandas as pd
from utils.logger import get_logger

logger = get_logger("TRANSFORM_DIM_DEVICE")


def transform_dim_device(input_path="csv_files/DIM_DEVICE_EXTRACTED.csv",
                         output_path="csv_files/DIM_DEVICE_TRANSFORMED.csv"):
    """
    Transformation DIM_DEVICE
    """

    try:
        logger.info("🚀 Début transformation DIM_DEVICE")

        df = pd.read_csv(input_path)

        # ==========================
        # Nettoyage
        # ==========================

        # Supprimer la clé technique source
        df = df.drop(columns=["device_sk"], errors="ignore")

        # Conversion date
        df["installation_date"] = pd.to_datetime(df["installation_date"])

        # Normalisation texte
        df["device_bk"] = df["device_bk"].str.strip()
        df["model"] = df["model"].str.strip()

        # Booléen propre
        df["is_active"] = df["is_active"].astype(bool)

        logger.info(f"{len(df)} lignes transformées")

        df.to_csv(output_path, index=False)

        logger.info(f"✅ Fichier transformé sauvegardé : {output_path}")

    except Exception as e:
        logger.error(f"❌ Erreur transformation DIM_DEVICE : {e}")


if __name__ == "__main__":
    transform_dim_device()
