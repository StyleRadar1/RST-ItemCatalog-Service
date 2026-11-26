## <img src="https://img.shields.io/badge/09-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 9. CI/CD CON EL BADGE DEL PIPELINE

[![CI](https://github.com/StyleRadar1/RST-ItemCatalog-Service/actions/workflows/ci.yml/badge.svg?branch=develop)](https://github.com/StyleRadar1/RST-ItemCatalog-Service/actions/workflows/ci.yml)
[![CD](https://github.com/StyleRadar1/RST-ItemCatalog-Service/actions/workflows/cd.yml/badge.svg?branch=develop)](https://github.com/StyleRadar1/RST-ItemCatalog-Service/actions/workflows/cd.yml)

Cada cambio enviado a `master`, `develop` o a una rama `feature/*`, así como cada pull request, pasa por un pipeline de **GitHub Actions** que compila el proyecto, ejecuta las pruebas, verifica la cobertura, realiza el análisis estático del código (SpotBugs y SonarCloud) y construye la imagen Docker.

Hay dos workflows con responsabilidades separadas:

* **`ci.yml` (validación):** compila, ejecuta las pruebas, verifica la cobertura con JaCoCo, analiza con SpotBugs y SonarCloud, publica el reporte de cobertura como artefacto y construye la imagen Docker. No despliega.
* **`cd.yml` (despliegue):** con un push a `develop` despliega en **QA** y con un push a `master` en **PROD** (Azure Container Apps); también se puede lanzar a mano eligiendo el ambiente. Construye y publica la imagen, crea la base de datos si no existe, despliega el contenedor y espera a que `GET /api/v1/health` responda antes de enviar tráfico (prueba de humo).

### Matriz de Pipelines por Rama Protegida

| Rama | Desencadenador | Etapas |
| :--- | :--- | :--- |
| `feature/**` | Push | `ci.yml`: compilación, pruebas, cobertura, análisis estático e imagen Docker |
| `develop` | Push y pull request | `ci.yml` y, al fusionar, `cd.yml` despliega en QA con verificación del health |
| `master` | Push y pull request | `ci.yml` y, al fusionar, `cd.yml` despliega en PROD con verificación del health |

### Diagrama del Flujo Automatizado (Pipeline Stages)

```mermaid
flowchart LR
    A["Push / PR<br>(GitHub)"] --> B["Build<br>(Maven, Java 21)"]
    B --> C["Tests and JaCoCo<br>(coverage ≥ 85%)"]
    C --> D["Static analysis<br>(SpotBugs, SonarCloud)"]
    D --> E["Docker image"]
    E --> F["CD: deploy QA / PROD<br>(Azure Container Apps)"]
    F --> G["Smoke test<br>(/api/v1/health)"]

    classDef step fill:#12063A,stroke:#5600EF,stroke-width:2px,color:#FFFFFF;
    classDef pass fill:#DCFCE7,stroke:#16A34A,stroke-width:2px,color:#0C0C12;

    class A,B,E,F step;
    class C,D,G pass;
```

<div align="center">
  <table width="100%" style="margin: 18px 0; border: 2px dashed #5600EF; border-radius: 8px; background-color: #FBFBFE;">
    <tr>
      <td align="center" style="padding: 22px;">
        <p>
          <img src="https://img.shields.io/badge/ESPACIO_DE_CAPTURA-EVIDENCIA_04-5600EF?style=for-the-badge" alt="Evidencia 04" /><br />
          <strong><font color="#5600EF" size="3">EJECUCIÓN DEL PIPELINE CI/CD EN VERDE</font></strong>
        </p>
        <p>
<img src="../evidencias/pipeline-ci-cd.png" width="100%" alt="Pipeline CI/CD" /><br />
        </p>
      </td>
    </tr>
  </table>
</div>

---

<p align="center"><a href="../../README.md">⬅ Volver al índice</a></p>
