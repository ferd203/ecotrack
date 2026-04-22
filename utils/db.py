# utils/db.py
import psycopg2
import os

from dotenv import load_dotenv
from sqlalchemy import create_engine, text


def get_engine():
    load_dotenv()

    db_user = os.getenv("POSTGRES_USER")
    db_password = os.getenv("POSTGRES_PASSWORD")
    db_host = os.getenv("POSTGRES_HOST")
    db_port = os.getenv("POSTGRES_PORT", "5432")
    db_name = os.getenv("POSTGRES_DB")

    required_vars = [db_user, db_password, db_host, db_name]
    if not all(required_vars):
        raise ValueError("Variables PostgreSQL manquantes dans le fichier .env")

    return create_engine(
        f"postgresql://{db_user}:{db_password}@{db_host}:{db_port}/{db_name}"
    )


def execute_sql(sql, params=None):
    conn = get_engine()
    cur = conn.cursor()

    try:
        cur.execute(sql, params)
        conn.commit()
    finally:
        cur.close()
        conn.close()

        
__main__ = "__main__"
if __name__ == "__main__":
    # Test de la connexion
    try:
        conn = get_engine()
        print("Connexion réussie à la base de données.")
    except Exception as e:
        print(f"Erreur de connexion : {e}")