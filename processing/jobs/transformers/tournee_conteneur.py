import logging
import os
from datetime import datetime
from io import BytesIO

import boto3
import pandas as pd
from dotenv import load_dotenv

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger(__name__)


def transform_tournee_conteneur_minio(
    source_key: str = "incoming/tournee_conteneurs.csv",
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

        logger.info("🚀 Début transformation TOURNEE_CONTENEUR")
        logger.info(f"Lecture du fichier MinIO : {source_key}")

        response = client.get_object(Bucket=bucket, Key=source_key)
        df = pd.read_csv(BytesIO(response["Body"].read()))

        logger.info(f"{len(df)} lignes à transformer")

        df.columns = df.columns.str.strip()

        # Nettoyage texte
        text_cols = [
            "tour_instance_bk",
            "route_bk",
            "zone_bk",
            "city",
            "container_bk",
            "container_type",
        ]
        for col in text_cols:
            if col in df.columns:
                df[col] = df[col].astype(str).str.strip()

        if "zone_bk" in df.columns:
            df["zone_bk"] = df["zone_bk"].str.upper()

        if "city" in df.columns:
            df["city"] = df["city"].str.title()

        # Conversions numériques
        numeric_cols = [
            "route_sk",
            "container_sk",
            "visit_order",
            "latitude",
            "longitude",
            "capacity_l",
        ]
        for col in numeric_cols:
            if col in df.columns:
                df[col] = pd.to_numeric(df[col], errors="coerce")

        # Dates / timestamps
        if "service_date" in df.columns:
            df["service_date"] = pd.to_datetime(df["service_date"], errors="coerce").dt.date

        if "visit_datetime" in df.columns:
            df["visit_datetime"] = pd.to_datetime(df["visit_datetime"], errors="coerce")

        # Doublons
        subset_cols = [c for c in ["tour_instance_bk", "container_bk", "visit_order"] if c in df.columns]
        if subset_cols:
            df.drop_duplicates(subset=subset_cols, inplace=True)
        else:
            df.drop_duplicates(inplace=True)

        df["etl_date"] = pd.Timestamp.now()

        run_timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")

        source_filename = source_key.split("/")[-1]
        source_stem, source_ext = source_filename.rsplit(".", 1)

        transformed_filename = f"{source_stem}_TRANSFORMED_{run_timestamp}.{source_ext}"
        archived_filename = f"{source_stem}_{run_timestamp}.{source_ext}"

        transformed_key = f"{transformed_prefix}{run_timestamp}/{transformed_filename}"
        archive_key = f"{archive_prefix}{run_timestamp}/{archived_filename}"

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

        client.copy_object(
            Bucket=bucket,
            CopySource={"Bucket": bucket, "Key": source_key},
            Key=archive_key,
        )

        logger.info(f"📦 Fichier archivé : {archive_key}")

        client.delete_object(Bucket=bucket, Key=source_key)

        logger.info(f"🗑️ Fichier supprimé de incoming : {source_key}")
        logger.info("✅ Transformation TOURNEE_CONTENEUR terminée avec succès")

        return df

    except Exception as e:
        logger.exception(f"❌ Erreur transformation TOURNEE_CONTENEUR : {e}")
        return None


if __name__ == "__main__":
    df = transform_tournee_conteneur_minio()

    if df is not None:
        print("\nAPERÇU TOURNEE_CONTENEUR TRANSFORMED")
        print(df.head())

        print("\nSTRUCTURE")
        print(df.dtypes)