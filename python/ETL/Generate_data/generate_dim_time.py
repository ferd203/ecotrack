# ETL/Generate/generate_dim_time.py

import pandas as pd
import psycopg2
from datetime import datetime
from utils.db import get_connection
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


def generate_dim_time(start_date="2020-01-01", end_date="2035-12-31"):

    logger.info("🚀 Génération DIM_TIME")

    dates = pd.date_range(start=start_date, end=end_date)

    df = pd.DataFrame()
    df["date_bk"] = dates
    df["time_sk"] = df["date_bk"].dt.strftime("%Y%m%d").astype(int)
    df["day"] = df["date_bk"].dt.day
    df["month"] = df["date_bk"].dt.month
    df["month_name"] = df["date_bk"].dt.strftime("%B")
    df["quarter"] = df["date_bk"].dt.quarter
    df["year"] = df["date_bk"].dt.year
    df["day_of_week"] = df["date_bk"].dt.dayofweek + 1
    df["is_weekend"] = df["day_of_week"].isin([6, 7])

    conn = get_connection()
    cur = conn.cursor()

    for _, row in df.iterrows():
        cur.execute("""
            INSERT INTO DW.DIM_TIME (
                time_sk,
                date_bk,
                day,
                month,
                month_name,
                quarter,
                year,
                day_of_week,
                is_weekend
            )
            VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s)
            ON CONFLICT (time_sk) DO NOTHING
        """, (
            row["time_sk"],
            row["date_bk"],
            row["day"],
            row["month"],
            row["month_name"],
            row["quarter"],
            row["year"],
            row["day_of_week"],
            row["is_weekend"]
        ))

    conn.commit()
    cur.close()
    conn.close()

    logger.info("✅ DIM_TIME générée avec succès")


if __name__ == "__main__":
    generate_dim_time()