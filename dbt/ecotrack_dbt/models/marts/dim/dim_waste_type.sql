{{ config(
    materialized='table',
    post_hook=[
      "create index if not exists idx_dim_waste_type_bk on {{ this }}(waste_type_bk)"
    ]
) }}

select
    waste_type_sk,
    waste_type_bk,
    waste_type_name
from {{ ref('stg_waste_type') }}