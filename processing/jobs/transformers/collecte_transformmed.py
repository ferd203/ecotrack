import pandas as pd
import numpy as np
from datetime import datetime, timedelta
import logging
import os
from dotenv import load_dotenv
from sqlalchemy import create_engine
from psycopg2.extras import execute_values

# ============================================================
# PARAMETRES
# ============================================================

MEASUREMENTS_PER_DAY = 50000
BATCH_SIZE = 10000
SEED = 42

np.random.seed(SEED)

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger(__name__)

# densité réaliste kg / litre selon type de déchet
WASTE_DENSITY = {
    1: 0.05,   # plastique
    2: 0.09,   # papier
    3: 0.35,   # organique
    4: 0.60    # verre
}

# ============================================================
# CONNEXION
# ============================================================

def get_engine():
    load_dotenv()

    user = os.getenv("POSTGRES_USER")
    pwd = os.getenv("POSTGRES_PASSWORD")
    host = os.getenv("POSTGRES_HOST")
    port = os.getenv("POSTGRES_PORT", "5432")
    db = os.getenv("POSTGRES_DB")

    return create_engine(
        f"postgresql+psycopg2://{user}:{pwd}@{host}:{port}/{db}"
    )

# ============================================================
# CHARGEMENT DONNEES
# ============================================================

def load_containers():
    engine = get_engine()

    query = """
    SELECT
        container_sk,
        zone_bk,
        capacity_l
    FROM raw.dim_container_raw
    WHERE is_current = TRUE
    """

    df = pd.read_sql(query, engine)
    df["container_sk"] = df["container_sk"].astype(int)
    df["capacity_l"] = pd.to_numeric(df["capacity_l"], errors="coerce").fillna(1000)
    return df


def load_zones():
    engine = get_engine()

    query = """
    SELECT
        zone_sk,
        zone_bk
    FROM raw.dim_zone_raw
    """

    df = pd.read_sql(query, engine)
    df["zone_sk"] = df["zone_sk"].astype(int)
    return df


def load_ids(table, col, where=""):
    engine = get_engine()
    query = f"SELECT {col} FROM raw.{table} {where}"
    df = pd.read_sql(query, engine)
    return df[col].tolist()


def load_visits():
    """
    Clé :
    (container_sk, service_date) -> [secondes de visite dans la journée]
    """
    engine = get_engine()

    df = pd.read_sql("""
    SELECT
        container_sk,
        service_date,
        visit_datetime
    FROM raw.tournee_conteneur_raw
    """, engine)

    df["visit_datetime"] = pd.to_datetime(df["visit_datetime"], errors="coerce")
    df["service_date"] = pd.to_datetime(df["service_date"], errors="coerce").dt.date
    df = df.dropna(subset=["container_sk", "service_date", "visit_datetime"]).copy()
    df["container_sk"] = df["container_sk"].astype(int)

    visits = {}

    for (container_sk, service_date), g in df.groupby(["container_sk", "service_date"]):
        secs = []
        for ts in g["visit_datetime"].sort_values():
            midnight = ts.floor("D")
            sec = int((ts - midnight).total_seconds())
            secs.append(sec)
        visits[(int(container_sk), service_date)] = secs

    logger.info(f"Visites chargées : {len(visits)} couples (container_sk, service_date)")
    return visits

# ============================================================
# MAPPINGS STABLES PAR CONTENEUR
# ============================================================

def build_container_waste_map(containers, waste_ids):
    mapping = {}
    for c in containers["container_sk"]:
        mapping[int(c)] = int(np.random.choice(waste_ids))
    return mapping


def build_container_fill_profile_map(containers):
    mapping = {}

    for c in containers["container_sk"]:
        mapping[int(c)] = np.random.choice(
            ["slow", "normal", "fast"],
            p=[0.50, 0.40, 0.10]
        )

    return mapping


def build_container_device_map(containers, device_ids):
    """
    Un device fixe par conteneur.
    Répartition stable et cyclique.
    """
    if not device_ids:
        raise ValueError("Aucun device disponible.")

    mapping = {}
    shuffled_devices = device_ids.copy()
    np.random.shuffle(shuffled_devices)

    for i, c in enumerate(containers["container_sk"].tolist()):
        mapping[int(c)] = int(shuffled_devices[i % len(shuffled_devices)])

    return mapping

# ============================================================
# ETATS INITIAUX
# ============================================================

def init_states(n):

    fill = np.random.uniform(5, 20, n)

    battery = []

    for _ in range(n):

        r = np.random.random()

        if r < 0.60:
            battery.append(np.random.uniform(80,100))

        elif r < 0.80:
            battery.append(np.random.uniform(60,80))

        elif r < 0.87:
            battery.append(np.random.uniform(40,60))

        elif r < 0.94:
            battery.append(np.random.uniform(20,40))

        else:
            battery.append(np.random.uniform(5,20))

    battery = np.array(battery)

    temp = np.random.uniform(10, 25, n)

    return fill, battery, temp

# ============================================================
# SIMULATION CONTENEUR / JOUR
# ============================================================

