{{ config(materialized='ephemeral') }}

WITH mesures_socle AS (
    SELECT * FROM {{ ref('int_mesures_normalises') }}
),

dim_temps AS (
    SELECT *
    FROM {{ ref('stg_temps') }}
),

calculs_temporels AS (
    SELECT
        m.time_sk,
        t.date_bk,
        t.month,
        t.year,
        t.day_of_week,
        
        -- 1. MOYENNE DE REMPLISSAGE QUOTIDIENNE
        -- On utilise une Window Function pour lisser les données par jour
        AVG(m.taux_remplissage_pct) OVER (PARTITION BY t.date_bk) AS avg_fill_daily,

        -- 2. MOYENNE MOBILE SUR 7 JOURS (Lissage de tendance)
        AVG(m.taux_remplissage_pct) OVER (
            ORDER BY t.date_bk 
            ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
        ) AS moving_avg_fill_7d,

        -- 3. CALCUL DU PAPI (Période Année Précédente / YoY)
        -- On compare le remplissage actuel au même jour de l'année précédente
        LAG(m.taux_remplissage_pct, 364) OVER (
            PARTITION BY m.container_sk 
            ORDER BY t.date_bk
        ) AS fill_rate_last_year,

        -- 4. VOLUME CUMULÉ DU MOIS (Running Total)
        SUM(m.volume_litres) OVER (
            PARTITION BY t.month, t.year 
            ORDER BY t.date_bk
        ) AS running_total_volume_month

    FROM mesures_socle m
    JOIN dim_temps t ON m.time_sk = t.time_sk
)

SELECT 
    *,
    -- Calcul de l'écart YoY en pourcentage
    ROUND(
        ((avg_fill_daily - NULLIF(fill_rate_last_year, 0)) / NULLIF(fill_rate_last_year, 0)) * 100, 
        2
    ) AS yoy_evolution_pct
FROM calculs_temporels