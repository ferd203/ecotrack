{{ config(materialized='view') }}

SELECT
    zone_sk,
    zone_bk AS zone_id,
    zone_name,
    city,
    population,
    longitude AS center_longitude,
    latitude AS center_latitude,
    geom AS geom_centroid
FROM dw.dim_zone