def simulate_container_day(
    timestamps,
    visit_seconds,
    start_fill,
    start_battery,
    start_temp,
    capacity,
    waste_type,
    fill_profile
):
    fill = float(start_fill)
    battery = float(start_battery)
    temp = float(start_temp)

    last_time = 0
    density = WASTE_DENSITY.get(waste_type, 0.15)

    # profil de remplissage stable par conteneur
    if fill_profile == "slow":
        fill_rate_per_sec = np.random.uniform(0.00005, 0.00010)
    elif fill_profile == "normal":
        fill_rate_per_sec = np.random.uniform(0.00010, 0.00022)
    else:
        fill_rate_per_sec = np.random.uniform(0.00022, 0.00045)

    levels = []
    volumes = []
    weights = []
    temps = []
    batteries = []

    visit_idx = 0

    for t in timestamps:
        elapsed = t - last_time
        if elapsed < 0:
            elapsed = 0

        # montée progressive
        fill += elapsed * fill_rate_per_sec

        # chute uniquement si visite du jour atteinte
        while visit_idx < len(visit_seconds) and t >= visit_seconds[visit_idx]:
            fill = np.random.uniform(3, 10)
            visit_idx += 1

        fill = min(fill, 105)

        # bruit léger
        fill_measured = fill + np.random.normal(0, 0.25)
        fill_measured = np.clip(fill_measured, 0, 105)

        volume = (fill_measured / 100.0) * capacity
        volume += np.random.normal(0, max(1.0, capacity * 0.005))
        volume = max(volume, 0)

        weight = volume * density
        weight += np.random.normal(0, 0.8)
        weight = max(weight, 0)

        temp += np.random.normal(0, 0.02)

        battery -= np.random.uniform(0.001, 0.005)
        battery = max(battery, 5)

        levels.append(fill_measured)
        volumes.append(volume)
        weights.append(weight)
        temps.append(temp)
        batteries.append(battery)

        last_time = t

    return (
        np.array(levels),
        np.array(volumes),
        np.array(weights),
        np.array(temps),
        np.array(batteries),
        fill,
        battery,
        temp
    )

# ============================================================
# SIMULATION JOUR
# ============================================================

def simulate_day(
    containers,
    states,
    batteries,
    temps,
    visits,
    waste_ids,
    container_waste_map,
    container_fill_profile_map,
    container_device_map,
    date
):
    rows = []

    counts = np.random.multinomial(
        MEASUREMENTS_PER_DAY,
        np.ones(len(containers)) / len(containers)
    )

    for i, m in enumerate(counts):
        if m == 0:
            continue

        row = containers.iloc[i]

        container_sk = int(row.container_sk)
        zone_sk = int(row.zone_sk)
        capacity = float(row.capacity_l)

        waste_type = container_waste_map[container_sk]
        fill_profile = container_fill_profile_map[container_sk]
        device_sk = container_device_map[container_sk]

        random_timestamps = np.random.randint(0, 86400, m)

        visit_seconds = visits.get((container_sk, date), [])

        all_timestamps = np.unique(
            np.concatenate([random_timestamps, np.array(visit_seconds, dtype=int)])
        )
        all_timestamps.sort()

        fill, vol, kg, temp_vals, batt_vals, new_fill, new_batt, new_temp = simulate_container_day(
            timestamps=all_timestamps,
            visit_seconds=visit_seconds,
            start_fill=states[i],
            start_battery=batteries[i],
            start_temp=temps[i],
            capacity=capacity,
            waste_type=waste_type,
            fill_profile=fill_profile
        )

        states[i] = new_fill
        batteries[i] = new_batt
        temps[i] = new_temp

        ts_real = [
            datetime(date.year, date.month, date.day) + timedelta(seconds=int(s))
            for s in all_timestamps
        ]

        df = pd.DataFrame({
            "measurement_timestamp": ts_real,
            "time_sk": int(date.strftime("%Y%m%d")),
            "container_sk": container_sk,
            "zone_sk": zone_sk,
            "taux_remplissage_pct": fill,
            "volume_litres": vol,
            "temperature_c": temp_vals,
            "batterie_pct": batt_vals,
            "poids_estime_kg": kg,
            "is_overflow": (fill >= 90).astype(int),
            "waste_type_sk": waste_type,
            "device_sk": device_sk
        })

        rows.append(df)

    df_out = pd.concat(rows, ignore_index=True)

    logger.info(
        f"{date} avg_fill={df_out['taux_remplissage_pct'].mean():.2f} "
        f"max_fill={df_out['taux_remplissage_pct'].max():.2f} "
        f"overflow_rate={(df_out['is_overflow'].mean()*100):.2f}%"
    )

    return df_out, states, batteries, temps

# ============================================================
# INSERTION
# ============================================================

def insert_measurements(df):
    engine = get_engine()
    conn = engine.raw_connection()
    cur = conn.cursor()

    cols = list(df.columns)
    values = [tuple(x) for x in df.to_numpy()]

    execute_values(
        cur,
        f"INSERT INTO raw.mesures_raw ({','.join(cols)}) VALUES %s",
        values,
        page_size=BATCH_SIZE
    )

    conn.commit()
    cur.close()
    conn.close()

# ============================================================
# MAIN
# ============================================================

def simulate():
    containers = load_containers()
    zones = load_zones()

    containers = containers.merge(zones, on="zone_bk", how="left")

    waste_ids = load_ids("dim_waste_type_raw", "id")
    device_ids = load_ids("dim_device_raw", "id", "WHERE is_active = TRUE")

    visits = load_visits()
    container_waste_map = build_container_waste_map(containers, waste_ids)
    container_fill_profile_map = build_container_fill_profile_map(containers)
    container_device_map = build_container_device_map(containers, device_ids)

    fill, battery, temp = init_states(len(containers))

    total = 0

    start = datetime(2025, 4, 1)
    end = datetime(2026, 4, 19)

    for d in pd.date_range(start, end):
        df, fill, battery, temp = simulate_day(
            containers=containers,
            states=fill,
            batteries=battery,
            temps=temp,
            visits=visits,
            waste_ids=waste_ids,
            container_waste_map=container_waste_map,
            container_fill_profile_map=container_fill_profile_map,
            container_device_map=container_device_map,
            date=d.date()
        )

        insert_measurements(df)

        total += len(df)
        logger.info(f"{d.date()} total rows {total}")

    logger.info("Simulation terminée")


if __name__ == "__main__":
    simulate()