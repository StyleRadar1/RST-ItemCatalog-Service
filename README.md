<div align="center">

  <a href="https://github.com/StyleRadar1">
    <img src="https://raw.githubusercontent.com/JUANNICOLASA/Frontend-Style-Radar/main/styleradar-frontend-v3/assets/logo/styleradar-logo.svg" width="340" alt="StyleRadar Logo Oficial" />
  </a>

  <h1>STYLE<font color="#5600EF">RADAR</font> &nbsp;·&nbsp; <code>ms-item-catalogo</code></h1>

  <p align="center">
    <img src="https://img.shields.io/badge/BOUNDED_CONTEXT-Catalogo_de_prendas-12063A?style=for-the-badge" alt="Context" />
    <img src="https://img.shields.io/badge/PUERTO_OFICIAL-8085-5600EF?style=for-the-badge" alt="Port" />
    <img src="https://img.shields.io/badge/ARQUITECTURA-HEXAGONAL-16A34A?style=for-the-badge" alt="Hexagonal" />
  </p>

  <p align="center">
    <strong><font color="#5600EF">Descubre tu estilo. En tu ciudad.</font></strong><br />
    <em>Plataforma de moda local con inventario multi-sede geolocalizado en tiempo real y probador virtual para Bogotá.</em>
  </p>

  <p align="center">
    <a href="https://github.com/StyleRadar1/RST-ItemCatalog-Service/actions/workflows/ci.yml">
      <img src="https://github.com/StyleRadar1/RST-ItemCatalog-Service/actions/workflows/ci.yml/badge.svg?branch=develop" alt="CI" />
    </a>
    <a href="#9-pruebas-y-cobertura">
      <img src="https://img.shields.io/badge/JaCoCo_Coverage-%E2%89%A5_85%25-16A34A?style=for-the-badge&logo=codecov&logoColor=white" alt="Coverage" />
    </a>
    <a href="#6-tecnologías-y-dependencias">
      <img src="https://img.shields.io/badge/Java-21_LTS-12063A?style=for-the-badge&logo=openjdk&logoColor=C4B5FD" alt="Java 21" />
    </a>
    <a href="#6-tecnologías-y-dependencias">
      <img src="https://img.shields.io/badge/Spring_Boot-3.3.4-12063A?style=for-the-badge&logo=spring&logoColor=C4B5FD" alt="Spring Boot 3.3.4" />
    </a>
    <a href="#7-cómo-ejecutar-local-y-con-docker">
      <img src="https://img.shields.io/badge/Docker-OCI_Container-12063A?style=for-the-badge&logo=docker&logoColor=C4B5FD" alt="Docker OCI" />
    </a>
  </p>

</div>


---

## <img src="https://img.shields.io/badge/ÍNDICE-5600EF?style=for-the-badge" height="20" align="absmiddle" /> ÍNDICE DE CONTENIDOS

