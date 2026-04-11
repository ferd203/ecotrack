{{ config(materialized='ephemeral') }}

WITH fact_mesures AS (
    SELECT * FROM {{ ref('stg_mesures') }}
),

dim_conteneurs AS (

    -- On sécurise le SCD Type 2 : on ne veut que la version active au moment de la mesure
    SELECT * FROM {{ ref('stg_container') }}
    WHERE is_current = TRUE
),

dim_dechets AS (
    SELECT * FROM {{ ref('stg_dechets') }}
),

normalisation AS (
    SELECT
        f.measurement_sk,
        f.measurement_timestamp,
        f.time_sk,
        f.container_sk,
        f.zone_sk,
        f.route_sk,
        f.agent_sk,
        f.device_sk,
        
        -- Données techniques de mesure
        f.taux_remplissage_pct,
        f.volume_litres,
        f.temperature_c,
        f.battery_pct,
        
        -- Informations du conteneur liées
        c.container_bk,
        c.container_type,
        c.capacity_l,
        c.longitude,
        c.latitude,
        
        -- Type de déchet
        w.waste_type_name,
        
        -- Calcul du poids (si vide dans la source, on applique un ratio par défaut)
        COALESCE(f.poids_estime_kg, ROUND((f.volume_litres * 0.25), 2)) AS poids_kg

    FROM fact_mesures f
    INNER JOIN dim_conteneurs c ON f.container_sk = c.container_sk
    LEFT JOIN dim_dechets w ON f.waste_type_sk = w.waste_type_sk
)

SELECT * FROM normalisation