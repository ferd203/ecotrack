# ETL/Streaming/simulate_iot_batch_v2.py

import pandas as pd
import numpy as np
from datetime import datetime, timedelta
import logging
from utils.db import get_connection
from psycopg2.extras import execute_values

# ============================================
# CONFIGURATION
# ============================================

MEASUREMENTS_PER_DAY = 500_000
DAYS_TO_GENERATE = 30
BATCH_SIZE = 10_000

# ============================================
# CONFIGURATION LOGGING
# ============================================

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger(__name__)

# ============================================
# CHARGEMENT CONTENEURS ACTIFS
# ============================================

def load_active_containers():
    conn = get_connection()
    cur = conn.cursor()
    cur.execute("""
        SELECT container_sk, container_bk, zone_bk
        FROM DW.DIM_CONTAINER
        WHERE is_current = TRUE
    """)
    rows = cur.fetchall()
    cur.close()
    conn.close()

    df = pd.DataFrame(rows, columns=["container_sk", "container_bk", "zone_bk"])
    logger.info(f"⚡ {len(df)} conteneurs chargés.")
    return df

# ============================================
# CHARGEMENT MAPPING ZONE
# ============================================

def load_zone_mapping():
    conn = get_connection()
    cur = conn.cursor()
    cur.execute("""
        SELECT zone_sk, zone_bk
        FROM DW.DIM_ZONE
    """)
    rows = cur.fetchall()
    cur.close()
    conn.close()

    df = pd.DataFrame(rows, columns=["zone_sk", "zone_bk"])
    logger.info(f"⚡ {len(df)} zones chargées.")
    return df

# ============================================
# SIMULATION JOURNALIÈRE (CORRIGÉE)
# ============================================

def simulate_one_day(containers_df, zone_df, current_date):

    # 🔥 On force minuit strict
    current_date = datetime(
        current_date.year,
        current_date.month,
        current_date.day,
        0, 0, 0
    )

    logger.info(f"📅 Simulation du jour {current_date.date()}")

    df = containers_df.merge(zone_df, on="zone_bk", how="left")

    # 🔥 Génération timestamps STRICTEMENT dans la journée
    seconds_offsets = np.random.randint(0, 86400, MEASUREMENTS_PER_DAY)
    timestamps = current_date + pd.to_timedelta(seconds_offsets, unit="s")

    container_choices = np.random.choice(df["container_sk"], size=MEASUREMENTS_PER_DAY)
    zone_map = dict(zip(df["container_sk"], df["zone_sk"]))
    zone_choices = [zone_map[c] for c in container_choices]

    df_measurements = pd.DataFrame({
        "measurement_timestamp": timestamps,
        "time_sk": int(current_date.strftime("%Y%m%d")),
        "container_sk": container_choices,
        "zone_sk": zone_choices,
        "taux_remplissage_pct": np.random.uniform(0, 100, MEASUREMENTS_PER_DAY).round(2),
        "volume_litres": np.random.uniform(50, 2000, MEASUREMENTS_PER_DAY).round(2),
        "temperature_c": np.random.uniform(-10, 40, MEASUREMENTS_PER_DAY).round(2),
        "batterie_pct": np.random.randint(5, 100, MEASUREMENTS_PER_DAY),
        "poids_estime_kg": np.random.uniform(10, 500, MEASUREMENTS_PER_DAY).round(2),
    })

    df_measurements["is_overflow"] = (
        df_measurements["taux_remplissage_pct"] > 90
    ).astype(int)

    return df_measurements

# ============================================
# INSERTION BATCH
# ============================================

def insert_measurements(df_measurements):

    conn = get_connection()
    cur = conn.cursor()

    records = df_measurements[[
        "measurement_timestamp",
        "time_sk",
        "container_sk",
        "zone_sk",
        "taux_remplissage_pct",
        "volume_litres",
        "temperature_c",
        "batterie_pct",
        "poids_estime_kg",
        "is_overflow"
    ]].values.tolist()

    execute_values(
        cur,
        """
        INSERT INTO DW.FACT_MEASUREMENT (
            measurement_timestamp,
            time_sk,
            container_sk,
            zone_sk,
            taux_remplissage_pct,
            volume_litres,
            temperature_c,
            batterie_pct,
            poids_estime_kg,
            is_overflow
        ) VALUES %s
        """,
        records,
        page_size=BATCH_SIZE
    )

    conn.commit()
    cur.close()
    conn.close()

    logger.info(f"✅ {len(df_measurements)} mesures insérées.")

# ============================================
# MAIN BATCH HISTORIQUE
# ============================================

def simulate_historical_batch():

    containers_df = load_active_containers()
    zone_df = load_zone_mapping()

    # 🔥 On force le start_date à minuit
    today = datetime.now()
    today_midnight = datetime(today.year, today.month, today.day)

    start_date = today_midnight - timedelta(days=DAYS_TO_GENERATE)

    total_inserted = 0

    for day in range(DAYS_TO_GENERATE):

        current_date = start_date + timedelta(days=day)

        df_measurements = simulate_one_day(
            containers_df,
            zone_df,
            current_date
        )

        insert_measurements(df_measurements)

        total_inserted += len(df_measurements)

        logger.info(f"📊 Total cumulé : {total_inserted} lignes")

    logger.info("🎯 Simulation historique terminée.")

# ============================================
# EXECUTION
# ============================================

if __name__ == "__main__":
    simulate_historical_batch()
