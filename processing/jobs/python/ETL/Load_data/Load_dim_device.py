# ============================================
# LOAD DIM_DEVICE
# ============================================

import sys
import os
import pandas as pd
import logging
from psycopg2.extras import execute_batch

# ------------------------------------------------
# 1. CONFIGURATION DU PYTHONPATH
# ------------------------------------------------
PROJECT_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), "../.."))
sys.path.append(PROJECT_ROOT)

# ------------------------------------------------
# 2. IMPORT DB
# ------------------------------------------------
from utils.db import get_connection

# ------------------------------------------------
# 3. LOGGING
# ------------------------------------------------
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger(__name__)

# ------------------------------------------------
# 4. CONSTANTES
# ------------------------------------------------
CSV_PATH =  "csv_files/DIM_DEVICE_TRANSFORMED.csv"
TABLE_NAME = "dim_device"

# ------------------------------------------------
# 5. LOAD FUNCTION
# ------------------------------------------------
def load_dim_device():
    logger.info("🚀 Début du chargement de DIM_DEVICE")

    # --- Lecture du CSV
    df = pd.read_csv(CSV_PATH, sep=",", encoding="utf-8")
    logger.info(f"{len(df)} lignes lues depuis {CSV_PATH}")

    # --- Colonnes réellement utiles (alignées DB)
    required_columns = [
        "device_bk",
        "model",
        "installation_date",
        "is_active"
    ]

    # --- Vérification
    missing_cols = [c for c in required_columns if c not in df.columns]
    if missing_cols:
        logger.error(f"Colonnes manquantes dans le CSV : {missing_cols}")
        logger.error(f"Colonnes trouvées : {list(df.columns)}")
        return

    # --- Connexion DB
    conn = get_connection()
    cur = conn.cursor()

    # --- SQL INSERT
    insert_sql = """
        INSERT INTO dim_device (
            device_bk,
            model,
            installation_date,
            is_active
        )
        VALUES (
            %(device_bk)s,
            %(model)s,
            %(installation_date)s,
            %(is_active)s
        )
        ON CONFLICT (device_sk) DO NOTHING;
    """

    # --- Insert batch (sans limite imposée)
    records = df[required_columns].to_dict(orient="records")
    execute_batch(cur, insert_sql, records)
    conn.commit()

    cur.close()
    conn.close()

    logger.info(f"✅ Chargement DIM_DEVICE terminé : {len(records)} lignes insérées")

# ------------------------------------------------
# 6. MAIN
# ------------------------------------------------
if __name__ == "__main__":
    try:
        load_dim_device()
    except Exception:
        logger.error("❌ Erreur lors du chargement de DIM_DEVICE", exc_info=True)