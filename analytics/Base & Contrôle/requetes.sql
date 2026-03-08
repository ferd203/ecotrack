
-- 1.) Nombbre de container par zone

SELECT z.zone_name,
       COUNT(c.container_sk) AS nb_conteneurs
FROM DW.dim_zone z
LEFT JOIN DW.dim_container c ON c.zone_bk = z.zone_bk
GROUP BY z.zone_name
ORDER BY nb_conteneurs DESC;

-- 2.) Liste des commentaires actifs

SELECT container_sk, status
FROM DW.DIM_CONTAINER
WHERE is_current = TRUE;

-- 3.) Mésure IoT les plus récentes

SELECT *
FROM DW.fact_measurement
ORDER BY measurement_timestamp DESC
LIMIT 20;

--4.) zones sans conteneurs

SELECT z.zone_name
FROM DW.DIM_ZONE z
LEFT JOIN DW.DIM_CONTAINER c
ON z.zone_bk = c.zone_bk
WHERE c.container_bk IS NULL;

-- 5.) Container mise a jours SCD type 2

SELECT container_sk, container_bk, capacity_l, version, is_current
FROM dw.dim_container
WHERE container_bk = 'CONT_00001'
ORDER BY version;

-- 6.) Surface d'une zone

SELECT zone_name,
       centroid AS surface_km2
FROM DW.dim_zone;

-- 7.) Contrôle volumes Mesures

SELECT COUNT(*) AS total_mesures
FROM DW.FACT_MEASUREMENT;

-- 8.) Les mesures du mois de Mars 2023

SELECT *
FROM DW.FACT_MEASUREMENT
WHERE measurement_timestamp BETWEEN '2023-03-01' AND '2023-03-31';

-- 9.) nombre de mesures par jour

SELECT
    t.date_bk,
    COUNT(*) AS nb_mesures
FROM DW.FACT_MEASUREMENT f
JOIN DW.DIM_TIME t ON f.time_sk = t.time_sk
GROUP BY t.date_bk
ORDER BY t.date_bk;

-- 10.) Baterrie Moyenne 

  SELECT AVG(battery_pct) AS avg_battery
FROM DW.fact_measurement
Limit 10000-- BLOC 3 — Analytics KPI