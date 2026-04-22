import logging
import os
from datetime import datetime
from io import BytesIO

import boto3
import pandas as pd
from dotenv import load_dotenv

# ============================================
# CONFIGURATION LOGGING
# ============================================
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger(__name__)


# ============================================
# TRANSFORM FUNCTION MINIO
# ============================================
def transform_dim_container_minio(
    source_key: str = "incoming/container.csv",
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

        logger.info("🚀 Début transformation DIM_CONTAINER")
        logger.info(f"Lecture du fichier MinIO : {source_key}")

        # 1️⃣ Lecture fichier source
        response = client.get_object(Bucket=bucket, Key=source_key)
        df = pd.read_csv(BytesIO(response["Body"].read()))

        logger.info(f"{len(df)} lignes à transformer")

        df.columns = df.columns.str.strip()

        # 2️⃣ Nettoyage colonnes texte
        df["container_bk"] = df["container_bk"].astype(str).str.strip()
        df["container_type"] = df["container_type"].astype(str).str.strip()
        df["zone_bk"] = df["zone_bk"].astype(str).str.strip().str.upper()
        df["status"] = df["status"].astype(str).str.strip().str.upper()

        # 3️⃣ Conversion types numériques
        df["capacity_l"] = pd.to_numeric(df["capacity_l"], errors="coerce")
        df["latitude"] = pd.to_numeric(df["latitude"], errors="coerce")
        df["longitude"] = pd.to_numeric(df["longitude"], errors="coerce")

        # 4️⃣ Dates
        df["date_debut"] = pd.to_datetime(
            df["date_debut"],
            errors="coerce"
        ).dt.date

        df["date_fin"] = pd.to_datetime(
            df["date_fin"],
            errors="coerce"
        ).dt.date

        # 5️⃣ SCD Colonnes
        df["version"] = pd.to_numeric(df["version"], errors="coerce")

        # garde ta logique actuelle
        df["is_current"] = df["is_current"].astype(bool)

        # Sécurité : toute ligne avec date_fin NULL = current
        df.loc[df["date_fin"].isna(), "is_current"] = True

        # Optionnel mais cohérent avec les autres scripts
        df["etl_date"] = pd.Timestamp.now()

        # 6️⃣ Génération noms de fichiers
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

        # 7️⃣ Sauvegarde fichier transformé
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

        # 8️⃣ Archivage fichier source
        client.copy_object(
            Bucket=bucket,
            CopySource={"Bucket": bucket, "Key": source_key},
            Key=archive_key,
        )

        logger.info(f"📦 Fichier archivé : {archive_key}")

        # 9️⃣ Suppression fichier source de incoming
        client.delete_object(Bucket=bucket, Key=source_key)

        logger.info(f"🗑️ Fichier supprimé de incoming : {source_key}")
        logger.info("✅ Transformation DIM_CONTAINER terminée avec succès")

        return df

    except Exception as e:
        logger.exception(f"❌ Erreur transformation DIM_CONTAINER : {e}")
        return None


# ============================================
# MAIN
# ============================================
if __name__ == "__main__":
    df = transform_dim_container_minio()

    if df is not None:
        print("\nAPERÇU DIM_CONTAINER TRANSFORMED")
        print(df.head())

        print("\nSTRUCTURE")
        print(df.dtypes)