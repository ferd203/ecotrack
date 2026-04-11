{{ config(materialized='view') }}

SELECT
    time_sk,
    date_bk AS full_date,
    day,
    month,
    month_name,
    year,
    is_weekend
FROM dw.dim_time