-- 1.) Nombre de conteneurs par zone
-- Permet d’identifier les zones sous-équipées ou sur-équipées.

SELECT
    z.zone_name,
    COUNT(c.container_sk) AS nb_conteneurs
FROM DW.DIM_CONTAINER c
JOIN DW.DIM_ZONE z
ON c.zone_bk = z.zone_bk
WHERE c.is_current = TRUE
GROUP BY z.zone_name
ORDER BY nb_conteneurs DESC;

-- 2.) Densité de conteneurs par km²
-- Permet d’évaluer la couverture du service de collecte.

SELECT
    z.zone_name,
    COUNT(c.container_sk) AS nb_conteneurs,
    z.area_km2,
    ROUND(COUNT(c.container_sk) / z.area_km2,2) AS densite_conteneurs
FROM DW.DIM_ZONE z
LEFT JOIN DW.DIM_CONTAINER c
ON z.zone_bk = c.zone_bk
GROUP BY z.zone_name, z.area_km2
ORDER BY densite_conteneurs DESC;

-- 3.) Distance conteneur → centre de zone
-- Permet d’identifier les conteneurs éloignés du centre de service.

SELECT
    c.container_bk,
    z.zone_name,
    ST_Distance(c.location, z.centroid) AS distance_m
FROM DW.DIM_CONTAINER c
JOIN DW.DIM_ZONE z
ON c.zone_bk = z.zone_bk
ORDER BY distance_m DESC
LIMIT 20;

-- 4.) Conteneurs dans un rayon donné (1 km)
-- Permet de rechercher les conteneurs dans une zone d’intervention.
SELECT
    container_bk,
    ST_AsText(location)
FROM DW.DIM_CONTAINER
WHERE ST_DWithin(
    location,
    ST_SetSRID(ST_MakePoint(2.35,48.85),4326),
    1000
);

-- 5.) Zones avec le plus de débordements
-- Analyse spatiale des zones problématiques.

SELECT
    z.zone_name,
    SUM(f.is_overflow) AS nb_debordements
FROM DW.FACT_MEASUREMENT f
JOIN DW.DIM_ZONE z
ON f.zone_sk = z.zone_sk
GROUP BY z.zone_name
ORDER BY nb_debordements DESC;

-- 6.) Localisation des conteneurs les plus remplis
-- Utile pour visualisation sur carte BI.

SELECT
    c.container_bk,
    z.zone_name,
    AVG(f.taux_remplissage_pct) AS remplissage_moyen,
    ST_AsText(c.geom) AS location
FROM DW.FACT_MEASUREMENT f
JOIN DW.DIM_CONTAINER c
ON f.container_sk = c.container_sk
JOIN DW.DIM_ZONE z
ON f.zone_sk = z.zone_sk
GROUP BY c.container_bk, z.zone_name, c.geom
ORDER BY remplissage_moyen DESC
LIMIT 20;

-- 7.) Distance conteneur → route de collecte

-- Permet d’évaluer l’efficacité des tournées.

SELECT
    c.container_bk,
    r.route_bk,
    ST_Distance(c.location, r.geom) AS distance_route
FROM DW.DIM_CONTAINER c
JOIN DW.DIM_ROUTE r
ON ST_DWithin(c.location, r.geom, 1000)
ORDER BY distance_route;

-- 8.) Nombre de conteneurs proches d'une route

-- Analyse la couverture logistique des routes de collecte.

SELECT
    r.route_bk,
    COUNT(c.container_sk) AS nb_conteneurs_proches
FROM DW.DIM_ROUTE r
JOIN DW.DIM_CONTAINER c
ON ST_DWithin(c.geom, r.geom, 500)
GROUP BY r.route_bk
ORDER BY nb_conteneurs_proches DESC;

-- 9.) Zones avec le remplissage moyen le plus élevé
-- Carte thermique potentielle.

SELECT
    z.zone_name,
    AVG(f.taux_remplissage_pct) AS remplissage_moyen
FROM DW.FACT_MEASUREMENT f
JOIN DW.DIM_ZONE z
ON f.zone_sk = z.zone_sk
GROUP BY z.zone_name
ORDER BY remplissage_moyen DESC;

-- 10.)Distance moyenne entre conteneurs
-- Permet d’évaluer la dispersion des équipements.

SELECT
    AVG(ST_Distance(c1.location, c2.location)) AS distance_moyenne
FROM DW.DIM_CONTAINER c1
JOIN DW.DIM_CONTAINER c2
ON c1.container_sk <> c2.container_sk;