1. [Nombre del servicio y su bounded context](#1-nombre-del-servicio-y-su-bounded-context)
2. [Equipo y responsables](#2-equipo-y-responsables)
3. [Arquitectura hexagonal con su diagrama](#3-arquitectura-hexagonal-con-su-diagrama)
4. [Endpoints y enlace a Swagger](#4-endpoints-y-enlace-a-swagger)
5. [Eventos que publica y consume](#5-eventos-que-publica-y-consume)
6. [Tecnologías y dependencias](#6-tecnologías-y-dependencias)
7. [Cómo ejecutar local y con Docker](#7-cómo-ejecutar-local-y-con-docker)
8. [Variables de entorno](#8-variables-de-entorno)
9. [Pruebas y cobertura](#9-pruebas-y-cobertura)
10. [CI/CD con el badge del pipeline](#10-cicd-con-el-badge-del-pipeline)
11. [Ramas y cómo contribuir](#11-ramas-y-cómo-contribuir)
12. [Enlace a la épica en Jira](#12-enlace-a-la-épica-en-jira)

---

<a id="1-nombre-del-servicio-y-su-bounded-context"></a>

## <img src="https://img.shields.io/badge/01-5600EF?style=for-the-badge" height="20" align="absmiddle" /> NOMBRE DEL SERVICIO Y SU BOUNDED CONTEXT

* **Identificador de Servicio:** `ms-item-catalogo`
* **Paquete Arquitectónico:** `Paquete 2: Catálogo`
* **Puerto de Escucha Oficial:** `8085`
* **Base de Datos Asignada:** `PostgreSQL 15.8` (`styleradar_item_catalog`), aislada bajo el patrón *Database-per-Service*.
* **Bounded Context:** `Catálogo de prendas`

### Responsabilidades del Dominio

Este microservicio encapsula exclusivamente la lógica de negocio de gestión, búsqueda y moderación de prendas de StyleRadar.

| Qué HACE este Microservicio (Alcance)                             | Qué NO HACE este Microservicio (Límites)                                          |
| :----------------------------------------------------------------- | :---------------------------------------------------------------------------------- |
| • Publica, edita y retira prendas del catálogo.                   | • NO autentica usuarios.                                                           |
| • Busca, filtra y ordena las prendas del catálogo.                | • NO calcula distancias (delegado a `ms-geolocalizacion` en `:8086`).              |
| • Modera las publicaciones de los almacenes.                      | • NO gestiona el inventario por talla (delegado a `ms-inventario` en `:8084`).     |
| • Garantiza persistencia ACID e idempotencia en su base de datos. | • NO accede directamente a bases de datos de otros microservicios.                 |

### Arquetipos de Moda y Personajes de StyleRadar

<div align="center">

| Antonia (Sastre) | Mateo (Calle) | Aura (Futuro) | Nora (Rebelde) | Tomás (Esencial) |
| :---: | :---: | :---: | :---: | :---: |
| <img src="https://raw.githubusercontent.com/JUANNICOLASA/Frontend-Style-Radar/main/styleradar-frontend-v3/assets/mascota/antonia.jpg" width="110" style="border-radius:8px;" alt="Antonia" /><br>**Sastre**<br><sub>Tiendas & Descubrimiento</sub> | <img src="https://raw.githubusercontent.com/JUANNICOLASA/Frontend-Style-Radar/main/styleradar-frontend-v3/assets/mascota/mateo.jpg" width="110" style="border-radius:8px;" alt="Mateo" /><br>**Calle**<br><sub>Playlists & Cultura Urbana</sub> | <img src="https://raw.githubusercontent.com/JUANNICOLASA/Frontend-Style-Radar/main/styleradar-frontend-v3/assets/mascota/aura.jpg" width="110" style="border-radius:8px;" alt="Aura" /><br>**Futuro**<br><sub>Probador Virtual AI</sub> | <img src="https://raw.githubusercontent.com/JUANNICOLASA/Frontend-Style-Radar/main/styleradar-frontend-v3/assets/mascota/nora.jpg" width="110" style="border-radius:8px;" alt="Nora" /><br>**Rebelde**<br><sub>Segunda Vida & Vintage</sub> | <img src="https://raw.githubusercontent.com/JUANNICOLASA/Frontend-Style-Radar/main/styleradar-frontend-v3/assets/mascota/tomas.jpg" width="110" style="border-radius:8px;" alt="Tomás" /><br>**Esencial**<br><sub>Fundaciones & Donaciones</sub> |

</div>

---

<a id="2-equipo-y-responsables"></a>

## <img src="https://img.shields.io/badge/02-5600EF?style=for-the-badge" height="20" align="absmiddle" /> EQUIPO Y RESPONSABLES

Desarrollado y mantenido por el **Equipo `Restrepo`** (prefijo `RST`) para la asignatura DOSW 2026.

<div align="center">

| Rol | Nombre Completo | Usuario GitHub |
| :---: | :---: | :---: |
| **Líder y apoyo** | `Juan Nicolás Álvarez Muñoz` | [@JUANNICOLASA](https://github.com/JUANNICOLASA) |
| **Backend Developer** | `Daniel Mosquera` | [@Danielmmartinez](https://github.com/Danielmmartinez) |
| **Backend Developer** | `Sebastián Camilo Granados` | [@Sebastian-Granados1456](https://github.com/Sebastian-Granados1456) |
| **Frontend Developer** | `Carlos Sánchez` | `Pendiente` |

</div>

---

<a id="3-arquitectura-hexagonal-con-su-diagrama"></a>

## <img src="https://img.shields.io/badge/03-5600EF?style=for-the-badge" height="20" align="absmiddle" /> ARQUITECTURA HEXAGONAL CON SU DIAGRAMA

El servicio sigue la arquitectura hexagonal acordada por el curso. Las reglas del negocio se mantienen aisladas de las herramientas externas, como la base de datos, el broker de mensajería o la API REST, de modo que estas pueden cambiar sin afectar la lógica principal.

### Diagrama Interactivo de Capas (Mermaid)

```mermaid
flowchart TD
    GW["API Gateway"]
    BRK["RabbitMQ"]

    subgraph INFRA_IN["Infraestructura de Entrada (Driving Adapters)"]
        REST["Adaptador REST"]
        AMQP_IN["Adaptador de eventos"]
    end

    subgraph APP["Capa de Aplicación"]
        IN_PORT["Puertos de entrada"]
        USE_CASES["Casos de uso"]
        OUT_PORT["Puertos de salida"]
    end

    subgraph DOMAIN["Núcleo de Dominio"]
        ENTITIES["Modelo de dominio"]
        EVENTS["Eventos de dominio"]
        EXCEPTIONS["Excepciones de dominio"]
    end

    subgraph INFRA_OUT["Infraestructura de Salida (Driven Adapters)"]
        JPA_REPO["Adaptador de persistencia"]
        RABBIT_PUB["Adaptador de mensajería"]
    end

    DB[("PostgreSQL")]

    GW --> REST
    BRK --> AMQP_IN
    REST --> IN_PORT
    AMQP_IN --> IN_PORT
    IN_PORT --> USE_CASES
    USE_CASES --> DOMAIN
    USE_CASES --> OUT_PORT
    OUT_PORT --> JPA_REPO
    OUT_PORT --> RABBIT_PUB
    JPA_REPO --> DB
    RABBIT_PUB --> BRK

    classDef domain fill:#EDE7FF,stroke:#5600EF,stroke-width:2px,color:#0C0C12;
    classDef app fill:#F8FAFC,stroke:#C4B5FD,stroke-width:2px,color:#0C0C12;
    classDef infra fill:#12063A,stroke:#5600EF,stroke-width:1.5px,color:#FFFFFF;

    class DOMAIN,ENTITIES,EVENTS,EXCEPTIONS domain;
    class APP,IN_PORT,USE_CASES,OUT_PORT app;
    class INFRA_IN,INFRA_OUT,REST,AMQP_IN,JPA_REPO,RABBIT_PUB infra;
```

### Diagrama de Componentes Específico

<div align="center">
  <table width="100%" style="margin: 18px 0; border: 2px dashed #C4B5FD; border-radius: 8px; background-color: #FBFBFE;">
    <tr>
      <td align="center" style="padding: 20px;">
        <p>
          <img src="https://img.shields.io/badge/DIAGRAMA-COMPONENTES-12063A?style=for-the-badge" alt="Diagrama de componentes" /><br />
          <strong><font color="#5600EF" size="3">DIAGRAMA DE COMPONENTES ESPECÍFICO (HEXAGONAL)</font></strong>
        </p>
        <p>
          <img src="docs/evidencias/diagrama-componentes.png" width="100%" alt="Diagrama de componentes" /><br />
        </p>
      </td>
    </tr>
  </table>
</div>

El diagrama muestra el servicio en arquitectura hexagonal. Las peticiones llegan desde el API Gateway a los controllers REST (adaptadores de entrada), que usan un mapper de entrada para convertir la petición al modelo de dominio y solo conocen la interfaz del caso de uso (puerto de entrada); los eventos de otros servicios llegan por RabbitMQ al `CatalogoEventListener`. Las implementaciones de los casos de uso (`<Nombre>Impl`) usan los puertos de salida: `PrendaRepositoryPort` para la base de datos (con un mapper de salida hacia la entidad JPA), `EventPublisherPort` para publicar `NuevaPrendaCreadaEvent` y `GeolocalizacionClientPort` para obtener distancias de `ms-geolocalizacion` al ordenar resultados (SR-RF-14). Los flujos cubren búsqueda y filtros (SR-RF-07 a 16), catálogo de almacén (SR-RF-19, 20 y 22), gestión de prendas (SR-RF-50 a 58), moderación (SR-RF-61 y 87) y la actualización de disponibilidad por eventos.

### Diagrama de Clases

<div align="center">
  <table width="100%" style="margin: 18px 0; border: 2px dashed #C4B5FD; border-radius: 8px; background-color: #FBFBFE;">
    <tr>
      <td align="center" style="padding: 20px;">
        <p>
          <img src="https://img.shields.io/badge/DIAGRAMA-CLASES-12063A?style=for-the-badge" alt="Diagrama de clases" /><br />
          <strong><font color="#5600EF" size="3">DIAGRAMA DE CLASES DEL DOMINIO</font></strong>
        </p>
        <p>
          <img src="docs/evidencias/diagrama-clases.png" width="100%" alt="Diagrama de clases" /><br />
        </p>
      </td>
    </tr>
  </table>
</div>

El agregado principal es `Prenda`, que agrupa sus atributos de búsqueda (tipo, color, marca y estilo), su `Precio`, sus `Fotografia` y su `EstadoPublicacion`. Los métodos del agregado cubren editar, retirar, aprobar, rechazar y marcar como agotada; cada aprobación o rechazo queda como una `Moderacion` con su motivo (SR-RF-61 y 87). `AlmacenResumen` guarda la reputación y el estado del almacén para ordenar por reputación (SR-RF-15) y ocultar los almacenes suspendidos. `FiltroBusqueda` reúne los criterios de búsqueda y el `Ordenamiento` (por distancia o reputación). Al publicar una prenda se genera el evento de dominio `NuevaPrendaCreadaEvent`.

### Diagrama de Base de Datos

<div align="center">
  <table width="100%" style="margin: 18px 0; border: 2px dashed #C4B5FD; border-radius: 8px; background-color: #FBFBFE;">
    <tr>
      <td align="center" style="padding: 20px;">
        <p>
          <img src="https://img.shields.io/badge/DIAGRAMA-BASE_DE_DATOS-12063A?style=for-the-badge" alt="Diagrama de base de datos" /><br />
          <strong><font color="#5600EF" size="3">MODELO DE DATOS (POSTGRESQL)</font></strong>
        </p>
        <p>
          <img src="docs/evidencias/diagrama-base-datos.png" width="100%" alt="Diagrama de base de datos" /><br />
        </p>
      </td>
    </tr>
  </table>
</div>

Se usa PostgreSQL porque las prendas tienen atributos fijos sobre los que se filtra y ordena. La tabla `prenda` guarda los atributos de búsqueda, el precio, el estado de publicación y la disponibilidad; `fotografia_prenda` guarda las fotos de cada prenda y `moderacion_publicacion` el historial de aprobaciones y rechazos, ambas en relación uno a muchos con `prenda`. `almacen_resumen` es una copia local de los datos del almacén que necesita el catálogo (nombre, reputación y si está activo), alimentada por eventos, ya que los almacenes pertenecen a otro servicio (*Database-per-Service*). `prenda.disponible` y `almacen_resumen.activo` se actualizan con los eventos `PrendaAgotadaEvent` y `SuscripcionSuspendidaEvent`.

### Estructura de Paquetes en `src/main/java/`

```text
src/main/java/edu/dosw/styleradar/itemcatalogo/
├── domain/
│   ├── model/                       <-- Entidades y agregados
│   ├── event/                       <-- Eventos de dominio
│   ├── exception/                   <-- Excepciones de negocio
│   └── port/
│       ├── in/                      <-- Puertos de entrada (casos de uso)
│       └── out/                     <-- Puertos de salida (repositorios, publicadores)
├── application/
│   ├── usecase/                     <-- Implementación de casos de uso (Impl)
│   ├── service/                     <-- Servicios de aplicación
│   │   └── impl/
│   ├── dto/
│   │   ├── request/
│   │   ├── response/
│   │   └── event/
│   └── mapper/
│       ├── in/
│       └── out/
└── infrastructure/
    ├── adapter/
    │   ├── in/
    │   │   ├── rest/                <-- Controllers REST
    │   │   │   └── doc/             <-- Interfaces de documentación OpenAPI
    │   │   └── amqp/                <-- Consumidores RabbitMQ
    │   └── out/
    │       ├── persistence/         <-- Entidades JPA y repositorios
    │       │   ├── entity/
    │       │   └── repository/
    │       ├── messaging/           <-- Publicadores RabbitMQ
    │       ├── external/            <-- Clientes de otros servicios
    │       └── security/
    ├── config/                      <-- Configuración Spring y OpenAPI
    └── exception/                   <-- Manejo global de errores (RFC 7807)
```

---

<a id="4-endpoints-y-enlace-a-swagger"></a>

## <img src="https://img.shields.io/badge/04-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 4. ENDPOINTS Y ENLACE A SWAGGER

### Enlaces por Entorno

| Entorno | Estado | URL Base del Servicio | Swagger OpenAPI UI | Health Check |
| :--- | :---: | :--- | :--- | :--- |
| **Desarrollo Local (DEV)** | Local | `http://localhost:8085/api/v1` | [Swagger UI Local](http://localhost:8085/api/v1/swagger-ui.html) | [`/health`](http://localhost:8085/api/v1/health) |

* **Especificación OpenAPI:** `http://localhost:8085/api/v1/v3/api-docs`

<div align="center">
  <table width="100%" style="margin: 18px 0; border: 2px dashed #5600EF; border-radius: 8px; background-color: #FBFBFE;">
    <tr>
      <td align="center" style="padding: 22px;">
        <p>
          <img src="https://img.shields.io/badge/ESPACIO_DE_CAPTURA-EVIDENCIA_01-5600EF?style=for-the-badge" alt="Evidencia 01" /><br />
          <strong><font color="#5600EF" size="3">DOCUMENTACIÓN SWAGGER OPENAPI UI ACTIVA</font></strong>
        </p>
        <p>
<img src="docs/evidencias/swagger-ui.png" width="100%" alt="Evidencia Swagger UI" /><br />
        </p>
      </td>
    </tr>
  </table>
</div>

### Tabla de Endpoints Expuestos

|  Método  | Endpoint          | Descripción         | Rol Requerido | Códigos HTTP |
| :------: | :---------------- | :------------------ | :------------ | :----------: |
|  `GET`   | `/api/v1/health`  | Estado del servicio | Público       |    `200`     |

### Funcionalidades que Cubrirá el Servicio

| Requerimiento | Funcionalidad |
| :--- | :--- |
| `SR-RF-07` | Buscar prendas mediante texto libre |
| `SR-RF-08` a `SR-RF-13` | Filtrar prendas por tipo, color, talla, precio, marca y estilo |
| `SR-RF-14` y `SR-RF-15` | Ordenar resultados por distancia y por reputación del almacén |
| `SR-RF-16` | Consultar prendas similares cuando no exista coincidencia exacta |
| `SR-RF-19`, `SR-RF-20` y `SR-RF-22` | Catálogo resumido, novedades semanales y catálogo de almacén |
| `SR-RF-50`, `SR-RF-51`, `SR-RF-53` a `SR-RF-55` y `SR-RF-58` | Publicar, editar y retirar prendas |
| `SR-RF-61` y `SR-RF-87` | Moderar publicaciones del catálogo |

---

<a id="5-eventos-que-publica-y-consume"></a>

## <img src="https://img.shields.io/badge/05-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 5. EVENTOS QUE PUBLICA Y CONSUME

La comunicación asíncrona entre microservicios se realiza a través de **RabbitMQ 3.13.7**.

### Matriz de Eventos

| Tipo | Evento | Emisor / Receptor | Propósito |
| :---: | :--- | :--- | :--- |
| **PUBLICA** | `NuevaPrendaCreadaEvent` | `ms-item-catalogo` &rarr; `ms-notificaciones` | Se publicó una prenda nueva en el catálogo. |
| **CONSUME** | `PrendaAgotadaEvent` | `ms-inventario` &rarr; `ms-item-catalogo` | Marca la prenda como no disponible en los resultados. |
| **CONSUME** | `SuscripcionSuspendidaEvent` | `ms-pagos-suscripciones` &rarr; `ms-item-catalogo` | Deja de mostrar el catálogo del almacén suspendido. |

---

<a id="6-tecnologías-y-dependencias"></a>

## <img src="https://img.shields.io/badge/06-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 6. TECNOLOGÍAS Y DEPENDENCIAS

| Capa / Componente | Tecnología | Versión |
| :--- | :--- | :--- |
| **Lenguaje Core** | Java | `21` |
| **Framework** | Spring Boot | `3.3.4` |
| **Base de Datos** | PostgreSQL | `15.8` |
| **Migraciones de BD** | Flyway | Gestionada por Spring Boot |
| **Broker de Mensajería** | RabbitMQ | `3.13.7` |
| **Mapeo de DTOs** | MapStruct | `1.5.5.Final` |
| **Documentación API** | springdoc-openapi | `2.6.0` |
| **Testing Integración** | Testcontainers | Gestionada por Spring Boot |
| **Métricas de Cobertura** | JaCoCo | `0.8.12` |
| **Análisis Estático** | SpotBugs | `4.8.6.4` |

---

<a id="7-cómo-ejecutar-local-y-con-docker"></a>

## <img src="https://img.shields.io/badge/07-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 7. CÓMO EJECUTAR LOCAL Y CON DOCKER

### Prerrequisitos de Entorno

* **JDK:** 21
* **Apache Maven:** 3.9+
* **Docker Engine y Docker Compose**

### Ejecución con Docker

El servicio, junto con su base de datos y RabbitMQ, se levanta con Docker a partir de un archivo `.env` creado desde `.env.example`:

```bash
docker compose up -d --build
```

<div align="center">
  <table width="100%" style="margin: 18px 0; border: 2px dashed #5600EF; border-radius: 8px; background-color: #FBFBFE;">
    <tr>
      <td align="center" style="padding: 22px;">
        <p>
          <img src="https://img.shields.io/badge/ESPACIO_DE_CAPTURA-EVIDENCIA_02-5600EF?style=for-the-badge" alt="Evidencia 02" /><br />
          <strong><font color="#5600EF" size="3">CONTENEDOR OCI DOCKER EN EJECUCIÓN (docker compose ps)</font></strong>
        </p>
        <p>
<img src="docs/evidencias/docker-compose-ps.png" width="100%" alt="Docker Compose PS" /><br />
        </p>
      </td>
    </tr>
  </table>
</div>

---

<a id="8-variables-de-entorno"></a>

## <img src="https://img.shields.io/badge/08-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 8. VARIABLES DE ENTORNO

Las variables se definen en `.env`, que no se versiona. La plantilla `.env.example` indica cuáles se requieren.

| Variable | Descripción | Valor por Defecto |
| :--- | :--- | :--- |
| `SERVER_PORT` | Puerto del servicio | `8085` |
| `DB_HOST` | Host de PostgreSQL | `localhost` |
| `DB_PORT` | Puerto de PostgreSQL | `5432` |
| `DB_NAME` | Nombre de la base de datos | `styleradar_item_catalog` |
| `DB_USER` | Usuario de PostgreSQL | `postgres` |
| `DB_PASSWORD` | Contraseña de PostgreSQL | — |
| `RABBITMQ_HOST` | Host de RabbitMQ | `localhost` |
| `RABBITMQ_PORT` | Puerto de RabbitMQ | `5672` |
| `RABBITMQ_USER` | Usuario de RabbitMQ | `styleradar` |
| `RABBITMQ_PASSWORD` | Contraseña de RabbitMQ | — |

---

<a id="9-pruebas-y-cobertura"></a>

## <img src="https://img.shields.io/badge/09-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 9. PRUEBAS Y COBERTURA (≥ 85%)

Las pruebas unitarias validan el dominio y los casos de uso, y las de integración validan la persistencia y la mensajería. Se exige una cobertura mínima del **85 %**, verificada con **JaCoCo** en cada ejecución del pipeline.

* **Ruta del Reporte HTML:** `target/site/jacoco/index.html`

<div align="center">
  <table width="100%" style="margin: 18px 0; border: 2px dashed #5600EF; border-radius: 8px; background-color: #FBFBFE;">
    <tr>
      <td align="center" style="padding: 22px;">
        <p>
          <img src="https://img.shields.io/badge/ESPACIO_DE_CAPTURA-EVIDENCIA_03-5600EF?style=for-the-badge" alt="Evidencia 03" /><br />
          <strong><font color="#5600EF" size="3">REPORTE CONSOLIDADO DE COBERTURA JACOCO (≥ 85%)</font></strong>
        </p>
        <p>
<!-- <img src="docs/evidencias/cobertura-jacoco.png" width="100%" alt="Reporte JaCoCo" /><br /> -->
  </table>
</div>

### Pirámide de Pruebas

| Nivel de Prueba | Alcance | Tecnologías | Ubicación en el Código |
| :--- | :--- | :--- | :--- |
| **Pruebas Unitarias** | Dominio y casos de uso | JUnit 5, Mockito | `src/test/java/.../domain/`, `src/test/java/.../application/` |
| **Pruebas de Integración** | Persistencia, mensajería y REST | Spring Boot Test, Testcontainers | `src/test/java/.../infrastructure/` |

---

<a id="10-cicd-con-el-badge-del-pipeline"></a>

## <img src="https://img.shields.io/badge/10-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 10. CI/CD CON EL BADGE DEL PIPELINE

[![CI](https://github.com/StyleRadar1/RST-ItemCatalog-Service/actions/workflows/ci.yml/badge.svg?branch=develop)](https://github.com/StyleRadar1/RST-ItemCatalog-Service/actions/workflows/ci.yml)

Cada cambio enviado a `master`, `develop` o a una rama `feature/*`, así como cada pull request, pasa por un pipeline de **GitHub Actions** que compila el proyecto, ejecuta las pruebas, verifica la cobertura, realiza el análisis estático del código y construye la imagen Docker.

### Diagrama del Flujo Automatizado

```mermaid
flowchart LR
    A["Push / PR<br>(GitHub)"] --> B["Compilar<br>(Maven, Java 21)"]
    B --> C["Pruebas y JaCoCo<br>(cobertura ≥ 85%)"]
    C --> D["Análisis estático<br>(SpotBugs)"]
    D --> E["Imagen Docker"]

    classDef step fill:#12063A,stroke:#5600EF,stroke-width:2px,color:#FFFFFF;
    classDef pass fill:#DCFCE7,stroke:#16A34A,stroke-width:2px,color:#0C0C12;

    class A,B,E step;
    class C,D pass;
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
<img src="docs/evidencias/pipeline-ci-cd.png" width="100%" alt="Pipeline CI/CD" /><br />
        </p>
      </td>
    </tr>
  </table>
</div>

---

<a id="11-ramas-y-cómo-contribuir"></a>

## <img src="https://img.shields.io/badge/11-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 11. RAMAS Y CÓMO CONTRIBUIR

### Estructura de Ramas

```text
master                              <-- Versiones estables
└── develop                         <-- Integración del trabajo del equipo
    └── feature/HU-xx-descripcion   <-- Una rama por historia de usuario
```

Los cambios llegan a `develop` y `master` únicamente por pull request, con revisión y el pipeline en verde.

### Checklist para Pull Requests

- [ ] La rama parte de la versión más reciente de `develop`.
- [ ] Todas las pruebas pasan y la cobertura JaCoCo es mayor o igual al 85 %.
- [ ] No se introducen credenciales ni tokens en el código fuente.
- [ ] El PR tiene la aprobación de otro integrante del equipo antes del merge.

---

<a id="12-enlace-a-la-épica-en-jira"></a>

## <img src="https://img.shields.io/badge/12-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 12. ENLACE A LA ÉPICA EN JIRA

* **Épica del Microservicio:** [RES-2 · ms-item-catalogo](https://mail-team-nyjtgqcj.atlassian.net/browse/RES-2)
* **Tablero del Equipo:** [Restrepo-StyleRadar (RES)](https://mail-team-nyjtgqcj.atlassian.net/jira/software/projects/RES/boards/76)

<div align="center">
  <table width="100%" style="margin: 18px 0; border: 2px dashed #5600EF; border-radius: 8px; background-color: #FBFBFE;">
    <tr>
      <td align="center" style="padding: 22px;">
        <p>
          <img src="https://img.shields.io/badge/ESPACIO_DE_CAPTURA-EVIDENCIA_05-5600EF?style=for-the-badge" alt="Evidencia 05" /><br />
          <strong><font color="#5600EF" size="3">TABLERO SCRUM Y SEGUIMIENTO DE LA ÉPICA EN JIRA</font></strong>
        </p>
        <p>
<img src="docs/evidencias/jira-tablero-epica.png" width="100%" alt="Tablero Jira" /><br />
        </p>
      </td>
    </tr>
  </table>
</div>

### Requerimientos Funcionales Asignados

| Código RF | Nombre del Requerimiento | Historia en Jira |
| :---: | :--- | :---: |
| `SR-RF-07` | Buscar prendas mediante texto libre | `RES-21` |
| `SR-RF-08` | Filtrar prendas por tipo | `RES-22` |
| `SR-RF-09` | Filtrar prendas por color | `RES-23` |
| `SR-RF-10` | Filtrar prendas por talla | `RES-24` |
| `SR-RF-11` | Filtrar prendas por rango de precio | `RES-25` |
| `SR-RF-12` | Filtrar prendas por marca | `RES-26` |
| `SR-RF-13` | Filtrar prendas por estilo | `RES-27` |
| `SR-RF-14` | Ordenar resultados por distancia | `RES-28` |
| `SR-RF-15` | Ordenar resultados por reputación del almacén | `RES-29` |
| `SR-RF-16` | Consultar prendas similares | `RES-30` |
| `SR-RF-19` | Consultar catálogo resumido desde el mapa | `RES-31` |
| `SR-RF-20` | Consultar novedades semanales de un almacén | `RES-32` |
| `SR-RF-22` | Consultar catálogo de almacén | `RES-33` |
| `SR-RF-50` | Publicar prenda en el catálogo | `RES-9` |
| `SR-RF-51` | Registrar fotografías de una prenda | `RES-10` |
| `SR-RF-53` | Registrar precio de una prenda | `RES-11` |
| `SR-RF-54` | Registrar atributos de búsqueda | `RES-12` |
| `SR-RF-55` | Editar prenda del catálogo | `RES-13` |
| `SR-RF-58` | Retirar prenda del catálogo | `RES-14` |
| `SR-RF-61` | Aprobar o rechazar publicaciones | `RES-15` |
| `SR-RF-87` | Revisar publicaciones pendientes por grupos | `RES-16` |