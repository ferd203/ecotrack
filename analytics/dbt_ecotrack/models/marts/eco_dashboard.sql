{{ config(
    materialized='table',
    indexes=[
      {'columns': ['measurement_timestamp']},
      {'columns': ['zone_name']},
      {'columns': ['container_bk']}
    ]
) }}

WITH base AS (
    SELECT * FROM {{ ref('int_mesures_normalises') }}
),

ops AS (
    SELECT * FROM {{ ref('int_alertes') }}
),

logistique AS (
    SELECT * FROM {{ ref('int_performance_logistique') }}
),

sante AS (
    SELECT * FROM {{ ref('int_sante_iot') }}
),

spatial_data AS (
    SELECT * FROM {{ ref('int_analyse_spatiale') }}
),

temporel AS (
    SELECT * FROM {{ ref('int_metriques_temporelles') }}
)

SELECT
    -- 1. Dimensions de base
    b.measurement_sk,
    b.measurement_timestamp,
    b.container_bk,
    b.container_type,
    b.waste_type_name,
    b.zone_sk,
    
    -- 2. Métriques Opérationnelles (Remplissage & Poids)
    b.taux_remplissage_pct,
    b.poids_kg,
    o.alerte_remplissage,
    o.is_critical_fill,
    o.is_overflow,
    o.alerte_thermique,
    o.alerte_battery,

    -- 3. Métriques Logistiques & CO2
    l.agent_nom,
    l.co2_emis_kg,
    l.ratio_volume_km,
    l.nb_collectes_inutiles,

    -- 4. Métriques Santé IoT
    s.etat_connexion,

    -- 5. Métriques Spatiales (Zones)
    sp.zone_name,
    sp.densite_bacs_km2,
    sp.habitants_par_bac,
    b.longitude,
    b.latitude,

    -- 6. Métriques Temporelles (Tendances)
    t.avg_fill_daily,
    t.moving_avg_fill_7d,
    t.yoy_evolution_pct

FROM base b
LEFT JOIN ops o ON b.measurement_sk = o.measurement_sk
LEFT JOIN logistique l ON b.route_sk = l.route_sk AND b.agent_sk = l.agent_sk
LEFT JOIN sante s ON b.device_sk = s.device_sk
LEFT JOIN spatial_data sp ON b.zone_sk = sp.zone_sk
LEFT JOIN temporel t ON b.time_sk = t.time_sk