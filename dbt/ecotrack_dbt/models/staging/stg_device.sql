{{ config(materialized='view') }}

select

    id as device_sk,
    device_bk,
    model,
    installation_date,
    is_active

from {{ source('raw','dim_device_raw') }}