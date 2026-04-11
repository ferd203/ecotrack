{{ config(materialized='view') }}

SELECT
    container_sk,
    container_bk AS container_id,
    capacity_l,
    longitude, 
    latitude,
    location AS geom_location, -- Pour les calculs PostGIS dans dbt
    status,
    is_current
FROM dw.dim_container
WHERE is_current = true