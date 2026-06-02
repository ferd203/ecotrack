# 🤖 ECOTRACK v2 — Machine Learning Pipeline

![Python](https://img.shields.io/badge/Python-3.12-blue?logo=python)
![XGBoost](https://img.shields.io/badge/XGBoost-2.x-orange?logo=xgboost)
![Scikit-learn](https://img.shields.io/badge/Scikit--learn-1.3-F7931E?logo=scikit-learn)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?logo=postgresql)
![Streamlit](https://img.shields.io/badge/Streamlit-Cloud-FF4B4B?logo=streamlit)
![Status](https://img.shields.io/badge/Statut-✅%20Déployé-brightgreen)

> **Projet Fil Rouge — INGETIS Paris · Master 1 Data Engineering · 2025-2026**  
> **Responsable ML** : Olivier Ouedraogo  
> **Équipe** : Olivier Ouedraogo · Ferdinand · Dany

---

## 🎯 Objectif

Transformer 19.4 millions de mesures IoT brutes en un système de **collecte prédictive des déchets urbains** — passer de tournées calendaires (camions qui passent tous les jours peu importe le niveau) à une collecte intelligente pilotée par les données (camions qui partent uniquement quand c'est nécessaire).

```
Avant ECOTRACK : 730 000 passages/an (tous les conteneurs · tous les jours)
Après ECOTRACK :  71 933 passages/an (uniquement les conteneurs urgents)
                  ─────────────────────────────────────────────────────
                  658 067 arrêts inutiles évités par an (-90.1%)
```

---

## 📊 Résultats des 3 modèles

| Modèle | Objectif | Métrique clé | Résultat | Statut |
|--------|----------|-------------|---------|--------|
| XGBoost Régression | Prédire taux remplissage J+1 | R² | **0.7675** | ✅ |
| XGBoost Classification | Détecter overflow 24h | AUC-ROC | **0.9990** | 🏆 |
| Isolation Forest | Détecter anomalies capteurs | Contamination | **5.00%** | ✅ |

---

## 🗂️ Structure du dossier ML

```
ml/
│
├── 📓 notebooks/
│   └── Ecotrack.ipynb          # Pipeline ML complet — 10 cellules
│
├── 📊 dashboard/
│   └── ecotrack_dashboard.py   # Dashboard Streamlit déployé
│
├── 📁 models/                  # Modèles sauvegardés (.pkl)
│   ├── xgb_regression.pkl      # Modèle 1 — XGBoost Régression
│   ├── xgb_classification.pkl  # Modèle 2 — XGBoost Classification
│   ├── isolation_forest.pkl    # Modèle 3 — Isolation Forest
│   ├── feature_cols.pkl        # Liste des 26 features
│   └── encoders.pkl            # LabelEncoders des variables catégorielles
│
└── README.md                   # Ce fichier
```

---

## 🏗️ Architecture du pipeline ML

```
PostgreSQL Contabo (84.247.173.158)
         │
         │  SELECT * FROM dw.vw_ml_dataset
         ▼
┌─────────────────────────────────────────────┐
│  CELLULE 1 — Chargement & imports           │
│  psycopg2 · pandas · numpy · sklearn        │
│  768 000 lignes × 39 colonnes               │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  CELLULE 2 — EDA Statistiques               │
│  describe() · groupby() · value_counts()    │
│  → Découverte déséquilibre 91.3% / 8.7%    │
│  → scale_pos_weight = 10.49 calculé         │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  CELLULE 3 — Visualisations EDA             │
│  9 graphiques · distributions · boxplots    │
│  → Confirmation avg_fill=32.23%             │
│  → Détection valeurs aberrantes             │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  CELLULE 4 — Matrice de corrélation         │
│  Heatmap seaborn · 26×26 features           │
│  → min_fill #1 corrélation (0.821)          │
│  → avg_fill #2 corrélation (0.814)          │
│  → delta_days exclu (variance nulle)        │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  CELLULE 5 — Prétraitement & Split 80/20    │
│  LabelEncoder · dropna · fillna             │
│  Split temporel — cutoff : 2026-02-01       │
│  Train : 608 000 lignes (2025-04 → 2026-02) │
│  Test  : 152 000 lignes (2026-02 → 2026-04) │
└──────────────────┬──────────────────────────┘
                   │
         ┌─────────┴──────────┐
         ▼                    ▼
┌─────────────────┐  ┌─────────────────────────┐
│  CELLULE 6      │  │  CELLULE 7              │
│  XGBoost Reg.   │  │  XGBoost Classif.       │
│  R²    = 0.7675 │  │  AUC    = 0.9990        │
│  RMSE  = 10.81% │  │  Recall = 98.9%         │
│  MAE   = 7.42%  │  │  F1     = 0.9143        │
└────────┬────────┘  └───────────┬─────────────┘
         │                       │
         └──────────┬────────────┘
                    ▼
┌─────────────────────────────────────────────┐
│  CELLULE 8 — Isolation Forest               │
│  contamination = 0.05                       │
│  38 000 anomalies détectées (5.00%)         │
│  Profil : avg_fill=75% · overflow×21        │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  CELLULE 9 — Bilan Impact Métier            │
│  -90.1% passages évités                     │
│  658 067 arrêts inutiles évités/an          │
│  98.9% overflows détectés                   │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────┐
│  CELLULE 10 — Export DW.PREDICTIONS         │
│  execute_values · batch 5 000               │
│  152 000 prédictions en 28.7 secondes       │
│  5 298 lignes/seconde                       │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
        Dashboard Streamlit Cloud
        (accessible publiquement)
```

---

## 📐 La Vue ML — VW_ML_DATASET

### Pourquoi une vue et pas une table ?

La vue `dw.vw_ml_dataset` est une **requête SQL sauvegardée** qui se recalcule dynamiquement à chaque appel. Elle ne stocke aucune donnée physiquement — elle reflète toujours les dernières mesures disponibles dans `FACT_MEASUREMENT`.

```sql
-- Vue construite en 3 CTE successives
WITH aggregated AS (
    -- CTE 1 : agrégation journalière
    -- 19.4M mesures → 768 000 lignes (1 par conteneur par jour)
    SELECT
        container_sk,
        date_bk,
        AVG(taux_remplissage_pct) AS avg_fill,
        MAX(taux_remplissage_pct) AS max_fill,
        MIN(taux_remplissage_pct) AS min_fill,
        STDDEV(taux_remplissage_pct) AS stddev_fill,
        COUNT(*) AS nb_mesures,
        ...
    FROM dw.fact_measurement
    GROUP BY container_sk, date_bk
),
with_dims AS (
    -- CTE 2 : enrichissement avec les dimensions
    -- Ajout city, zone_name, capacity_l, waste_type_name...
    SELECT a.*, dc.capacity_l, dz.city, dz.zone_name, ...
    FROM aggregated a
    JOIN dw.dim_container dc ON a.container_sk = dc.container_sk
    JOIN dw.dim_zone dz ON dc.zone_bk = dz.zone_bk
    WHERE dc.is_current = TRUE
),
with_windows AS (
    -- CTE 3 : features temporelles + targets
    -- LAG = regarder dans le passé (mémoire du modèle)
    -- LEAD = regarder dans le futur (target à prédire)
    SELECT *,
        LAG(avg_fill, 1) OVER w AS fill_j1,
        LAG(avg_fill, 7) OVER w AS fill_j7,
        avg_fill - LAG(avg_fill, 1) OVER w AS fill_trend_1j,
        LEAD(avg_fill, 1) OVER w AS target_fill_j1,
        CASE WHEN LEAD(max_fill, 1) OVER w > 90
             THEN 1 ELSE 0 END AS will_overflow_24h
    FROM with_dims
    WINDOW w AS (PARTITION BY container_sk ORDER BY date_bk)
)
SELECT * FROM with_windows;
```

### Résultat de la vue

```
768 000 lignes = 2 000 conteneurs × 384 jours
26 features    = remplissage + historique + capteurs + géo + temporel
4 targets      = target_fill_j1 · target_fill_j3
                 will_overflow_24h · will_overflow_72h
```

---

## 🔧 Les 26 Features ML

### Famille 1 — Remplissage actuel (5 features)
| Feature | Corrélation target | Importance ML | Rôle |
|---------|-------------------|--------------|------|
| `min_fill` | 0.821 | **60.89%** 🥇 | Niveau plancher journalier |
| `avg_fill` | 0.814 | 16.76% | Niveau moyen journalier |
| `max_fill` | 0.688 | 10.43% | Pic maximum atteint |
| `stddev_fill` | -0.147 | 1.92% | Variabilité du remplissage |
| `overflow_count` | 0.496 | 1.66% | Fois où > 90% dans la journée |

### Famille 2 — Historique temporel (6 features)
| Feature | Description | Importance |
|---------|-------------|-----------|
| `fill_j1` | Taux d'hier | 0.46% |
| `fill_j2` | Taux avant-hier | 0.52% |
| `fill_j3` | Taux il y a 3 jours | 0.49% |
| `fill_j7` | Taux il y a 7 jours | 0.55% |
| `fill_trend_1j` | Variation sur 1 jour | 2.26% |
| `fill_trend_7j` | Variation sur 7 jours | 1.12% |

> `fill_trend_1j = -80%` → collecte hier (chute brutale)  
> `fill_trend_1j = +5%`  → remplissage normal

### Famille 3 — Capteurs IoT (5 features)
| Feature | Description |
|---------|-------------|
| `avg_temp` | Température — influence décomposition biodéchets |
| `avg_battery` | Santé capteur — < 10% = risque panne |
| `avg_volume_litres` | Volume physique occupé |
| `avg_poids_kg` | Densité du contenu |
| `nb_mesures` | Fréquence mesures — faible = capteur défaillant |

### Famille 4 — Conteneur (4 features)
`capacity_l` · `container_type_enc` · `waste_type_name_enc` · `day_of_week`

### Famille 5 — Géographie (4 features)
`city_enc` · `zone_name_enc` · `population` · `area_km2`

### Famille 6 — Temporelle (2 features)
`month` · `quarter`

---

## ✂️ Le Split Temporel 80/20

```python
# Pourquoi temporel et pas aléatoire ?
# → Éviter le data leakage (fuite de données)
# → Les features LAG regardent dans le passé
# → Un split aléatoire mélangerait passé et futur

cutoff = df_ml['date_bk'].quantile(0.80)
# cutoff = 2026-02-01

train = df_ml[df_ml['date_bk'] <= cutoff]  # 608 000 lignes
test  = df_ml[df_ml['date_bk'] >  cutoff]  # 152 000 lignes
```

```
Train : 2025-04-04 → 2026-02-01 | 608 000 lignes | Overflow : 8.86%
Test  : 2026-02-02 → 2026-04-18 | 152 000 lignes | Overflow : 8.47%
```

---

## ⚖️ Le scale_pos_weight — Gestion du déséquilibre

```python
# Déséquilibre constaté en cellule 2
vc = df['will_overflow_24h'].value_counts()
# Classe 0 (normal)   : 701 135 lignes = 91.3%
# Classe 1 (overflow) :  66 865 lignes =  8.7%

spw = vc[0] / vc[1]
# spw = 701 135 / 66 865 = 10.49
```

**Sans scale_pos_weight :**
- Précision apparente : 91.3% (prédit toujours 0)
- Recall : ~0% → tous les overflows manqués
- Modèle inutile opérationnellement

**Avec scale_pos_weight = 10.49 :**
- Se tromper sur un overflow est 10.49× plus pénalisé
- Recall : **98.9%** → seulement 145 overflows manqués
- AUC : **0.9990**

---

## 📈 Résultats détaillés

### Modèle 1 — XGBoost Régression

```
Target    : target_fill_j1 (taux de remplissage J+1)
Durée     : 47.7 secondes (500 arbres)

RMSE  = 10.81%   ← erreur quadratique moyenne
MAE   = 7.42%    ← erreur absolue moyenne
R²    = 0.7675   ← 76.75% de variance expliquée
```

**Top 5 features (régression) :**
```
min_fill       : 60.89% ████████████████████
avg_fill       : 16.76% █████
max_fill       : 10.43% ███
fill_trend_1j  :  2.26% █
stddev_fill    :  1.92% █
```

### Modèle 2 — XGBoost Classification

```
Target    : will_overflow_24h (débordement dans 24h)
Durée     : 67.6 secondes (500 arbres)

AUC-ROC   = 0.9990   🏆 Exceptionnel
F1 Score  = 0.9143
Precision = 0.8502
Recall    = 0.9887
```

**Matrice de confusion :**
```
                 Prédit Normal   Prédit Overflow
Réel Normal      136 877 ✅       2 243 ⚠️
Réel Overflow        145 ❌      12 735 ✅

TN = 136 877  → Jours normaux correctement ignorés
FP =   2 243  → Fausses alertes (1.6% des normaux)
FN =     145  → Overflows manqués (1.1% des overflows)
TP =  12 735  → Vrais overflows détectés (98.9%)
```

**Top 5 features (classification) :**
```
avg_fill       : 69.81% ████████████████████
min_fill       : 11.06% ███
max_fill       : 10.29% ███
fill_trend_1j  :  3.38% █
overflow_count :  1.66% ▌
```

### Modèle 3 — Isolation Forest

```
Contamination : 0.05 (5%)
Durée         : 8.9 secondes (200 arbres)

Anomalies détectées : 38 000 / 760 000 (5.00%)
```

**Profil des anomalies vs normal :**
```
                   Normal    Anomalie   Ratio
avg_fill           30.06%    75.09%    × 2.5
stddev_fill         5.82      19.20    × 3.3
avg_volume_litres  316 L     991 L     × 3.1
avg_poids_kg        59 kg    256 kg    × 4.3
overflow_count       0.77     16.14    × 21
fill_trend_1j       +0.68    -12.34   ← signal collecte
```

---

## 💥 Impact Métier

```
Période de test : 76 jours · 2 000 conteneurs

COLLECTE CALENDAIRE (avant)
  Passages totaux       : 152 000
  Overflows manqués     : inconnu (pas de prédiction)

COLLECTE PRÉDICTIVE ECOTRACK (après)
  Alertes émises        :  14 978
  Overflows détectés    :  12 735 (98.9%)
  Overflows manqués     :     145 (1.1%)
  Fausses alertes       :   2 243
  Passages évités       : 137 022 (-90.1%)

PROJECTION ANNUELLE
  Passages calendaire/an  : 730 000
  Passages prédictif/an   :  71 933
  Passages évités/an      : 658 067 (-90.1%)
```

**Distribution des alertes dans DW.PREDICTIONS :**
```
🔴 ROUGE  (proba ≥ 80%) :  13 706  → collecter aujourd'hui
🟠 ORANGE (proba ≥ 50%) :   1 272  → surveiller demain
🟡 JAUNE  (proba ≥ 30%) :     641  → dans 2-3 jours
🟢 VERT   (proba < 30%) : 136 381  → aucune action
⚠️ Anomalies capteurs   :   7 339
```

---

## 🗄️ Table DW.PREDICTIONS

```sql
-- Structure de la table exportée
CREATE TABLE dw.predictions (
    prediction_id        SERIAL PRIMARY KEY,
    container_sk         INTEGER,
    container_bk         TEXT,
    date_bk              DATE,
    city                 TEXT,
    zone_name            TEXT,
    waste_type_name      TEXT,
    capacity_l           INTEGER,

    -- Features clés
    avg_fill             NUMERIC(6,2),
    max_fill             NUMERIC(6,2),
    min_fill             NUMERIC(6,2),
    fill_j1              NUMERIC(6,2),
    fill_trend_1j        NUMERIC(8,4),
    overflow_count       INTEGER,

    -- Modèle 1 (Régression)
    pred_fill_j1         NUMERIC(6,2),
    pred_fill_j1_error   NUMERIC(6,2),

    -- Modèle 2 (Classification)
    pred_overflow_proba  NUMERIC(6,4),
    pred_overflow_24h    INTEGER,
    pred_alert_level     TEXT,       -- ROUGE/ORANGE/JAUNE/VERT

    -- Modèle 3 (Anomalies)
    anomaly_score        NUMERIC(8,4),
    is_anomaly           INTEGER,

    model_version        TEXT DEFAULT 'v2.0',
    created_at           TIMESTAMP DEFAULT NOW()
);
```

**Export optimisé :**
```python
# execute_values — 100× plus rapide que executemany
from psycopg2.extras import execute_values

execute_values(cur, query, rows, page_size=5000)
# 152 000 lignes en 28.7 secondes → 5 298 lignes/sec
```

---

## 🚀 Dashboard Streamlit

**URL publique :** déployé sur Streamlit Cloud  
**Repository :** `github.com/Olivier-Oued/ecotrack-dashboard`

**6 sections :**
```
1. KPI Globaux       → 215 urgents · 2 000 conteneurs
2. Top conteneurs    → tableau filtrable ville + seuil
3. Carte France      → points colorés par niveau alerte
4. Analyse par ville → avg_fill + alertes ROUGE
5. Anomalies         → capteurs défaillants
6. Évolution         → tendance temporelle
```

**Connexion PostgreSQL via secrets Streamlit :**
```toml
[postgres]
host     = "84.247.173.158"
database = "airflow"
user     = "ecotrack_2026"
port     = 5432
```

---

## ⚙️ Stack technique ML

```
Python 3.12       · pandas · numpy
XGBoost 2.x       · scikit-learn
psycopg2          · psycopg2.extras (execute_values)
matplotlib        · seaborn · plotly
joblib            · Google Colab (GPU/CPU)
Streamlit         · PostgreSQL 16
```

---

## 📋 Axes d'amélioration

```
1. Réentraînement automatique (MLOps)
   → DAG Airflow weekly_retrain chaque lundi
   → Validation AUC avant déploiement

2. Intégration tournées planifiées
   → Ajouter DIM_ROUTE comme feature
   → R² attendu > 0.85 (vs 0.7675 actuel)

3. Dashboard temps réel
   → Job quotidien Airflow → DW.PREDICTIONS
   → Dashboard toujours à jour à J+1

4. Feature is_collection_day
   → Détecter fill_trend_1j < -50%
   → Réduire RMSE de 10.81% → ~7%

5. Scalabilité nationale
   → Transfer Learning sur features géographiques
   → Extension à de nouvelles villes sans réentraînement
```

---

## 👤 Auteur

**Olivier Ouedraogo** — Data Engineer & Data Scientist  
Master 1 Data Engineering · INGETIS Paris  
📧 olivierouedraogo290@gmail.com  
🔗 github.com/Olivier-Oued

---

*ECOTRACK v2.0 · Olivier Ouedraogo & Ferdinand & Dany · INGETIS Paris · 2025-2026*  
*AUC = 0.9990 · R² = 0.7675 · -90.1% passages évités*
