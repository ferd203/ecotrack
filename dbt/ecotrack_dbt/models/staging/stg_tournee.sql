{{ config(materialized='view') }}

select
    tour_instance_bk,
    service_date,
    route_sk,
    route_bk,
    zone_bk,
    zone_name,
    city,
    start_datetime,
    end_datetime,
    visited_container_count,
    zone_container_count,
    all_zone_containers_visited,
    planned_distance_m,
    actual_distance_m,
    planned_duration_min,
    actual_duration_min
from {{ source('raw','tournee_raw') }}