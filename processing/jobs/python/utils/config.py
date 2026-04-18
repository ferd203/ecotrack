# utils/config.py
import os

DB_CONFIG = {
    "host": os.getenv("DB_HOST", "localhost"),
    "schema": os.getenv("DB_SCHEMA", "DW"),
    "port": os.getenv("DB_PORT", "5432"),
    "database": os.getenv("DB_NAME", "ecotrack_projet"),  # ← correction ici !
    "user": os.getenv("DB_USER", "postgres"),
    "password": os.getenv("DB_PASSWORD", "1234")
}
