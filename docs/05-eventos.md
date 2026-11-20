## <img src="https://img.shields.io/badge/05-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 5. EVENTOS QUE PUBLICA Y CONSUME

La comunicación asíncrona se hace por **RabbitMQ 3.13.7**, siguiendo la guía de mensajería del curso: exchange compartido `styleradar.exchange` (topic, durable) y, en QA y PROD, conexión por el puerto **5671 con TLS** y el usuario asignado al servicio.

### Matriz de Eventos AMQP

| Tipo | Routing key | Emisor / Receptor | Cuándo |
| :---: | :--- | :--- | :--- |
| **PUBLICA** | `styleradar.catalogo.prenda.publicada` | `ms-item-catalogo` &rarr; `ms-notificaciones`, `ms-probador` | Un administrador aprueba la prenda y queda visible en el catálogo. |
| **PUBLICA** | `styleradar.catalogo.prenda.retirada` | `ms-item-catalogo` &rarr; `ms-notificaciones`, `ms-playlist` | El almacén dueño (o un administrador) retira la prenda. |
| **PUBLICA** | `styleradar.catalogo.moderacion.cambiada` | `ms-item-catalogo` &rarr; `ms-notificaciones` | Un administrador aprueba o rechaza una publicación. |
| **CONSUME** | `styleradar.inventario.actualizado` | `ms-inventario` &rarr; `ms-item-catalogo` | Si el stock llega a 0, la prenda se marca como no disponible y deja de mostrarse. |
| **CONSUME** | `styleradar.inventario.stock.agotado` | `ms-inventario` &rarr; `ms-item-catalogo` | La prenda se agotó: se marca como no disponible. |
| **CONSUME** | `styleradar.pagos.suscripcion.suspendida` | `ms-pagos-suscripciones` &rarr; `ms-item-catalogo` | Se suspende el almacén: deja de mostrarse su catálogo y no puede publicar. |

### Colas y errores

| Cola | Para qué |
| :--- | :--- |
| `styleradar.catalogo.inventario-actualizado.q` | Recibe los eventos de inventario (actualizado y stock agotado). |
| `styleradar.catalogo.suscripcion-suspendida.q` | Recibe las suspensiones de suscripción. |
| `styleradar.catalogo.dlq` | Cola de errores: por el DLX `styleradar.dlx` llegan aquí los mensajes que no se pudieron procesar (por ejemplo, sin un id válido). |

Los mensajes que publica el servicio son JSON con `eventoId`, `productor`, `itemId`/`prendaId`, `tiendaId`/`almacenId` y los datos del evento (`nombrePrenda`, `categoria`, `precio`, `imagenUrl` con la primera foto, `motivo`, `estadoModeracion`, fechas). Los ids son UUID. Los nombres del exchange, las colas y el DLX se pueden cambiar por ambiente con variables de entorno.

---

<p align="center"><a href="../README.md">⬅ Volver al índice</a></p>
