{{ config(
    materialized='table',
    post_hook=[
        "create index if not exists idx_dim_agent_bk on {{ this }}(agent_bk)",
      "create index if not exists idx_dim_agent_role on {{ this }}(role)"
    ]
) }}

select
    agent_sk,
    agent_bk,
    firstname,
    lastname,
    role,
    is_active,
    current_timestamp as etl_date
from {{ ref('stg_agent') }}