{{ config(
    materialized='table',
    post_hook=[
      "create index if not exists idx_dim_device_bk on {{ this }}(device_bk)",
      "create index if not exists idx_dim_device_active on {{ this }}(is_active)"
    ]
) }}

select
    device_sk,
    device_bk,
    model,
    installation_date,
    is_active
from {{ ref('stg_device') }}