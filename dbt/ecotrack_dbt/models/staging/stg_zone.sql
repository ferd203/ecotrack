{{ config(materialized='view') }}

select

    zone_sk,
    zone_bk,
    zone_name,
    city,
    population,
    area_km2,

    latitude,
    longitude

from {{ source('raw','dim_zone_raw') }}