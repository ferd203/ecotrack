{{ config(
    materialized='table',
    post_hook=[
        "create index if not exists idx_fact_measurement_time on {{ this }}(time_sk)",
        "create index if not exists idx_fact_measurement_container on {{ this }}(container_sk)",
        "create index if not exists idx_fact_measurement_container_time on {{ this }}(container_sk, time_sk)",
        "create index if not exists idx_fact_measurement_zone on {{ this }}(zone_sk)",
        "create index if not exists idx_fact_measurement_waste_type on {{ this }}(waste_type_sk)",
        "create index if not exists idx_fact_measurement_device on {{ this }}(device_sk)",
        "create index if not exists idx_fact_measurement_overflow on {{ this }}(is_overflow)",
        "create index if not exists idx_fact_measurement_battery on {{ this }}(batterie_pct)",
        "create index if not exists idx_fact_measurement_fillrate on {{ this }}(taux_remplissage_pct)"
    ]
) }}

with mesures as (

    select *
    from {{ ref('stg_mesures') }}

),

dim_time as (

    select *
    from {{ ref('dim_time') }}

),

dim_container as (

    select *
    from {{ ref('dim_container') }}

),

dim_zone as (

    select *
    from {{ ref('dim_zone') }}

),

dim_waste_type as (

    select *
    from {{ ref('dim_waste_type') }}

),

dim_device as (

    select *
    from {{ ref('dim_device') }}

)

select
    m.time_sk,
    m.container_sk,
    m.zone_sk,
    m.waste_type_sk,
    cast(null as integer) as agent_sk,
    m.device_sk,
    cast(null as integer) as route_sk,

    m.measurement_timestamp,

    m.taux_remplissage_pct,
    m.volume_litres,
    m.temperature_c,
    m.batterie_pct,
    m.poids_estime_kg,
    m.is_overflow,
    cast(0 as smallint) as nb_signalements_actifs

from mesures m
inner join dim_time dt
    on m.time_sk = dt.time_sk
inner join dim_container dc
    on m.container_sk = dc.container_sk
inner join dim_zone dz
    on m.zone_sk = dz.zone_sk
left join dim_waste_type dwt
    on m.waste_type_sk = dwt.waste_type_sk
left join dim_device dd
    on m.device_sk = dd.device_sk