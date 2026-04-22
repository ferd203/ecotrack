{{ config(
    materialized='table',
    schema='mart',
    post_hook=[
        "create index if not exists idx_mart_map_containers_container on {{ this }}(container_sk)",
        "create index if not exists idx_mart_map_containers_zone on {{ this }}(zone_sk)",
        "create index if not exists idx_mart_map_containers_status on {{ this }}(statut_remplissage)"
    ]
) }}

with latest_measure as (

    select
        fm.container_sk,
        fm.zone_sk,
        fm.device_sk,
        fm.waste_type_sk,
        fm.measurement_timestamp,
        fm.taux_remplissage_pct,
        fm.volume_litres,
        fm.temperature_c,
        fm.batterie_pct,
        fm.poids_estime_kg,
        fm.is_overflow,
        row_number() over (
            partition by fm.container_sk
            order by fm.measurement_timestamp desc
        ) as rn
    from {{ ref('fact_measurement') }} fm

),

filtered_measure as (

    select *
    from latest_measure
    where rn = 1

),

container_dim as (

    select
        container_sk,
        container_bk,
        container_type,
        capacity_l,
        zone_bk,
        status,
        latitude,
        longitude,
        is_current
    from {{ ref('dim_container') }}
    where is_current = true

),

zone_dim as (

    select
        zone_sk,
        zone_bk,
        zone_name,
        city
    from {{ ref('dim_zone') }}

),

device_dim as (

    select
        device_sk,
        device_bk,
        model,
        is_active
    from {{ ref('dim_device') }}

),

waste_dim as (

    select
        waste_type_sk,
        waste_type_name
    from {{ ref('dim_waste_type') }}

)

select
    c.container_sk,
    c.container_bk,
    c.capacity_l,
    c.status as container_status,
    c.latitude,
    c.longitude,

    z.zone_sk,
    z.zone_bk,
    z.zone_name,
    z.city,

    d.device_sk,
    d.device_bk,
    d.model as device_model,
    d.is_active as device_is_active,

    w.waste_type_sk,
    w.waste_type_name,

    f.measurement_timestamp,
    f.taux_remplissage_pct,
    f.volume_litres,
    f.temperature_c,
    f.batterie_pct,
    f.poids_estime_kg,
    f.is_overflow,

    case
        when f.taux_remplissage_pct >= 90 then 'Débordement'
        when f.taux_remplissage_pct >= 70 then 'Critique'
        when f.taux_remplissage_pct >= 40 then 'Moyen'
        else 'Normal'
    end as statut_remplissage,

    case
        when f.batterie_pct < 20 then 'Batterie critique'
        when f.batterie_pct < 50 then 'Batterie faible'
        else 'Batterie normale'
    end as statut_batterie,

    case
        when f.temperature_c < 0 then 'Très basse'
        when f.temperature_c < 10 then 'Basse'
        when f.temperature_c <= 30 then 'Normale'
        when f.temperature_c <= 40 then 'Élevée'
        else 'Critique'
    end as statut_temperature

from filtered_measure f
left join container_dim c
    on f.container_sk = c.container_sk
left join zone_dim z
    on f.zone_sk = z.zone_sk
left join device_dim d
    on f.device_sk = d.device_sk
left join waste_dim w
    on f.waste_type_sk = w.waste_type_sk
where c.latitude is not null
  and c.longitude is not null