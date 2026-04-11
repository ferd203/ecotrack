{{ config(materialized='ephemeral')}}

WITH mesure_socle AS (
    SELECT * FROM {{ref('int_mesures_normalises')}}
),

devices AS (
    SELECT * FROM {{ref('stg_device')}}
)?

derniers_activites AS (
    SELECT
        device_sk,
        MAX(measurement_timestamp) AS date_dernier_signal
    FROM mesure_socle
    GROUP BY 1
)

analyse_sante_iot AS (
    SELECT
        m.device_sk,
        d.device_bk,
        d.model AS device_model,
        m.battery_pct,
        da.date_dernier_signal,

        -- 1. DÉTECTION DE PANNE (Time-out)
        -- Si aucun signal depuis plus de 24h, on considère le capteur "Muet"
        CASE 
            WHEN da.date_dernier_signal < CURRENT_TIMESTAMP - INTERVAL '24 hours' THEN 'Hors ligne'
            ELSE 'ACTIF'
        END AS etat_connexion,

        -- 3. CALCUL DE L'ÂGE DU SIGNAL
        EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - da.date_dernier_signal)) / 3600 AS heures_depuis_signal

    FROM mesures_socle m
    JOIN devices d ON m.device_sk = d.device_sk
    JOIN derniere_activite da ON m.device_sk = da.device_sk
    -- On ne garde que l'état le plus récent pour chaque appareil
    WHERE m.measurement_timestamp = da.date_dernier_signal
)

SELECT * FROM analyse_sante_iot