# ============================================================
# ECOTRACK v2 — Création ecotrack_dashboard.py
# ============================================================

dashboard_code = '''
import streamlit as st
import psycopg2
import pandas as pd
import numpy as np
import plotly.express as px
import plotly.graph_objects as go
from datetime import datetime, timedelta

# ── Configuration page ────────────────────────────────────────
st.set_page_config(
    page_title="ECOTRACK v2 — Dashboard",
    page_icon="♻️",
    layout="wide",
    initial_sidebar_state="expanded"
)

# ── CSS personnalisé ──────────────────────────────────────────
st.markdown("""
<style>
    .metric-rouge  { background:#FCEBEB; border-left:5px solid #A32D2D;
                     padding:15px; border-radius:8px; margin:5px 0; }
    .metric-vert   { background:#EAF3DE; border-left:5px solid #27500A;
                     padding:15px; border-radius:8px; margin:5px 0; }
    .metric-bleu   { background:#D6E4F0; border-left:5px solid #1F4E79;
                     padding:15px; border-radius:8px; margin:5px 0; }
    .metric-orange { background:#FAEEDA; border-left:5px solid #854F0B;
                     padding:15px; border-radius:8px; margin:5px 0; }
    .title-main    { color:#1F4E79; font-size:2.2rem; font-weight:800; }
    .subtitle      { color:#5F5E5A; font-size:1rem; font-style:italic; }
</style>
""", unsafe_allow_html=True)

# ── Connexion PostgreSQL ──────────────────────────────────────
@st.cache_resource
def get_connection():
    return psycopg2.connect(
        host     = st.secrets["postgres"]["host"],
        database = st.secrets["postgres"]["database"],
        user     = st.secrets["postgres"]["user"],
        password = st.secrets["postgres"]["password"],
        port     = st.secrets["postgres"]["port"]
    )

@st.cache_data(ttl=300)
def load_predictions():
    conn = get_connection()
    df = pd.read_sql("""
        SELECT
            container_sk, container_bk, date_bk, city,
            zone_name, waste_type_name, capacity_l,
            avg_fill, max_fill, min_fill, fill_j1,
            fill_trend_1j, overflow_count,
            pred_fill_j1, pred_fill_j1_error,
            pred_overflow_proba, pred_overflow_24h,
            pred_alert_level, anomaly_score, is_anomaly,
            created_at
        FROM dw.predictions
        ORDER BY date_bk DESC, pred_overflow_proba DESC
    """, conn)
    df["date_bk"] = pd.to_datetime(df["date_bk"])
    return df

@st.cache_data(ttl=300)
def load_zones():
    conn = get_connection()
    return pd.read_sql("""
        SELECT DISTINCT
            dc.container_sk,
            dz.latitude,
            dz.longitude,
            dz.city,
            dz.zone_name
        FROM dw.dim_container dc
        JOIN dw.dim_zone dz ON dc.zone_bk = dz.zone_bk
        WHERE dc.is_current = TRUE
    """, conn)

# ── Chargement données ────────────────────────────────────────
try:
    with st.spinner("Chargement des données..."):
        df    = load_predictions()
        zones = load_zones()
        df    = df.merge(zones[["container_sk","latitude","longitude"]],
                         on="container_sk", how="left")
except Exception as e:
    st.error(f"❌ Erreur connexion : {e}")
    st.stop()

# ── Sidebar ───────────────────────────────────────────────────
with st.sidebar:
    st.image("https://img.icons8.com/color/96/recycling.png", width=80)
    st.markdown("## ♻️ ECOTRACK v2")
    st.markdown("*Collecte prédictive des déchets*")
    st.divider()

    st.markdown("### 🎛️ Filtres")
    villes_dispo = ["Toutes"] + sorted(df["city"].unique().tolist())
    ville_sel    = st.selectbox("🏙️ Ville", villes_dispo)

    dates_dispo  = sorted(df["date_bk"].dt.date.unique())
    date_sel     = st.selectbox("📅 Date", dates_dispo,
                                index=len(dates_dispo)-1)

    seuil_proba  = st.slider("⚠️ Seuil alerte (%)",
                              min_value=0, max_value=100,
                              value=50, step=5) / 100

    st.divider()
    st.markdown("### 📊 Modèles ML")
    st.success("Modèle 1 — Régression\\nR² = 0.7675")
    st.success("Modèle 2 — Classification\\nAUC = 0.9990")
    st.info("Modèle 3 — Anomalies\\n38 000 détectées (5%)")
    st.divider()
    st.caption(f"Données : {len(df):,} prédictions")
    st.caption(f"Serveur : 84.247.173.158")
    st.caption(f"Mise à jour : {datetime.now().strftime('%H:%M')}")

# ── Filtrage ──────────────────────────────────────────────────
df_date = df[df["date_bk"].dt.date == date_sel]
if ville_sel != "Toutes":
    df_date = df_date[df_date["city"] == ville_sel]
df_alert = df_date[df_date["pred_overflow_proba"] >= seuil_proba]

# ── En-tête ───────────────────────────────────────────────────
st.markdown(
    "<p class=\\'title-main\\'>♻️ ECOTRACK v2 — Dashboard Prédictif</p>",
    unsafe_allow_html=True
)
st.markdown(
    "<p class=\\'subtitle\\'>De la collecte calendaire à la collecte intelligente"
    f" · {date_sel} · {ville_sel}</p>",
    unsafe_allow_html=True
)
st.divider()

# ══════════════════════════════════════════════════════════════
# SECTION 1 — KPI GLOBAUX
# ══════════════════════════════════════════════════════════════
st.markdown("## 📊 KPI Globaux")

rouge  = (df_date["pred_alert_level"] == "ROUGE").sum()
orange = (df_date["pred_alert_level"] == "ORANGE").sum()
jaune  = (df_date["pred_alert_level"] == "JAUNE").sum()
vert   = (df_date["pred_alert_level"] == "VERT").sum()
total  = len(df_date)
anomalies = df_date["is_anomaly"].sum()

c1, c2, c3, c4, c5, c6 = st.columns(6)
c1.metric("🔴 Urgents",    f"{rouge:,}",
          f"{rouge/total*100:.1f}%" if total > 0 else "0%")
c2.metric("🟠 À risque",   f"{orange:,}",
          f"{orange/total*100:.1f}%" if total > 0 else "0%")
c3.metric("🟡 Surveillance", f"{jaune:,}",
          f"{jaune/total*100:.1f}%" if total > 0 else "0%")
c4.metric("🟢 Normaux",    f"{vert:,}",
          f"{vert/total*100:.1f}%" if total > 0 else "0%")
c5.metric("⚠️ Anomalies",  f"{anomalies:,}",
          f"{anomalies/total*100:.1f}%" if total > 0 else "0%")
c6.metric("📦 Total",      f"{total:,}", "conteneurs")

st.divider()

# ══════════════════════════════════════════════════════════════
# SECTION 2 — TOP CONTENEURS À RISQUE
# ══════════════════════════════════════════════════════════════
st.markdown("## 🚨 Top conteneurs à collecter")

col_left, col_right = st.columns([2, 1])

with col_left:
    top_alert = df_date[df_date["pred_overflow_proba"] >= seuil_proba][[
        "container_bk", "city", "zone_name", "waste_type_name",
        "avg_fill", "pred_fill_j1", "pred_overflow_proba",
        "pred_alert_level", "is_anomaly", "capacity_l"
    ]].sort_values("pred_overflow_proba", ascending=False).head(50)

    top_alert.columns = [
        "Conteneur", "Ville", "Zone", "Type déchet",
        "Taux actuel %", "Prédit J+1 %", "Proba overflow",
        "Alerte", "Anomalie", "Capacité L"
    ]

    def color_alert(val):
        colors = {
            "ROUGE":  "background-color:#FCEBEB; color:#A32D2D; font-weight:bold",
            "ORANGE": "background-color:#FAEEDA; color:#854F0B; font-weight:bold",
            "JAUNE":  "background-color:#FFFDE7; color:#F57F17; font-weight:bold",
            "VERT":   "background-color:#EAF3DE; color:#27500A"
        }
        return colors.get(val, "")

    styled = top_alert.style\\
        .applymap(color_alert, subset=["Alerte"])\\
        .format({
            "Taux actuel %"  : "{:.1f}%",
            "Prédit J+1 %"   : "{:.1f}%",
            "Proba overflow" : "{:.1%}"
        })
    st.dataframe(styled, use_container_width=True, height=400)

with col_right:
    st.markdown("### Distribution des alertes")
    fig_pie = px.pie(
        values=[rouge, orange, jaune, vert],
        names=["🔴 ROUGE", "🟠 ORANGE", "🟡 JAUNE", "🟢 VERT"],
        color_discrete_sequence=["#A32D2D","#854F0B","#F57F17","#27500A"],
        hole=0.4
    )
    fig_pie.update_layout(
        margin=dict(t=20, b=20, l=20, r=20),
        showlegend=True,
        height=300
    )
    st.plotly_chart(fig_pie, use_container_width=True)

    st.markdown("### Taux moyen par alerte")
    if len(df_date) > 0:
        avg_by_alert = df_date.groupby("pred_alert_level")["avg_fill"].mean()
        for level, avg in avg_by_alert.items():
            emoji = {"ROUGE":"🔴","ORANGE":"🟠","JAUNE":"🟡","VERT":"🟢"}.get(level,"")
            st.metric(f"{emoji} {level}", f"{avg:.1f}%")

st.divider()

# ══════════════════════════════════════════════════════════════
# SECTION 3 — CARTE GÉOGRAPHIQUE
# ══════════════════════════════════════════════════════════════
st.markdown("## 🗺️ Carte des alertes")

df_map = df_date.dropna(subset=["latitude","longitude"])

if len(df_map) > 0:
    color_map = {
        "ROUGE":"#A32D2D", "ORANGE":"#E67E22",
        "JAUNE":"#F1C40F", "VERT":"#27500A"
    }
    df_map["color"] = df_map["pred_alert_level"].map(color_map)
    df_map["size"]  = df_map["pred_overflow_proba"] * 20 + 5

    fig_map = px.scatter_mapbox(
        df_map,
        lat="latitude", lon="longitude",
        color="pred_alert_level",
        color_discrete_map=color_map,
        size="size",
        size_max=20,
        hover_name="container_bk",
        hover_data={
            "city"               : True,
            "avg_fill"           : ":.1f",
            "pred_overflow_proba": ":.1%",
            "pred_alert_level"   : True,
            "size"               : False,
            "latitude"           : False,
            "longitude"          : False
        },
        zoom=5,
        center={"lat":46.8, "lon":2.3},
        mapbox_style="open-street-map",
        title=f"Alertes conteneurs — {date_sel}",
        height=550
    )
    fig_map.update_layout(margin=dict(t=40, b=0, l=0, r=0))
    st.plotly_chart(fig_map, use_container_width=True)
else:
    st.warning("Coordonnées GPS non disponibles pour cette sélection.")

st.divider()

# ══════════════════════════════════════════════════════════════
# SECTION 4 — ANALYSE PAR VILLE
# ══════════════════════════════════════════════════════════════
st.markdown("## 🏙️ Analyse par ville")

col_v1, col_v2 = st.columns(2)

with col_v1:
    ville_stats = df_date.groupby("city").agg(
        avg_fill        = ("avg_fill","mean"),
        taux_overflow   = ("pred_overflow_24h","mean"),
        nb_urgents      = ("pred_alert_level", lambda x: (x=="ROUGE").sum()),
        nb_anomalies    = ("is_anomaly","sum")
    ).round(2).sort_values("avg_fill", ascending=False).reset_index()

    fig_ville = px.bar(
        ville_stats, x="city", y="avg_fill",
        color="avg_fill",
        color_continuous_scale=["#27500A","#F1C40F","#A32D2D"],
        title="avg_fill moyen par ville",
        labels={"city":"Ville","avg_fill":"Taux moyen (%)"}
    )
    fig_ville.add_hline(
        y=df_date["avg_fill"].mean(),
        line_dash="dash", line_color="red",
        annotation_text=f"Moy={df_date['avg_fill'].mean():.1f}%"
    )
    fig_ville.update_layout(height=350, showlegend=False)
    st.plotly_chart(fig_ville, use_container_width=True)

with col_v2:
    fig_ov = px.bar(
        ville_stats, x="city", y="nb_urgents",
        color="nb_urgents",
        color_continuous_scale=["#EAF3DE","#FCEBEB"],
        title="Nombre d\'alertes ROUGE par ville",
        labels={"city":"Ville","nb_urgents":"Alertes 🔴"}
    )
    fig_ov.update_layout(height=350, showlegend=False)
    st.plotly_chart(fig_ov, use_container_width=True)

st.markdown("### Tableau récapitulatif par ville")
ville_stats.columns = [
    "Ville","avg_fill %","Taux overflow","Alertes ROUGE","Anomalies"
]
st.dataframe(
    ville_stats.style.format({
        "avg_fill %"    : "{:.2f}%",
        "Taux overflow" : "{:.2%}"
    }),
    use_container_width=True
)

st.divider()

# ══════════════════════════════════════════════════════════════
# SECTION 5 — ANOMALIES CAPTEURS
# ══════════════════════════════════════════════════════════════
st.markdown("## ⚠️ Anomalies capteurs détectées")

df_anom = df_date[df_date["is_anomaly"]==1][[
    "container_bk","city","zone_name","waste_type_name",
    "avg_fill","avg_fill","anomaly_score",
    "overflow_count","pred_alert_level"
]].sort_values("anomaly_score").head(30)

if len(df_anom) > 0:
    col_a1, col_a2 = st.columns([2,1])
    with col_a1:
        st.dataframe(df_anom, use_container_width=True, height=300)
    with col_a2:
        fig_anom = px.histogram(
            df_date, x="anomaly_score",
            color_discrete_sequence=["#1F4E79"],
            title="Distribution scores anomalie",
            nbins=60
        )
        fig_anom.add_vline(
            x=0, line_dash="dash",
            line_color="red",
            annotation_text="Seuil"
        )
        fig_anom.update_layout(height=300)
        st.plotly_chart(fig_anom, use_container_width=True)
else:
    st.success("✅ Aucune anomalie détectée pour cette sélection.")

st.divider()

# ══════════════════════════════════════════════════════════════
# SECTION 6 — ÉVOLUTION TEMPORELLE
# ══════════════════════════════════════════════════════════════
st.markdown("## 📈 Évolution temporelle")

df_time = df.copy()
if ville_sel != "Toutes":
    df_time = df_time[df_time["city"] == ville_sel]

daily = df_time.groupby("date_bk").agg(
    avg_fill      = ("avg_fill","mean"),
    nb_rouge      = ("pred_alert_level", lambda x: (x=="ROUGE").sum()),
    nb_anomalies  = ("is_anomaly","sum"),
    taux_overflow = ("pred_overflow_24h","mean")
).reset_index()

fig_time = go.Figure()
fig_time.add_trace(go.Scatter(
    x=daily["date_bk"], y=daily["avg_fill"],
    name="avg_fill", line=dict(color="#1F4E79", width=2)
))
fig_time.add_trace(go.Bar(
    x=daily["date_bk"], y=daily["nb_rouge"],
    name="Alertes ROUGE", marker_color="#A32D2D",
    opacity=0.6, yaxis="y2"
))
fig_time.update_layout(
    title=f"Évolution avg_fill et alertes — {ville_sel}",
    yaxis  = dict(title="avg_fill (%)", color="#1F4E79"),
    yaxis2 = dict(title="Alertes ROUGE", overlaying="y",
                  side="right", color="#A32D2D"),
    legend = dict(orientation="h", y=-0.2),
    height = 400,
    hovermode="x unified"
)
st.plotly_chart(fig_time, use_container_width=True)

# ── Footer ────────────────────────────────────────────────────
st.divider()
st.markdown("""
<div style="text-align:center; color:#5F5E5A; font-size:0.85rem; padding:10px;">
    ♻️ ECOTRACK v2.0 · Olivier Ouedraogo & Ferdinand & Dany ·
    INGETIS · Avril 2026 ·
    <b>AUC=0.9990 | R²=0.7675 | -90.1% passages</b>
</div>
""", unsafe_allow_html=True)
'''

# ── Sauvegarde du fichier ─────────────────────────────────────
with open('/content/ecotrack_dashboard.py', 'w', encoding='utf-8') as f:
    f.write(dashboard_code)

print("✅ ecotrack_dashboard.py créé dans /content")
print(f"   Taille : {len(dashboard_code):,} caractères")

# ── Création du fichier secrets ───────────────────────────────
import os
os.makedirs('/content/.streamlit', exist_ok=True)

secrets = """
[postgres]
host     = "84.247.173.158"
database = "airflow"
user     = "ecotrack_2026"
password = "ecotrack_2026"
port     = 5432
"""

with open('/content/.streamlit/secrets.toml', 'w') as f:
    f.write(secrets)

print("✅ .streamlit/secrets.toml créé")
print("\nFichiers prêts pour déploiement Streamlit Cloud !")
print("\nContenu /content :")
import subprocess
result = subprocess.run(['ls', '-la', '/content'], capture_output=True, text=True)
print(result.stdout)