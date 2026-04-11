{{config(materialized='view')}}

SELECT 
    device_sk,
    device_bk AS device_id,
    model,
    installation_date,
    is_active
FROM dw.dim_device