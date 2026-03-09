# utils/db.py
import psycopg2
from utils.config import DB_CONFIG


def get_connection():
    conn = psycopg2.connect(
        host=DB_CONFIG["host"],
        port=DB_CONFIG["port"],
        database=DB_CONFIG["database"],
        user=DB_CONFIG["user"],
        password=DB_CONFIG["password"],
        options=f"-c search_path={DB_CONFIG['schema']}",
        client_encoding="utf-8"
    )
    return conn


def execute_sql(sql, params=None):
    conn = get_connection()
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
        conn = get_connection()
        print("Connexion réussie à la base de données.")
        conn.close()
    except Exception as e:
        print(f"Erreur de connexion : {e}")
