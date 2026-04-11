{{ config(materialized='view') }}

SELECT
    agent_sk,
    agent_bk AS agent_id,
    full_name,
    role,
    is_active
FROM dw.dim_agent