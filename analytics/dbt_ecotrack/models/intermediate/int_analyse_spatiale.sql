{{ config(materialized='ephemeral') }}

WITH zones AS (
    SELECT * FROM {{ ref('stg_zone') }}
),

conteneurs AS (
    SELECT * FROM {{ ref('stg_container') }}
    WHERE is_current = TRUE
),

statistiques_zone AS (
    SELECT
        z.zone_sk,
        z.zone_name,
        z.area_km2,
        z.population,
        z.geom,
        COUNT(c.container_sk) AS nb_conteneurs,
        
        -- Calcul de la distance moyenne au centre de la zone (PostGIS)
        AVG(public.ST_Distance(c.location, z.geom)) AS distance_moyenne_centre_m

    FROM zones z
    LEFT JOIN conteneurs c ON z.zone_bk = c.zone_bk
    GROUP BY 1, 2, 3, 4, 5
)

SELECT
    *,
    -- DENSITÉ : Combien de bacs par km² ?
    ROUND(nb_conteneurs / NULLIF(area_km2, 0), 2) AS densite_bacs_km2,
    
    -- COUVERTURE : Combien d'habitants par bac ?
    ROUND(population / NULLIF(nb_conteneurs, 0), 0) AS habitants_par_bac

FROM statistiques_zone