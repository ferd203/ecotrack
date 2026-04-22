{{ config(
    materialized='table',
    post_hook=[
      "create index if not exists idx_dim_time_date_bk on {{ this }}(date_bk)"
    ]
) }}

with dates as (

    select distinct
        cast(measurement_timestamp as date) as date_bk
    from {{ ref('stg_mesures') }}
    where measurement_timestamp is not null

),

final as (

    select
        cast(to_char(date_bk, 'YYYYMMDD') as int) as time_sk,
        date_bk,
        extract(day from date_bk)::int as day,
        extract(month from date_bk)::int as month,
        trim(to_char(date_bk, 'Month'))::varchar(20) as month_name,
        extract(quarter from date_bk)::int as quarter,
        extract(year from date_bk)::int as year,
        extract(isodow from date_bk)::int as day_of_week,
        case
            when extract(isodow from date_bk) in (6, 7) then true
            else false
        end as is_weekend
    from dates

)

select * from final