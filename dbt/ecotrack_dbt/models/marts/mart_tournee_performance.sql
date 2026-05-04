{{ config(
    materialized='table',
    schema='mart',
    post_hook=[
        "create index if not exists idx_mart_tournee_route on {{ this }}(route_sk)",
        "create index if not exists idx_mart_tournee_date on {{ this }}(date_bk)"
    ]
) }}

with collecte as (

    select
        fc.route_sk,
        fc.container_sk,
        fc.zone_sk,
        fc.agent_sk,
        fc.visit_datetime,
        fc.volume_collecte_litres,
        fc.poids_collecte_kg,
        fc.taux_remplissage_pct
    from {{ ref('fact_collecte') }} fc

),

route_dim as (

    select
        route_sk,
        route_bk,
        route_date,
        planned_distance_m,
        actual_distance_m,
        planned_duration_min,
        actual_duration_min
    from {{ ref('dim_route') }}

),

zone_dim as (

    select
        zone_sk,
        zone_name,
        city
    from {{ ref('dim_zone') }}

),

agg as (

    select
        r.route_sk,
        r.route_bk,
        r.route_date as date_bk,
        z.city,
        c.agent_sk,
        c.zone_sk,

        count(distinct c.container_sk) as nb_conteneurs_visites,

        sum(c.volume_collecte_litres) as volume_total_litres,
        sum(c.poids_collecte_kg) as poids_total_kg,

        round(avg(c.taux_remplissage_pct)::numeric, 2) as taux_moyen_collecte,

        r.planned_distance_m,
        r.actual_distance_m,
        r.planned_duration_min,
        r.actual_duration_min

    from collecte c
    left join route_dim r
        on c.route_sk = r.route_sk
    left join zone_dim z
        on c.zone_sk = z.zone_sk

    group by
        r.route_sk,
        r.route_bk,
        r.route_date,
        z.city,
        c.agent_sk,
        c.zone_sk,
        r.planned_distance_m,
        r.actual_distance_m,
        r.planned_duration_min,
        r.actual_duration_min

),

final as (

    select
        *,
        case
            when taux_moyen_collecte >= 80 then 'Très efficace'
            when taux_moyen_collecte >= 60 then 'Efficace'
            when taux_moyen_collecte >= 40 then 'Moyen'
            else 'Non efficace'
        end as performance_tournee

    from agg

)

select *
from final
order by date_bk desc