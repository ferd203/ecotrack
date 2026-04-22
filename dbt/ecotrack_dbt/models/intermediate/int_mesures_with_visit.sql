{{ config(materialized='view') }}

with mesures as (

    select *
    from {{ ref('stg_mesures') }}

),

visites as (

    select *
    from {{ ref('stg_tournee_conteneur') }}

),

joined as (

    select
        m.measurement_timestamp,
        m.time_sk,
        m.container_sk,
        m.zone_sk,
        m.device_sk,
        m.waste_type_sk,
        m.taux_remplissage_pct,
        m.volume_litres,
        m.temperature_c,
        m.batterie_pct,
        m.poids_estime_kg,
        m.is_overflow,

        v.route_sk,
        v.route_bk,
        v.tour_instance_bk,
        v.visit_datetime,
        v.visit_order,

        row_number() over (
            partition by m.container_sk, m.measurement_timestamp
            order by abs(extract(epoch from (m.measurement_timestamp - v.visit_datetime)))
        ) as rn

    from mesures m
    left join visites v
        on m.container_sk = v.container_sk
       and date(m.measurement_timestamp) = v.service_date

)

select
    measurement_timestamp,
    time_sk,
    container_sk,
    zone_sk,
    route_sk,
    route_bk,
    tour_instance_bk,
    visit_datetime,
    visit_order,
    device_sk,
    waste_type_sk,
    taux_remplissage_pct,
    volume_litres,
    temperature_c,
    batterie_pct,
    poids_estime_kg,
    is_overflow
from joined
where rn = 1