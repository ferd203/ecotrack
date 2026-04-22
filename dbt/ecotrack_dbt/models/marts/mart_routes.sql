{{ config(
    materialized='table',
    schema='mart',
    post_hook=[
        "create index if not exists idx_mart_routes_route_sk on {{ this }}(route_sk)",
        "create index if not exists idx_mart_routes_route_date on {{ this }}(route_date)",
        "create index if not exists idx_mart_routes_route_bk on {{ this }}(route_bk)"
    ]
) }}

with routes as (

    select
        r.route_sk,
        r.route_bk,
        r.route_date,
        r.planned_distance_m,
        r.actual_distance_m,
        r.planned_duration_min,
        r.actual_duration_min,
        r.geom
    from {{ ref('dim_route') }} r

),

tournees as (

    select distinct
        route_sk,
        route_bk,
        zone_bk,
        zone_name,
        city
    from {{ ref('stg_tournee') }}

)

select
    r.route_sk,
    r.route_bk,
    r.route_date,

    t.zone_bk,
    t.zone_name,
    t.city,

    r.planned_distance_m,
    r.actual_distance_m,
    r.planned_duration_min,
    r.actual_duration_min,

    ST_AsText(r.geom) as route_wkt

from routes r
left join tournees t
    on r.route_sk = t.route_sk
   and r.route_bk = t.route_bk