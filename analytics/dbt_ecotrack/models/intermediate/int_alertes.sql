{{config(materialized='ephemeral')}}

WITH mesures AS (
    SELECT * FROM {{ref('stg_mesures')}}
),

-- Les indictateurs

WITH mesures_socle AS (
    SELECT * FROM {{ ref('int_mesures_normalises') }}
),

analyses_alertes AS (
    SELECT *,
        -- Alerte de remplissage des conteneurs 
        CASE
            WHEN fill_rate >= 95 THEN 'Débordement'
            WHEN fill_rate >= 80 THEN 'Critique'
            WHEN fill_rate >= 60 THEN 'Elevé'
            ELSE 'Normal'
        END AS alerte_remplissage

        -- Alerte niveau batterie
        CASE
            WHEN battery_pct <= 15 THEN 'Critique'
            WHEN battery_pct <= 30 THEN 'Faible'
            ELSE 'Optimale'
        END AS alerte_battery

        -- Alerte de débordement combinée
        CASE
            WHEN fill_rate >= 95 AND battery_pct <= 15 THEN TRUE
            ELSE FALSE
        END AS is_priority_intervention

        -- Détection de fermentation ou danger (via la température)
        CASE 
            WHEN temperature_c > 55 THEN 'Danger Feu'
            WHEN temperature_c > 35 THEN 'Fermentation'
            ELSE 'Stable'
        END AS alerte_thermique

        -- Indicateurs booléens pour les KPIs rapides (SUM dans Power BI)
        CASE WHEN taux_remplissage_pct >= 80 THEN 1 ELSE 0 END AS is_critical_fill,
        CASE WHEN taux_remplissage_pct >= 95 THEN 1 ELSE 0 END AS is_overflow,

    FROM mesures_socle
)  

SELECT * FROM analyses_alertes
