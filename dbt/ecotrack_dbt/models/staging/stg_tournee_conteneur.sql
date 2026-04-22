{{ config(materialized='view') }}

with tc as (

    select
        id,
        tour_instance_bk,
        service_date,
        route_sk,
        route_bk,
        zone_bk,
        city,
        container_sk,
        container_bk,
        visit_order,
        visit_datetime,
        latitude,
        longitude,
        container_type,
        capacity_l,
        etl_date
    from {{ source('raw', 'tournee_conteneur_raw') }}
    where visit_datetime is not null
      and container_sk is not null
      and route_sk is not null

),

t as (

    select
        tour_instance_bk,
        planned_distance_m,
        actual_distance_m,
        planned_duration_min,
        actual_duration_min
    from {{ source('raw', 'tournee_raw') }}

)

select
    tc.id,
    tc.tour_instance_bk,
    tc.service_date,
    tc.route_sk,
    tc.route_bk,
    tc.zone_bk,
    tc.city,
    tc.container_sk,
    tc.container_bk,
    tc.visit_order,
    tc.visit_datetime,
    cast(tc.visit_datetime as date) as visit_date,
    tc.latitude,
    tc.longitude,
    tc.container_type,
    tc.capacity_l,
    cast(t.planned_distance_m as numeric(12,2)) as planned_distance_m,
    cast(t.actual_distance_m as numeric(12,2)) as actual_distance_m,
    t.planned_duration_min,
    t.actual_duration_min,
    tc.etl_date
from tc
left join t
    on tc.tour_instance_bk = t.tour_instance_bk