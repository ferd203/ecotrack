{{ config(materialized='view') }}

select

    id as agent_sk,
    agent_bk,
    firstname,
    lastname,
    role,
    is_active

from {{ source('raw','dim_agent_raw') }}