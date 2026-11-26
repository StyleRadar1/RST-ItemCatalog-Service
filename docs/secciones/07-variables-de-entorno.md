## <img src="https://img.shields.io/badge/07-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 7. VARIABLES DE ENTORNO

Las variables se definen en `.env`, que no se versiona. La plantilla `.env.example` indica cuáles se requieren.

| Variable | Descripción | Valor por Defecto |
| :--- | :--- | :--- |
| `SERVER_PORT` | Puerto del servicio | `8220` |
| `DB_HOST` | Host de PostgreSQL | `localhost` |
| `DB_PORT` | Puerto de PostgreSQL | `5432` |
| `DB_NAME` | Nombre de la base de datos | `styleradar_item_catalog` |
| `DB_USER` | Usuario de PostgreSQL | `postgres` |
| `DB_PASSWORD` | Contraseña de PostgreSQL | — |
| `RABBITMQ_HOST` | Host de RabbitMQ | `localhost` |
| `RABBITMQ_PORT` | Puerto de RabbitMQ | `5672` |
| `RABBITMQ_USER` | Usuario de RabbitMQ | `styleradar` |
| `RABBITMQ_PASSWORD` | Contraseña de RabbitMQ | — |
| `RABBITMQ_VHOST` | Virtual host de RabbitMQ | `/` |
| `RABBITMQ_SSL_ENABLED` | Conexión TLS (en QA y PROD `true`, puerto `5671`) | `false` |
| `RABBITMQ_EXCHANGE` | Exchange compartido | `styleradar.exchange` |
| `RABBITMQ_DLX` | Exchange de errores | `styleradar.dlx` |
| `RABBITMQ_DLQ` | Cola de errores del servicio | `styleradar.catalogo.dlq` |
| `TIENDAS_URL` | URL de `ms-tiendas` con `/api/v1` | `http://localhost:8140/api/v1` (QA y PROD: `styleradar-tiendas-<ambiente>.azurewebsites.net/api/v1`) |
| `TIENDAS_CARGA_HABILITADA` | Activa la carga periódica de almacenes desde `ms-tiendas` | `true` |
| `TIENDAS_CARGA_INTERVALO` | Cada cuánto se cargan los almacenes | `PT30M` |

---

* **Datos fijos de QA:** con el perfil `qa`, Flyway también carga `db/qa`. Ahí está la prenda `c3bc58c3-e0e2-43cd-a61a-1279a3894fe7` (Camiseta agotada QA notificaciones) del Almacén Pruebas QA, sin inventario para que su disponibilidad responda `stockTotal` 0; la usa la prueba automática de prenda agotada de ms-notificaciones y no se debe usar en otras pruebas. También están diez prendas aprobadas de prueba, cinco por cada tienda de QA (Almacén Pruebas QA y Almacén Pruebas QA 2), con distintos tipos, estilos y precios y foto en `images.unsplash.com`, uno de los dominios de imágenes que permite el front.

<p align="center"><a href="../../README.md">⬅ Volver al índice</a></p>
