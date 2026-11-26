## <img src="https://img.shields.io/badge/11-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 11. SEGURIDAD, HABEAS DATA Y ANÁLISIS OWASP TOP 10

`ms-item-catalogo` maneja el catálogo de prendas de los almacenes. No guarda datos personales de compradores: solo ids de almacén (UUID), el id numérico del usuario que administra el almacén y el UUID del moderador, derivado del id del usuario autenticado. Las fotografías son de prendas, no de personas.

Esta sección distingue lo que **está implementado** de lo que **queda como recomendación**. No es una certificación OWASP.

### Habeas Data (Ley 1581 de 2012)

| Aspecto | Implementado |
| :--- | :--- |
| Datos que se guardan | Prendas, almacenes (nombre, NIT, reputación, estado) y el id numérico del administrador del almacén. Ningún nombre, correo, documento ni teléfono de personas. |
| Eventos | Los eventos de RabbitMQ solo llevan ids (prenda, almacén y `propietarioUsuarioId`) y datos de la prenda; ningún dato de contacto. |
| Logs | Los logs registran ids técnicos y el motivo de la falla, nunca el contenido de los tokens ni datos de contacto. |
| Retención | Una prenda retirada queda con estado `RETIRADA` y deja de mostrarse; no se borra para conservar la trazabilidad de la moderación. |

### Controles frente a OWASP Top 10:2025

| Riesgo | Control implementado | Pendiente o recomendación |
| :--- | :--- | :--- |
| **A01 Broken Access Control** | Publicar, editar y retirar exigen ser el almacén dueño o `STYLERADAR_ADMIN` (403 si no); moderar exige `STYLERADAR_ADMIN`. El rol y el usuario llegan en las cabeceras `X-User-*` que el API Gateway arma desde el JWT validado. | El servicio confía en esas cabeceras: restringir el ingreso de Azure Container Apps para que solo el Gateway pueda llamarlo, o validar el JWT también aquí. |
| **A02 Security Misconfiguration** | Actuator solo expone `health` e `info`. El contenedor corre con un usuario sin privilegios (`USER app`). Cada ambiente tiene su propia configuración por variables de entorno. | Agregar cabeceras de seguridad HTTP en el Gateway si aún no las envía. |
| **A03 Software Supply Chain Failures** | Versiones fijas en el `pom.xml` y en las imágenes base de Docker; análisis de SonarCloud en cada PR. | Activar Dependabot o un análisis de dependencias vulnerables. |
| **A04 Cryptographic Failures** | RabbitMQ en QA y PROD por el puerto 5671 con TLS y la CA del broker cargada en la imagen. Azure Database for PostgreSQL exige conexiones cifradas. | Ninguno identificado. |
| **A05 Injection** | Acceso a datos con Spring Data JPA y consultas parametrizadas, sin SQL armado con texto. Validación de entrada con Bean Validation (`@NotBlank`, `@Size`, valores permitidos en tipo, color y estilo). | Ninguno identificado. |
| **A06 Insecure Design** | Reglas de negocio en el dominio: máximo 5 fotografías, precio positivo, rechazo con motivo y solo se moderan prendas pendientes (422 si no). | Ninguno identificado. |
| **A07 Authentication Failures** | La autenticación la hace `ms-auth` y el JWT lo valida el API Gateway. | Ver A01: no exponer el servicio sin el Gateway. |
| **A08 Software or Data Integrity Failures** | Migraciones versionadas con Flyway; imágenes construidas solo por el pipeline. Los consumidores de eventos rechazan a la cola de errores (DLQ) los mensajes sin datos válidos. | Ninguno identificado. |
| **A09 Logging and Alerting Failures** | Se registran las fallas de integración (ms-tiendas, RabbitMQ) y los errores inesperados. | Configurar alertas en Azure sobre errores 5xx y mensajes en la DLQ. |
| **A10 Mishandling of Exceptional Conditions** | `GlobalExceptionHandler` responde todos los errores en formato RFC 7807 con un `code`, sin trazas internas (el 500 dice solo "Ocurrió un error inesperado"). Si RabbitMQ falla, la operación se guarda igual y solo se registra un aviso. | Ninguno identificado. |

### Secretos

Las contraseñas de la base de datos y de RabbitMQ, y las credenciales de Azure, están en los *secrets* de GitHub Actions y se pasan como variables de entorno al contenedor. No hay secretos en el código ni en los archivos de configuración del repositorio.

---

<p align="center"><a href="../../README.md">⬅ Volver al índice</a></p>
