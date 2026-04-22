{{ config(
    materialized='table',
    post_hook=[
        "create index if not exists idx_dim_container_bk on {{ this }}(container_bk)",
        "create index if not exists idx_dim_container_zone_bk on {{ this }}(zone_bk)",
        "create index if not exists idx_dim_container_current on {{ this }}(is_current)",
        "create index if not exists idx_dim_container_status on {{ this }}(status)",
        "create index if not exists idx_dim_container_geom on {{ this }} using gist (geom)"
    ]
) }}

select
    container_sk,
    container_bk,
    container_type,
    capacity_l,
    zone_bk,
    status,
    latitude,
    longitude,
    case
        when latitude is not null and longitude is not null
        then ST_SetSRID(ST_MakePoint(longitude, latitude), 4326)
        else null
    end as geom,
    date_debut,
    date_fin,
    version,
    is_current,
    current_timestamp as created_at
from {{ ref('stg_container') }}