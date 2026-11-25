## <img src="https://img.shields.io/badge/04-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 4. EVENTOS QUE PUBLICA Y CONSUME

La comunicación asíncrona se hace por **RabbitMQ 3.13.7**, siguiendo la guía de mensajería del curso: exchange compartido `styleradar.exchange` (topic, durable) y, en QA y PROD, conexión por el puerto **5671 con TLS** y el usuario asignado al servicio.

### 4.1 Eventos asíncronos (RabbitMQ)

#### Matriz de eventos AMQP

| Tipo | Routing key | Emisor / Receptor | Cuándo |
| :---: | :--- | :--- | :--- |
| **PUBLICA** | `styleradar.catalogo.prenda.publicada` | `ms-item-catalogo` &rarr; `ms-notificaciones`, `ms-probador-virtual`, `ms-estadisticas` | Un administrador aprueba la prenda y queda visible en el catálogo. |
| **PUBLICA** | `styleradar.catalogo.prenda.retirada` | `ms-item-catalogo` &rarr; `ms-notificaciones`, `ms-playlist` | El almacén dueño (o un administrador) retira la prenda. |
| **PUBLICA** | `styleradar.catalogo.moderacion.cambiada` | `ms-item-catalogo` &rarr; `ms-notificaciones` | Un administrador aprueba o rechaza una publicación. |
| **CONSUME** | `styleradar.inventario.actualizado` | `ms-inventario` &rarr; `ms-item-catalogo` | El evento es de una talla, así que el catálogo consulta el stock total de la prenda en `ms-inventario`: si todas las tallas quedaron en 0 se marca como no disponible y deja de mostrarse; si vuelve a tener unidades, reaparece (SR-RF-57). |
| **CONSUME** | `styleradar.inventario.stock.agotado` | `ms-inventario` &rarr; `ms-item-catalogo` | Una talla se agotó: igual que el anterior, la prenda solo se oculta si ya no le queda stock en ninguna talla. |
| **CONSUME** | `styleradar.pagos.suscripcion.suspendida` | `ms-pagos-suscripciones` &rarr; `ms-item-catalogo` | Se suspende el almacén: deja de mostrarse su catálogo y no puede publicar. El almacén se identifica por el `nit` del evento (con la misma regla de UUID); si no viene, se usa `almacenId`. |

#### Colas y errores

| Cola | Para qué |
| :--- | :--- |
| `styleradar.catalogo.inventario-actualizado.q` | Recibe los eventos de inventario (actualizado y stock agotado). |
| `styleradar.catalogo.suscripcion-suspendida.q` | Recibe las suspensiones de suscripción. |
| `styleradar.catalogo.dlq` | Cola de errores: por el DLX `styleradar.dlx` llegan aquí los mensajes que no se pudieron procesar (por ejemplo, sin un id válido). |

Los mensajes que publica el servicio son JSON con `eventoId`, `productor`, `itemId`/`prendaId`, `tiendaId`/`almacenId` y los datos del evento (`nombrePrenda`, `categoria`, `precio`, `imagenUrl` con la primera foto, `motivo`, `estadoModeracion`, fechas). Los ids son UUID. Los nombres del exchange, las colas y el DLX se pueden cambiar por ambiente con variables de entorno. Las fechas van en UTC con formato ISO-8601 terminado en `Z`; los tres eventos incluyen además `fechaEvento`, la fecha que lee `ms-estadisticas` (indicador `PLATFORM_GARMENT_PUBLICATIONS`). En `prenda.publicada` también se envían `category` (categoría que reconoce `ms-probador-virtual`: TOP, BOTTOM, ONE_PIECE, OUTERWEAR, FOOTWEAR o ACCESSORIES) y `availableSizes`/`tallasDisponibles` y `stockTotal` (tallas y unidades con stock en `ms-inventario`, si se conocen al aprobar). `moderacion.cambiada` incluye `propietarioUsuarioId`, el usuario que administra el almacén según `ms-tiendas` (se registra cuando ese usuario publica, edita o retira una prenda).

### 4.2 Llamadas síncronas (HTTP / Feign), no son eventos

Estas integraciones son consultas directas por HTTP con OpenFeign: el servicio espera la respuesta y no pasan por RabbitMQ.

| Servicio | Endpoint | Para qué |
| :--- | :--- | :--- |
| `ms-tiendas` | `GET /api/v1/stores?page&size` | Cargar los almacenes aprobados (NIT, nombre y reputación) 20 segundos después de arrancar y luego cada 30 minutos. `ms-tiendas` no publica eventos de almacenes. |
| `ms-tiendas` | `GET /api/v1/users/{userId}/store` | Saber qué almacén administra el usuario que publica, edita o retira una prenda. Se reenvían el JWT y las cabeceras `X-User-*`. |
| `ms-inventario` | `GET /api/v1/inventory/sizes/{size}/garments` | Filtrar la búsqueda por talla con las prendas que tienen stock en esa talla. |
| `ms-inventario` | `GET /api/v1/inventory/garments/{garmentId}` | Stock total y tallas con stock de una prenda (disponibilidad, guía de tallas y sincronización al recibir un evento de inventario). |
| `ms-geolocalizacion` | `GET /api/v1/stores/{id}/distance?lat&lng` | Distancia al almacén para ordenar la búsqueda por cercanía. |

El id de cada almacén es un UUID fijo calculado desde su NIT, el mismo que usa `ms-geolocalizacion`, para que la distancia y el catálogo de un almacén apunten al mismo registro. Si un almacén fue suspendido por `ms-pagos`, la carga conserva la suspensión.

---

<p align="center"><a href="../../README.md">⬅ Volver al índice</a></p>
