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


def transform_tournee_minio(
    source_key: str = "incoming/tournees.csv",
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

        logger.info("🚀 Début transformation TOURNEE")
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
            "zone_name",
            "city",
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
            "visited_container_count",
            "zone_container_count",
            "planned_distance_m",
            "actual_distance_m",
            "planned_duration_min",
            "actual_duration_min",
        ]
        for col in numeric_cols:
            if col in df.columns:
                df[col] = pd.to_numeric(df[col], errors="coerce")

        # Booléen
        if "all_zone_containers_visited" in df.columns:
            df["all_zone_containers_visited"] = df["all_zone_containers_visited"].astype(bool)

        # Dates / timestamps
        if "service_date" in df.columns:
            df["service_date"] = pd.to_datetime(df["service_date"], errors="coerce").dt.date

        for col in ["start_datetime", "end_datetime"]:
            if col in df.columns:
                df[col] = pd.to_datetime(df[col], errors="coerce")

        # Doublons
        if "tour_instance_bk" in df.columns:
            df.drop_duplicates(subset=["tour_instance_bk"], inplace=True)
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
        logger.info("✅ Transformation TOURNEE terminée avec succès")

        return df

    except Exception as e:
        logger.exception(f"❌ Erreur transformation TOURNEE : {e}")
        return None


if __name__ == "__main__":
    df = transform_tournee_minio()

    if df is not None:
        print("\nAPERÇU TOURNEE TRANSFORMED")
        print(df.head())

        print("\nSTRUCTURE")
        print(df.dtypes)