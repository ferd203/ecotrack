{{ config(
    materialized='table',
    post_hook=[
        "create index if not exists idx_fact_collecte_time on {{ this }}(time_sk)",
        "create index if not exists idx_fact_collecte_container on {{ this }}(container_sk)",
        "create index if not exists idx_fact_collecte_route on {{ this }}(route_sk)",
        "create index if not exists idx_fact_collecte_zone on {{ this }}(zone_sk)",
        "create index if not exists idx_fact_collecte_agent on {{ this }}(agent_sk)",
        "create index if not exists idx_fact_collecte_visit_datetime on {{ this }}(visit_datetime)",
        "create index if not exists idx_fact_collecte_container_time on {{ this }}(container_sk, time_sk)"
    ]
) }}

with visites as (

    select
        tc.tour_instance_bk,
        tc.service_date,
        tc.route_sk,
        tc.route_bk,
        tc.zone_bk,
        tc.container_sk,
        tc.container_bk,
        tc.visit_order,
        tc.visit_datetime
    from {{ ref('stg_tournee_conteneur') }} tc

),

agents as (

    select
        ta.tour_instance_bk,
        ta.agent_bk
    from {{ ref('stg_tournee_agent') }} ta
    where upper(ta.role_in_tour) = 'DRIVER'

),

agent_dim as (

    select
        agent_sk,
        agent_bk
    from {{ ref('dim_agent') }}

),

visites_with_agent as (

    select
        v.tour_instance_bk,
        v.service_date,
        v.route_sk,
        v.route_bk,
        v.zone_bk,
        v.container_sk,
        v.container_bk,
        v.visit_order,
        v.visit_datetime,
        ad.agent_sk
    from visites v
    left join agents a
        on v.tour_instance_bk = a.tour_instance_bk
    left join agent_dim ad
        on a.agent_bk = ad.agent_bk

),

container_dim as (

    select
        container_sk,
        zone_bk
    from {{ ref('dim_container') }}
    where is_current = true

),

zone_dim as (

    select
        zone_sk,
        zone_bk
    from {{ ref('dim_zone') }}

),

mesures as (

    select
        id,
        measurement_timestamp,
        time_sk,
        container_sk,
        taux_remplissage_pct,
        volume_litres,
        poids_estime_kg,
        is_overflow
    from {{ ref('stg_mesures') }}

),

last_measure_timestamp as (

    select
        v.tour_instance_bk,
        v.container_sk,
        v.visit_datetime,
        max(m.measurement_timestamp) as last_measurement_timestamp
    from visites_with_agent v
    left join mesures m
        on m.container_sk = v.container_sk
       and cast(m.measurement_timestamp as date) = cast(v.visit_datetime as date)
       and m.measurement_timestamp < date_trunc('second', v.visit_datetime)
    group by
        v.tour_instance_bk,
        v.container_sk,
        v.visit_datetime

),

mesure_finale as (

    select
        l.tour_instance_bk,
        l.container_sk,
        l.visit_datetime,
        m.time_sk,
        m.taux_remplissage_pct,
        m.volume_litres,
        m.poids_estime_kg,
        m.is_overflow
    from last_measure_timestamp l
    left join mesures m
        on m.container_sk = l.container_sk
       and m.measurement_timestamp = l.last_measurement_timestamp

)

select
    mf.time_sk,
    v.route_sk,
    cd.container_sk,
    zd.zone_sk,
    v.agent_sk,
   
    v.visit_datetime,
    v.visit_order,
    v.tour_instance_bk,

    mf.taux_remplissage_pct,
    mf.volume_litres as volume_collecte_litres,
    mf.poids_estime_kg as poids_collecte_kg,
    mf.is_overflow

from visites_with_agent v
left join mesure_finale mf
    on v.tour_instance_bk = mf.tour_instance_bk
   and v.container_sk = mf.container_sk
   and v.visit_datetime = mf.visit_datetime
left join container_dim cd
    on v.container_sk = cd.container_sk
left join zone_dim zd
    on v.zone_bk = zd.zone_bk