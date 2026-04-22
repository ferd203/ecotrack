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

logger = logging.getLogger(__name__)


# ============================================
# TRANSFORM DIM_WASTE_TYPE MINIO
# ============================================
def transform_dim_waste_type_minio(
    source_key: str = "incoming/waste_type.csv",
    transformed_prefix: str = "transformed/",
    archive_prefix: str = "archive/",
):
    try:
        load_dotenv()

        endpoint = os.getenv("MINIO_ENDPOINT")
        access_key = os.getenv("MINIO_ACCESS_KEY")
        secret_key = os.getenv("MINIO_SECRET_KEY")
        bucket = os.getenv("MINIO_BUCKET")

        if not all([endpoint, access_key, secret_key, bucket]):
            raise ValueError("Variables MinIO manquantes dans le fichier .env")

        client = boto3.client(
            "s3",
            endpoint_url=endpoint,
            aws_access_key_id=access_key,
            aws_secret_access_key=secret_key,
        )

        logger.info(f"Lecture du fichier MinIO : {source_key}")

        response = client.get_object(Bucket=bucket, Key=source_key)
        df = pd.read_csv(BytesIO(response["Body"].read()), encoding="utf-8")

        logger.info(f"{len(df)} lignes lues depuis MinIO")

        df.columns = df.columns.str.strip()

        # ----------------------------------
        # SUPPRESSION CLÉ TECHNIQUE SOURCE
        # ----------------------------------
        df.drop(columns=["waste_type_sk"], inplace=True, errors="ignore")

        # ----------------------------------
        # NETTOYAGE DONNÉES
        # ----------------------------------
        if "waste_type_bk" in df.columns:
            df["waste_type_bk"] = df["waste_type_bk"].astype(str).str.strip()

        if "waste_type_name" in df.columns:
            df["waste_type_name"] = df["waste_type_name"].astype(str).str.strip()

        # ----------------------------------
        # SUPPRESSION DOUBLONS
        # ----------------------------------
        df.drop_duplicates(inplace=True)

        # ----------------------------------
        # GÉNÉRATION NOMS DE FICHIERS
        # ----------------------------------
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
        archive_key = f"{archive_prefix}{run_timestamp}/{archived_filename}"

        # ----------------------------------
        # SAUVEGARDE FICHIER TRANSFORMÉ
        # ----------------------------------
        output_buffer = BytesIO()
        df.to_csv(output_buffer, index=False, encoding="utf-8")
        output_buffer.seek(0)

        client.put_object(
            Bucket=bucket,
            Key=transformed_key,
            Body=output_buffer.getvalue(),
            ContentType="text/csv",
        )

        logger.info("✅ Transformation DIM_WASTE_TYPE terminée")
        logger.info(f"📁 Fichier transformé : {transformed_key}")

        # ----------------------------------
        # ARCHIVAGE DU FICHIER SOURCE
        # ----------------------------------
        client.copy_object(
            Bucket=bucket,
            CopySource={"Bucket": bucket, "Key": source_key},
            Key=archive_key,
        )
        logger.info(f"📦 Fichier source archivé : {archive_key}")

        # ----------------------------------
        # SUPPRESSION FICHIER SOURCE
        # ----------------------------------
        client.delete_object(Bucket=bucket, Key=source_key)
        logger.info(f"🗑️ Fichier source supprimé : {source_key}")

        return df

    except Exception as e:
        logger.exception(f"Erreur transformation DIM_WASTE_TYPE : {e}")
        return None


# ============================================
# MAIN
# ============================================
if __name__ == "__main__":
    df = transform_dim_waste_type_minio()

    if df is not None:
        print("\nAPERÇU DIM_WASTE_TYPE TRANSFORMED")
        print(df.head())

        print("\nSTRUCTURE")
        print(df.dtypes)