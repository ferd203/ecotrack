# ETL/Streaming/simulate_iot_batch.py

import pandas as pd
import numpy as np
from datetime import datetime, timedelta
import logging
from utils.db import get_connection
from psycopg2.extras import execute_values

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
# SIMULATION DES MESURES
# ============================================
def simulate_iot_measurements(containers_df, zone_df, num_measurements=500_000):
    logger.info(f"⏱ Simulation des {num_measurements} mesures à partir de {datetime.now()}...")

    # On merge pour récupérer zone_sk
    df = containers_df.merge(zone_df, on="zone_bk", how="left")

    # Génération aléatoire des mesures
    timestamps = [datetime.now() + timedelta(seconds=i) for i in range(num_measurements)]
    container_choices = np.random.choice(df["container_sk"], size=num_measurements)
    zone_choices = np.array([df.loc[df["container_sk"] == c, "zone_sk"].values[0] for c in container_choices])

    df_measurements = pd.DataFrame({
        "measurement_timestamp": timestamps,
        "time_sk": [int(dt.strftime("%Y%m%d")) for dt in timestamps],
        "container_sk": container_choices,
        "zone_sk": zone_choices,
        "taux_remplissage_pct": np.random.uniform(0, 100, num_measurements).round(2),
        "volume_litres": np.random.uniform(50, 2000, num_measurements).round(2),
        "temperature_c": np.random.uniform(-10, 40, num_measurements).round(2),
        "batterie_pct": np.random.randint(0, 100, num_measurements),
        "poids_estime_kg": np.random.uniform(10, 500, num_measurements).round(2),
        "is_overflow": np.random.randint(0, 2, num_measurements)
    })

    logger.info("⚡ Mesures simulées. Prêtes pour insertion en base...")
    return df_measurements

# ============================================
# INSERTION EN BASE (BATCH)
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
        page_size=10000
    )

    conn.commit()
    cur.close()
    conn.close()
    logger.info(f"✅ {len(df_measurements)} mesures insérées avec succès dans FACT_MEASUREMENT.")

# ============================================
# MAIN
# ============================================
def simulate_batch():
    containers_df = load_active_containers()
    zone_df = load_zone_mapping()
    df_measurements = simulate_iot_measurements(containers_df, zone_df)
    insert_measurements(df_measurements)

if __name__ == "__main__":
    simulate_batch()
