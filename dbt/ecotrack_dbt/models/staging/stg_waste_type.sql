{{ config(materialized='view') }}

select

    id as waste_type_sk,
    waste_type_bk,
    waste_type_name

from {{ source('raw','dim_waste_type_raw') }}