import logging
import os
from datetime import datetime
from io import BytesIO

import boto3
import pandas as pd
from dotenv import load_dotenv

# ============================================
# LOGGING
# ============================================
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s"
)

logger = logging.getLogger("TRANSFORM_DIM_DEVICE")


# ============================================
# TRANSFORM DIM_DEVICE MINIO
# ============================================
def transform_dim_device_minio(
    source_key: str = "incoming/device.csv",
    transformed_prefix: str = "transformed/",
    archive_prefix: str = "archive/",
):

    try:

        # ==============================
        # Chargement .env
        # ==============================

        load_dotenv()

        endpoint = os.getenv("MINIO_ENDPOINT")
        access_key = os.getenv("MINIO_ACCESS_KEY")
        secret_key = os.getenv("MINIO_SECRET_KEY")
        bucket = os.getenv("MINIO_BUCKET")

        if not all([endpoint, access_key, secret_key, bucket]):
            raise ValueError("Variables MinIO manquantes")

        # ==============================
        # Connexion MinIO
        # ==============================

        client = boto3.client(
            "s3",
            endpoint_url=endpoint,
            aws_access_key_id=access_key,
            aws_secret_access_key=secret_key,
        )

        logger.info("🚀 Début transformation DIM_DEVICE")

        # ==============================
        # Lecture fichier source
        # ==============================

        response = client.get_object(Bucket=bucket, Key=source_key)

        df = pd.read_csv(BytesIO(response["Body"].read()))

        logger.info(f"{len(df)} lignes lues")

        df.columns = df.columns.str.strip()

        # ==============================
        # Transformation
        # ==============================

        df = df.drop(columns=["device_sk"], errors="ignore")

        df["installation_date"] = pd.to_datetime(
            df["installation_date"],
            errors="coerce"
        )

        df["device_bk"] = df["device_bk"].astype(str).str.strip()
        df["model"] = df["model"].astype(str).str.strip()

        df["is_active"] = df["is_active"].astype(bool)

        df["etl_date"] = pd.Timestamp.now()

        logger.info(f"{len(df)} lignes transformées")

        # ==============================
        # Génération noms fichiers
        # ==============================

        run_timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")

        source_filename = source_key.split("/")[-1]
        source_stem, source_ext = source_filename.rsplit(".", 1)

        transformed_filename = (
            f"{source_stem}_TRANSFORMED_{run_timestamp}.{source_ext}"
        )

        archived_filename = f"{source_stem}_{run_timestamp}.{source_ext}"

        transformed_key = (
            f"{transformed_prefix}{run_timestamp}/{transformed_filename}"
        )

        archive_key = (
            f"{archive_prefix}{run_timestamp}/{archived_filename}"
        )

        # ==============================
        # Sauvegarde transformé
        # ==============================

        output_buffer = BytesIO()

        df.to_csv(output_buffer, index=False)

        output_buffer.seek(0)

        client.put_object(
            Bucket=bucket,
            Key=transformed_key,
            Body=output_buffer.getvalue(),
            ContentType="text/csv",
        )

        logger.info(f"📁 Fichier transformé : {transformed_key}")

        # ==============================
        # Archivage fichier source
        # ==============================

        client.copy_object(
            Bucket=bucket,
            CopySource={"Bucket": bucket, "Key": source_key},
            Key=archive_key,
        )

        logger.info(f"📦 Fichier archivé : {archive_key}")

        # ==============================
        # Suppression incoming
        # ==============================

        client.delete_object(Bucket=bucket, Key=source_key)

        logger.info(f"🗑️ Fichier supprimé de incoming : {source_key}")

        logger.info("✅ Transformation DIM_DEVICE terminée")

        return df

    except Exception as e:

        logger.exception(f"❌ Erreur transformation DIM_DEVICE : {e}")

        return None


# ============================================
# MAIN
# ============================================

if __name__ == "__main__":

    df = transform_dim_device_minio()

    if df is not None:

        print("\nAPERÇU DIM_DEVICE TRANSFORMED")

        print(df.head())

        print("\nSTRUCTURE")

        print(df.dtypes)