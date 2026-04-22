{{ config(
    materialized='table',
    schema='mart',
    post_hook=[
        "create index if not exists idx_mart_battery_date on {{ this }}(date_bk)",
        "create index if not exists idx_mart_battery_zone on {{ this }}(zone_sk)",
        "create index if not exists idx_mart_battery_date_zone on {{ this }}(date_bk, zone_sk)"
    ]
) }}

with mesures as (

    select
        fm.time_sk,
        fm.zone_sk,
        fm.container_sk,
        fm.device_sk,
        fm.batterie_pct
    from {{ ref('fact_measurement') }} fm
    where fm.batterie_pct is not null

),

time_dim as (

    select
        time_sk,
        date_bk,
        month,
        month_name,
        year
    from {{ ref('dim_time') }}

),

zone_dim as (

    select
        zone_sk,
        zone_bk,
        zone_name,
        city
    from {{ ref('dim_zone') }}

),

agg as (

    select
        t.date_bk,
        t.month,
        t.month_name,
        t.year,
        z.zone_sk,
        z.zone_bk,
        z.zone_name,
        z.city,

        count(*) as nb_mesures,
        count(distinct m.device_sk) as nb_devices,
        round(avg(m.batterie_pct)::numeric, 2) as batterie_moyenne_pct,

        sum(case when m.batterie_pct < 20 then 1 else 0 end) as nb_batteries_critiques,
        sum(case when m.batterie_pct < 50 then 1 else 0 end) as nb_batteries_faibles,

        round(
            (
                100.0 * sum(case when m.batterie_pct < 50 then 1 else 0 end)
                / nullif(count(*), 0)
            )::numeric,
            2
        ) as pct_batteries_faibles

    from mesures m
    inner join time_dim t
        on m.time_sk = t.time_sk
    inner join zone_dim z
        on m.zone_sk = z.zone_sk

    group by
        t.date_bk,
        t.month,
        t.month_name,
        t.year,
        z.zone_sk,
        z.zone_bk,
        z.zone_name,
        z.city

),

final as (

    select
        *,
        case
            when batterie_moyenne_pct < 20 then 'Critique'
            when batterie_moyenne_pct < 50 then 'À surveiller'
            else 'Normal'
        end as statut_batterie_zone
    from agg

)

select *
from final
order by date_bk, zone_sk