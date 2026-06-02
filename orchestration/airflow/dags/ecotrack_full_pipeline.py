from airflow import DAG
from airflow.operators.bash import BashOperator
from datetime import datetime

DATA_PLATFORM_DIR = "/opt/data-platform"
DBT_PROJECT_DIR = "/usr/app/ecotrack_dbt"

def ingestion_cmd(script):
    return (
        f"cd {DATA_PLATFORM_DIR} && "
        f"docker compose run --rm ingestion "
        f"python processing/jobs/loaders/{script}"
    )

def dbt_cmd(command):
    return (
        f"cd {DATA_PLATFORM_DIR} && "
        f"docker compose run --rm dbt "
        f"bash -c 'cd {DBT_PROJECT_DIR} && {command}'"
    )

with DAG(
    dag_id="ecotrack_full_pipeline",
    start_date=datetime(2025, 1, 1),
    schedule="@daily",
    catchup=False,
    tags=["ecotrack", "ingestion", "dbt"],
) as dag:

    load_zone = BashOperator(task_id="load_zone", bash_command=ingestion_cmd("load_zone.py"))
    load_waste_type = BashOperator(task_id="load_waste_type", bash_command=ingestion_cmd("load_waste_type.py"))
    load_device = BashOperator(task_id="load_device", bash_command=ingestion_cmd("load_device.py"))
    load_agent = BashOperator(task_id="load_agent", bash_command=ingestion_cmd("load_agent.py"))

    load_container = BashOperator(task_id="load_container", bash_command=ingestion_cmd("load_container.py"))
    load_route = BashOperator(task_id="load_route", bash_command=ingestion_cmd("load_route.py"))

    load_tournee_raw = BashOperator(task_id="load_tournee_raw", bash_command=ingestion_cmd("load_tournee_raw.py"))
    load_tournee_agent_raw = BashOperator(task_id="load_tournee_agent_raw", bash_command=ingestion_cmd("load_tournee_agent_raw.py"))
    load_tournee_conteneur_raw = BashOperator(
        task_id="load_tournee_conteneur_raw",
        bash_command=ingestion_cmd("loard_tournee_conteneur_raw.py")
    )

    dbt_debug = BashOperator(task_id="dbt_debug", bash_command=dbt_cmd("dbt debug"))
    dbt_run = BashOperator(task_id="dbt_run", bash_command=dbt_cmd("dbt run"))
    dbt_test = BashOperator(task_id="dbt_test", bash_command=dbt_cmd("dbt test"))

    [load_zone, load_waste_type, load_device, load_agent] >> load_container
    load_container >> load_route
    load_route >> load_tournee_raw
    load_tournee_raw >> [load_tournee_agent_raw, load_tournee_conteneur_raw]
    [load_tournee_agent_raw, load_tournee_conteneur_raw] >> dbt_debug >> dbt_run >> dbt_test