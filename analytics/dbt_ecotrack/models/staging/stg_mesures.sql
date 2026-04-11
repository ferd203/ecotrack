{{ config(materialized='view') }}

SELECT
    measurement_sk,
    time_sk,
    container_sk,
    zone_sk,
    route_sk,
    waste_type_sk,
    agent_sk,
    device_sk,
    
    -- Les indicateurs techniques
    taux_remplissage_pct AS fill_rate,
    volume_litres,
    temperature_c,
    battery_pct,
    poids_estime_kg AS weight_kg,
    
    -- Le flag d'alerte
    is_overflow,
    
    -- Le timestamp technique
    measurement_timestamp
    
FROM dw.fact_measurement 