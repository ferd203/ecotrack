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


def transform_tournee_agent_minio(
    source_key: str = "incoming/tournee_agents.csv",
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

        logger.info("🚀 Début transformation TOURNEE_AGENT")
        logger.info(f"Lecture du fichier MinIO : {source_key}")

        response = client.get_object(Bucket=bucket, Key=source_key)
        df = pd.read_csv(BytesIO(response["Body"].read()))

        logger.info(f"{len(df)} lignes à transformer")

        df.columns = df.columns.str.strip()

        # Nettoyage texte
        text_cols = [
            "tour_instance_bk",
            "zone_bk",
            "route_bk",
            "agent_bk",
            "role_in_tour",
        ]
        for col in text_cols:
            if col in df.columns:
                df[col] = df[col].astype(str).str.strip()

        if "zone_bk" in df.columns:
            df["zone_bk"] = df["zone_bk"].str.upper()

        if "role_in_tour" in df.columns:
            df["role_in_tour"] = df["role_in_tour"].str.upper()

        # Dates / timestamps
        if "service_date" in df.columns:
            df["service_date"] = pd.to_datetime(df["service_date"], errors="coerce").dt.date

        for col in ["start_datetime", "end_datetime"]:
            if col in df.columns:
                df[col] = pd.to_datetime(df[col], errors="coerce")

        # Doublons
        subset_cols = [c for c in ["tour_instance_bk", "agent_bk", "role_in_tour"] if c in df.columns]
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
        logger.info("✅ Transformation TOURNEE_AGENT terminée avec succès")

        return df

    except Exception as e:
        logger.exception(f"❌ Erreur transformation TOURNEE_AGENT : {e}")
        return None


if __name__ == "__main__":
    df = transform_tournee_agent_minio()

    if df is not None:
        print("\nAPERÇU TOURNEE_AGENT TRANSFORMED")
        print(df.head())

        print("\nSTRUCTURE")
        print(df.dtypes)