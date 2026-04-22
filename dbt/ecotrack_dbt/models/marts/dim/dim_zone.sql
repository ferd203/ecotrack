{{ config(
    materialized='table',
    post_hook=[
      "create index if not exists idx_dim_zone_bk on {{ this }}(zone_bk)",
      "create index if not exists idx_dim_zone_city on {{ this }}(city)",
      "create index if not exists idx_dim_zone_geom on {{ this }} using gist (geom)"
    ]
) }}

select
    zone_sk,
    zone_bk,
    zone_name,
    city,
    population,
    area_km2,
    latitude,
    longitude,
    case
        when latitude is not null and longitude is not null
        then ST_SetSRID(ST_MakePoint(longitude, latitude), 4326)
        else null
    end as geom
from {{ ref('stg_zone') }}