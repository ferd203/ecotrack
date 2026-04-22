import os
from io import BytesIO

import boto3
import pandas as pd
from dotenv import load_dotenv
from sqlalchemy import create_engine, text
from datetime import datetime


def get_engine():
    load_dotenv()

    db_user = os.getenv("POSTGRES_USER")
    db_password = os.getenv("POSTGRES_PASSWORD")
    db_host = os.getenv("POSTGRES_HOST")
    db_port = os.getenv("POSTGRES_PORT", "5432")
    db_name = os.getenv("POSTGRES_DB")

    if not all([db_user, db_password, db_host, db_name]):
        raise ValueError("Variables PostgreSQL manquantes dans .env")

    return create_engine(
        f"postgresql://{db_user}:{db_password}@{db_host}:{db_port}/{db_name}"
    )


def get_minio_client():
    load_dotenv()

    endpoint = os.getenv("MINIO_ENDPOINT")
    access_key = os.getenv("MINIO_ACCESS_KEY")
    secret_key = os.getenv("MINIO_SECRET_KEY")

    if not all([endpoint, access_key, secret_key]):
        raise ValueError("Variables MinIO manquantes dans .env")

    return boto3.client(
        "s3",
        endpoint_url=endpoint,
        aws_access_key_id=access_key,
        aws_secret_access_key=secret_key,
    )


def find_file(minio_client, bucket: str) -> tuple[str | None, bytes | None]:
    response = minio_client.list_objects_v2(
        Bucket=bucket,
        Prefix="transformed/",
    )

    if "Contents" not in response:
        return None, None

    for obj in response["Contents"]:
        key = obj["Key"]

        if key.endswith("/"):
            continue

        file_name = key.split("/")[-1].lower()

        if "zone_transformed" in file_name:
            file_obj = minio_client.get_object(Bucket=bucket, Key=key)
            return key, file_obj["Body"].read()

    return None, None


def is_file_already_loaded(engine, file_name: str) -> bool:
    query = text("""
        SELECT 1
        FROM raw.ingestion_log
        WHERE file_name = :file_name
        LIMIT 1
    """)

    with engine.begin() as conn:
        result = conn.execute(query, {"file_name": file_name}).fetchone()

    return result is not None


def register_loaded_file(engine, file_name: str, file_path: str, row_count: int) -> None:
    query = text("""
        INSERT INTO raw.ingestion_log (
            file_name,
            file_path,
            target_table,
            row_count,
            load_status
        )
        VALUES (
            :file_name,
            :file_path,
            'dim_waste_type_raw',
            :row_count,
            'LOADED'
        )
    """)

    with engine.begin() as conn:
        conn.execute(
            query,
            {
                "file_name": file_name,
                "file_path": file_path,
                "row_count": row_count,
            },
        )


def main() -> None:
    load_dotenv()

    bucket = os.getenv("MINIO_BUCKET")
    if not bucket:
        raise ValueError("MINIO_BUCKET manquant dans .env")

    engine = get_engine()
    minio_client = get_minio_client()

    key, file_bytes = find_file(minio_client, bucket)

    if key is None or file_bytes is None:
        print("Aucun fichier dim_zone_raw transformé trouvé.")
        return

    file_name = key.split("/")[-1]

    if is_file_already_loaded(engine, file_name):
        print(f"Fichier déjà chargé, ignoré : {file_name}")
        return

    print(f"Chargement de {file_name} vers raw.dim_zone_raw")

    df = pd.read_csv(BytesIO(file_bytes))

    df.to_sql(
        name="dim_zone_raw",
        con=engine,
        schema="raw",
        if_exists="append",
        index=False,
    )

    register_loaded_file(
        engine=engine,
        file_name=file_name,
        file_path=key,
        row_count=len(df),
    )

    print(f"Chargement terminé : raw.dim_zone_raw ({len(df)} lignes)")


if __name__ == "__main__":
    main()