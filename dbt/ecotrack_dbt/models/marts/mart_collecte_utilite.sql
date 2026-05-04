{{ config(
    materialized='table',
    schema='mart',
    post_hook=[
        "create index if not exists idx_mart_collecte_route on {{ this }}(route_sk)",
        "create index if not exists idx_mart_collecte_date on {{ this }}(date_bk)"
    ]
) }}

with collecte as (

    select
        fc.route_sk,
        fc.container_sk,
        fc.zone_sk,
        fc.agent_sk,
        fc.visit_datetime,
        fc.taux_remplissage_pct,
        fc.volume_collecte_litres,
        fc.poids_collecte_kg
    from {{ ref('fact_collecte') }} fc

),

zone_dim as (

    select
        zone_sk,
        zone_name,
        city
    from {{ ref('dim_zone') }}

),

classified as (

    select
        c.*,
        case
            when c.taux_remplissage_pct < 30 then 'Inutile'
            when c.taux_remplissage_pct < 60 then 'Moyenne'
            when c.taux_remplissage_pct < 90 then 'Utile'
            else 'Urgente'
        end as niveau_utilite
    from collecte c

),

agg as (

    select
        date_trunc('day', c.visit_datetime)::date as date_bk,
        c.route_sk,
        z.city,
        c.agent_sk,
        c.zone_sk,

        count(*) as nb_collectes,

        sum(case when c.niveau_utilite = 'Inutile' then 1 else 0 end) as nb_inutiles,
        sum(case when c.niveau_utilite = 'Moyenne' then 1 else 0 end) as nb_moyennes,
        sum(case when c.niveau_utilite = 'Utile' then 1 else 0 end) as nb_utiles,
        sum(case when c.niveau_utilite = 'Urgente' then 1 else 0 end) as nb_urgentes,

        round(avg(c.taux_remplissage_pct)::numeric, 2) as taux_moyen_collecte,
        sum(c.volume_collecte_litres) as volume_total_litres,
        sum(c.poids_collecte_kg) as poids_total_kg

    from classified c
    left join zone_dim z
        on c.zone_sk = z.zone_sk

    group by
        date_trunc('day', c.visit_datetime)::date,
        c.route_sk,
        z.city,
        c.agent_sk,
        c.zone_sk

)

select *
from agg
order by date_bk desc