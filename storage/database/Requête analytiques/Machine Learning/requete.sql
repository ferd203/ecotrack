-- 1.) Dataset ML de base (features propres)
-- Objectif : créer un dataset propre pour entraîner un modèle.
SELECT
    f.container_sk,
    f.zone_sk,
    d.time_sk,
    d.day,
    f.taux_remplissage_pct,
    f.volume_litres,
    f.temperature_c,
    f.battery_pct,
    f.poids_estime_kg,
    f.is_overflow
FROM dw.fact_measurement f
JOIN dw.dim_time d
ON f.time_sk = d.time_sk;


-- 2.) Création du label ML (classification overflow)
-- Objectif : prédire le débordement.
SELECT
    container_sk,
    measurement_timestamp,
    taux_remplissage_pct,
    CASE
        WHEN taux_remplissage_pct > 90 THEN 1
        ELSE 0
    END AS overflow_label
FROM dw.fact_measurement;

--3 remplissage moyen par conteneur

SELECT
    container_sk,
    AVG(taux_remplissage_pct) AS avg_fill_rate
FROM DW.FACT_MEASUREMENT
GROUP BY container_sk;

-- 4.) Variabilité du remplissage (STDDEV)
-- Indique la stabilité d’un conteneur.
SELECT
    container_sk,
    STDDEV(taux_remplissage_pct) AS fill_variability
FROM dw.fact_measurement
GROUP BY container_sk;

-- 5.) Moyenne journalière par conteneur
--Feature temporelle
SELECT
    container_sk,
    DATE(measurement_timestamp) AS day,
    AVG(taux_remplissage_pct) AS daily_avg_fill
FROM dw.fact_measurement
GROUP BY container_sk, day;

-- 6.) Moving average (7 jours)
-- Feature pour prédiction temporelle.
SELECT
    container_sk,
    measurement_timestamp,
    AVG(taux_remplissage_pct)
    OVER (
        PARTITION BY container_sk
        ORDER BY measurement_timestamp
        ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
    ) AS moving_avg_7
FROM dw.fact_measurement;

-- 7.) Feature trend (LAG)
-- Permet de détecter une augmentation rapide. 
  SELECT
    container_sk,
    measurement_timestamp,
    taux_remplissage_pct,
    taux_remplissage_pct -
    LAG(taux_remplissage_pct)
    OVER (PARTITION BY container_sk ORDER BY measurement_timestamp)
    AS fill_trend
FROM dw.fact_measurement;

--8.) Nombres de debordement par container
-- Features comportementale
SELECT
    container_sk,
    COUNT(*) FILTER (WHERE is_overflow = 1) AS overflow_count
FROM dw.fact_measurement
GROUP BY container_sk;

-- 9.) Moyenne temperature par zone
-- objectif: peut influencer les dechets
SELECT
    zone_sk,
    AVG(temperature_c) AS avg_temp
FROM dw.fact_measurement
GROUP BY zone_sk;

-- 10.) batterie moyenne capteur
SELECT
    device_sk,
    AVG(battery_pct) AS avg_battery
FROM DW.FACT_MEASUREMENT
GROUP BY device_sk;