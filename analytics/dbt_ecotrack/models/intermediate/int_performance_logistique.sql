{{config(materialized='ephemeral')}}

WITH mesures_socle AS (
    SELECT * FROM{{ref('int_mesures_normalises')}}
),

routes AS (
    SELECT * FROM{{ref('stg_routes')}}
),

agents AS (
    SELECT * FROM{{ref('stg_agent')}}
),

calcul_logique AS (
    SELECT
        m.route_sk,
        m.agent_sk,
        r.route_bk,
        r.planned_distance_m,
        a.full_name AS agent_nom,

        -- Ratio moyen : 0.15 kg CO2 par km pour un camion
        ROUND((r.planned_distance_m / 1000.0) * 0.15, 2) AS co2_emis_kg,

        -- Permet de voir si on déplace un gros camion pour peu de déchets
        SUM(m.volume_litres) AS volume_total_collecte,
        ROUND(
            SUM(m.volume_litres) / NULLIF(r.planned_distance_m / 1000.0, 0), 
            2
        ) AS ratio_volume_km,

        -- 3. COLLECTES INUTILES (Optimisation)
        SUM(
            CASE 
                WHEN m.taux_remplissage_pct < 30 THEN 1 
                ELSE 0 
            END) AS nb_collectes_inutiles,
            
        COUNT(m.measurement_sk) AS total_bacs_visites
    
    FROM mesures_socles m
    INNER JOIN routes r ON m.route_sk = r.route_sk
    INNER JOIN agent a ON m.agent_sk = a.agent_sk
    GROUP BY 1, 2, 3, 4, 5
)

SELECT * FROM calcul_logistique