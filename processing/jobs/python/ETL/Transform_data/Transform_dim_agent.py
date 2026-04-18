import pandas as pd
import logging
import os

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s"
)

logger = logging.getLogger(__name__)


def transform_dim_agent(
    input_csv="csv_files/STG_DIM_AGENT.csv",
    output_csv="csv_files/DIM_AGENT_TRANSFORMED.csv"
):

    try:

        if not os.path.exists(input_csv):
            logger.error(f"Fichier introuvable : {input_csv}")
            return None

        df = pd.read_csv(input_csv)

        logger.info(f"{len(df)} lignes lues")

        df.columns = df.columns.str.strip()

        # ----------------------------------
        # SUPPRESSION SK SOURCE
        # ----------------------------------
        if "agent_sk" in df.columns:
            df.drop(columns=["agent_sk"], inplace=True)

        # ----------------------------------
        # SPLIT FULL NAME
        # ----------------------------------
        df[["firstname", "lastname"]] = df["full_name"].str.split(
            " ",
            n=1,
            expand=True
        )

        df.drop(columns=["full_name"], inplace=True)

        # ----------------------------------
        # NORMALISATION
        # ----------------------------------
        df["is_active"] = df["is_active"].astype(bool)

        # ----------------------------------
        # COLONNES ETL
        # ----------------------------------
        df["etl_date"] = pd.Timestamp.now()

        # ----------------------------------
        # ORDRE FINAL
        # ----------------------------------
        df = df[
            [
                "agent_bk",
                "firstname",
                "lastname",
                "role",
                "is_active",
                "etl_date"
            ]
        ]

        os.makedirs("csv_files", exist_ok=True)
        df.to_csv(output_csv, index=False)

        logger.info("✅ Transformation DIM_AGENT terminée")

        return df

    except Exception as e:
        logger.error(e)
        return None


if __name__ == "__main__":

    df = transform_dim_agent()

    if df is not None:
        print(df.head())