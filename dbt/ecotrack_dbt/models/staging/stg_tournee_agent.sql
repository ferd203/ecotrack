{{ config(materialized='view') }}

select
    id,
    tour_instance_bk,
    service_date,
    zone_bk,
    route_bk,
    agent_bk,
    role_in_tour,
    start_datetime,
    end_datetime
from {{ source('raw', 'tournee_agent_raw') }}