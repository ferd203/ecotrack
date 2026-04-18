# ETL/Load_data/Load_dim_container.py

import pandas as pd
import psycopg2
import logging
from utils.db import get_connection

# ============================================
# CONFIGURATION LOGGING
# ============================================
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger(__name__)

# ============================================
# LOAD FUNCTION (SCD2)
# ============================================
def load_dim_container(input_path="csv_files/DIM_CONTAINER_TRANSFORMED.csv"):

    try:
        logger.info("🚀 Début chargement DIM_CONTAINER (SCD2)")

        df = pd.read_csv(input_path)

        conn = get_connection()
        cur = conn.cursor()

        for _, row in df.iterrows():

            # 1️⃣ Vérifier si container existe (version active)
            cur.execute("""
                SELECT container_sk,
                       container_type,
                       capacity_l,
                       zone_bk,
                       status,
                       latitude,
                       longitude,
                       version
                FROM DW.DIM_CONTAINER
                WHERE container_bk = %s
                AND is_current = TRUE
            """, (row["container_bk"],))

            existing = cur.fetchone()

            # ==================================================
            # CAS 1 : Nouveau container (jamais existé)
            # ==================================================
            if existing is None:

                # Générer nouveau container_sk via sequence
                cur.execute("SELECT nextval('dw.dim_container_sk_seq')")
                new_sk = cur.fetchone()[0]

                cur.execute("""
                    INSERT INTO DW.DIM_CONTAINER (
                        container_sk,
                        container_bk,
                        container_type,
                        capacity_l,
                        zone_bk,
                        status,
                        latitude,
                        longitude,
                        geom,
                        date_debut,
                        date_fin,
                        version,
                        is_current
                    )
                    VALUES (
                        %s,%s,%s,%s,%s,%s,%s,%s,
                        public.ST_SetSRID(
                            public.ST_MakePoint(%s,%s),
                            4326
                        ),
                        %s,NULL,1,TRUE
                    )
                """, (
                    new_sk,
                    row["container_bk"],
                    row["container_type"],
                    row["capacity_l"],
                    row["zone_bk"],
                    row["status"],
                    row["latitude"],
                    row["longitude"],
                    row["longitude"],  # X
                    row["latitude"],   # Y
                    row["date_debut"]
                ))

            # ==================================================
            # CAS 2 : Container existant → vérifier si changement
            # ==================================================
            else:
                (
                    container_sk,
                    old_type,
                    old_capacity,
                    old_zone,
                    old_status,
                    old_lat,
                    old_long,
                    old_version
                ) = existing

                # Si quelque chose a changé, créer nouvelle version
                if (
                    old_type != row["container_type"] or
                    old_capacity != row["capacity_l"] or
                    old_zone != row["zone_bk"] or
                    old_status != row["status"] or
                    float(old_lat) != float(row["latitude"]) or
                    float(old_long) != float(row["longitude"])
                ):

                    # 🔴 Fermer ancienne version
                    cur.execute("""
                        UPDATE DW.DIM_CONTAINER
                        SET is_current = FALSE,
                            date_fin = CURRENT_DATE,
                            status = 'INACTIF'
                        WHERE container_sk = %s
                    """, (container_sk,))

                    # Générer nouveau container_sk
                    cur.execute("SELECT nextval('dw.dim_container_sk_seq')")
                    new_sk = cur.fetchone()[0]

                    # 🟢 Insérer nouvelle version
                    cur.execute("""
                        INSERT INTO DW.DIM_CONTAINER (
                            container_sk,
                            container_bk,
                            container_type,
                            capacity_l,
                            zone_bk,
                            status,
                            latitude,
                            longitude,
                            geom,
                            date_debut,
                            date_fin,
                            version,
                            is_current
                        )
                        VALUES (
                            %s,%s,%s,%s,%s,%s,%s,%s,
                            public.ST_SetSRID(
                                public.ST_MakePoint(%s,%s),
                                4326
                            ),
                            CURRENT_DATE,
                            NULL,
                            %s,
                            TRUE
                        )
                    """, (
                        new_sk,
                        row["container_bk"],
                        row["container_type"],
                        row["capacity_l"],
                        row["zone_bk"],
                        row["status"],
                        row["latitude"],
                        row["longitude"],
                        row["longitude"],
                        row["latitude"],
                        old_version + 1
                    ))

                # Sinon rien à faire, la version active reste

        conn.commit()
        cur.close()
        conn.close()

        logger.info("✅ Chargement DIM_CONTAINER terminé avec succès (SCD2)")

    except Exception as e:
        logger.error(f"❌ Erreur chargement DIM_CONTAINER : {e}")
        if conn:
            conn.rollback()


# ============================================
# MAIN
# ============================================
if __name__ == "__main__":
    load_dim_container()