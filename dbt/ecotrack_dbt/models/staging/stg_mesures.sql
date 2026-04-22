{{ config(materialized='view') }}

select
    id,
    measurement_timestamp,
    time_sk,
    container_sk,
    zone_sk,
    taux_remplissage_pct,
    volume_litres,
    temperature_c,
    batterie_pct,
    poids_estime_kg,
    is_overflow,
    waste_type_sk,
    device_sk
from {{ source('raw','mesures_raw') }}