{{ config(materialized='view') }}

SELECT

    route_sk,
    route_bk AS route_id,
    route_date,
    planned_distance_m,
    planned_duration_min,
    path AS geom_path

FROM dw.dim_route