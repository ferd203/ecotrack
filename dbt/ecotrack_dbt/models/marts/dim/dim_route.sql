{{ config(
    materialized='table',
    post_hook=[
        "create index if not exists idx_dim_route_bk on {{ this }}(route_bk)",
        "create index if not exists idx_dim_route_date on {{ this }}(route_date)",
        "create index if not exists idx_dim_route_geom on {{ this }} using gist (geom)"
    ]
) }}

with route_points as (

    select
        route_sk,
        route_bk,
        service_date as route_date,
        visit_order,

        planned_distance_m,
        planned_duration_min,
        actual_distance_m,
        actual_duration_min,

        ST_SetSRID(
            ST_MakePoint(longitude, latitude),
            4326
        ) as point_geom

    from {{ ref('stg_tournee_conteneur') }}

    where latitude is not null
    and longitude is not null
    and route_sk is not null

),

route_lines as (

    select
        route_sk,
        route_bk,
        route_date,

        max(planned_distance_m) as planned_distance_m,
        max(planned_duration_min) as planned_duration_min,
        max(actual_distance_m) as actual_distance_m,
        max(actual_duration_min) as actual_duration_min,

        ST_MakeLine(point_geom order by visit_order) as geom

    from route_points

    group by
        route_sk,
        route_bk,
        route_date

)

select *
from route_lines