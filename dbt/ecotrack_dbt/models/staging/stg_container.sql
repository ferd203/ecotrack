{{ config(materialized='view') }}

select
    container_sk,
    container_bk,
    container_type,
    capacity_l,
    zone_bk,
    latitude,
    longitude,
    status,
    date_debut,
    date_fin,
    version,
    is_current
from {{ source('raw', 'dim_container_raw') }}