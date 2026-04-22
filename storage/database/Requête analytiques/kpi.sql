--1.) Jour de plus de debordement

SELECT DATE(measurement_timestamp) AS day,
       COUNT(*) AS debordements
FROM DW.fact_measurement
WHERE Taux_remplissage_pct > 100
GROUP BY day
ORDER BY debordements DESC
LIMIT 10;

-- 2.) Pic par Heure

SELECT DATE_TRUNC('hour', measurement_timestamp) AS Time,
       MAX(Taux_remplissage_pct)
FROM DW.fact_measurement
GROUP BY Time
Limit 200;

-- 3.) Mediane par zone

SELECT z.zone_name,
       PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY f.taux_remplissage_pct) AS median_fill
FROM DW.FACT_MEASUREMENT f
JOIN DW.DIM_ZONE z ON f.zone_sk = z.zone_sk
GROUP BY z.zone_name;

--4.) Taux moyenne par ZONE

  SELECT z.zone_name,
       AVG(f.taux_remplissage_pct) AS avg_fill_rate
FROM DW.FACT_MEASUREMENT f
JOIN DW.DIM_ZONE z ON f.zone_sk = z.zone_sk
GROUP BY z.zone_name
Limit 20000;

--5.) Moyenne Journalière

SELECT d.date_bk,
       AVG(f.taux_remplissage_pct) AS daily_avg
FROM DW.FACT_MEASUREMENT f
JOIN DW.DIM_time d ON f.time_sk = d.time_sk
GROUP BY d.date_bk
ORDER BY d.date_bk;

-- 6.) Evolution du remplissage dans le temps

SELECT
    t.year,
    t.month,
    AVG(f.taux_remplissage_pct) AS remplissage_moyen
FROM DW.FACT_MEASUREMENT f
JOIN DW.DIM_TIME t
ON f.time_sk = t.time_sk
GROUP BY t.year, t.month
ORDER BY t.year, t.month;

--7.) Taux remplissage moyenne par ans

SELECT d.year,
       AVG(f.taux_remplissage_pct) AS yearly_avg,
       AVG(f.taux_remplissage_pct)
         - LAG(AVG(f.taux_remplissage_pct))
           OVER (ORDER BY d.year) AS yoy_change
FROM DW.FACT_MEASUREMENT f
JOIN DW.DIM_Time d ON f.time_sk = d.time_sk
GROUP BY d.year;

-- 8.) zones les plus saturées

SELECT d.date_bk,
       AVG(AVG(f.taux_remplissage_pct))
       OVER (ORDER BY d.date_bk
             ROWS BETWEEN 29 PRECEDING AND CURRENT ROW) AS ma_30d
FROM DW.FACT_MEASUREMENT f
JOIN DW.DIM_time d ON f.time_sk = d.time_sk
GROUP BY d.date_bk;

-- 9.) Conteneurs à risque (>90%)

SELECT DISTINCT container_sk
FROM DW.fact_measurement
WHERE taux_remplissage_pct > 90;

-- 10.) Volume total par jour

SELECT DATE(measurement_timestamp),
       SUM(volume_litres)
FROM DW.fact_measurement
GROUP BY DATE(measurement_timestamp);