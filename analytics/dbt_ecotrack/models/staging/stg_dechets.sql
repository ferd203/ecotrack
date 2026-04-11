{{ config(materialized='view') }}

SELECT
    waste_type_sk,
    waste_type_bk AS waste_type_id,
    waste_type_name
FROM dw.dim_waste_type