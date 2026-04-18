# ============================================
# LOAD DIM_AGENT
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
CSV_PATH = "csv_files/DIM_AGENT_TRANSFORMED.csv"
TABLE_NAME = "dim_agent"

# ------------------------------------------------
# 5. LOAD FUNCTION
# ------------------------------------------------
def Load_dim_agent():
    logger.info("🚀 Début du chargement de DIM_AGENT")

    # --- Lecture du CSV
    df = pd.read_csv(CSV_PATH, sep=",", encoding="utf-8")
    logger.info(f"{len(df)} lignes lues depuis {CSV_PATH}")

    # --- Colonnes réellement utiles (alignées DB)
    required_columns = [
        "agent_bk",
        "firstname",
        "lastname",
        "role",
        "is_active",
        "etl_date"
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
        INSERT INTO dim_agent (
            agent_bk,
            firstname,
            lastname,
            role,
            is_active,
            etl_date
        )
        VALUES (
            %(agent_bk)s,
            %(firstname)s,
            %(lastname)s,
            %(role)s,
            %(is_active)s,
            %(etl_date)s
        )
        ON CONFLICT (agent_sk) DO NOTHING;
    """

    # --- Insert batch (sans limite imposée)
    records = df[required_columns].to_dict(orient="records")
    execute_batch(cur, insert_sql, records)
    conn.commit()

    cur.close()
    conn.close()

    logger.info(f"✅ Chargement DIM_AGENT terminé : {len(records)} lignes insérées")

# ------------------------------------------------
# 6. MAIN
# ------------------------------------------------
if __name__ == "__main__":
    try:
        Load_dim_agent()
    except Exception:
        logger.error("❌ Erreur lors du chargement de DIM_AGENT", exc_info=True)