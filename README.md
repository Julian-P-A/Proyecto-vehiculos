# Proyecto Vehículos

API Spring Boot para gestionar vehículos, personas, documentos y trayectos.

Proyecto universitario antiguo, desarrollado aproximadamente en 2022. Su
estructura sigue los requisitos del curso: no utiliza DTOs porque así se
solicitó y expone las entidades directamente. La configuración de MySQL y JWT
conserva los valores del ejercicio académico. La clave de Google Maps se
configura mediante una variable de entorno.

Requiere JDK 17 o posterior y MySQL. El wrapper descarga Maven; no se necesita
el proyecto Laboratorio1.

## Funciones

- **Vehículos:** registro, consulta, actualización y eliminación; búsquedas por
  placa, tipo de vehículo, código y estado de documentos.
- **Documentos:** catálogo de documentos, creación individual o por lotes,
  asociación a vehículos, fechas de expedición y vencimiento, estados y PDF en Base64.
- **Personas y conductores:** registro y consulta por identificación interna,
  tipo o estado; asociación de conductores a vehículos y gestión de licencias.
- **Usuarios:** creación de cuenta al registrar una persona administrativa de tipo
  `A`, consulta de usuarios, cambio de contraseña y generación de una nueva API key.
- **JWT y API key:** generación de tokens y acceso a las rutas protegidas mediante
  las cabeceras `Authorization` y `x-api-key`.
- **Trayectos:** registro, actualización, eliminación y consulta por ruta,
  placa del vehículo o identificación del conductor.
- **Carga de Excel:** lectura de una hoja, validación y procesamiento de filas
  mediante procedimientos MySQL, y consulta del detalle del cargue.
- **Google Maps:** consulta de coordenadas para las ubicaciones de los trayectos.
- **Tareas programadas:** actualización de coordenadas cada 90 segundos y revisión
  de vencimientos de licencias y documentos cada dos minutos. Una licencia vencida
  cambia la relación conductor–vehículo a `RO`; un documento vencido pasa a `VENCIDO`.
- **Swagger/OpenAPI:** descripción de operaciones, modelos y parámetros de la API.

### Códigos usados por el proyecto

| Concepto | Valores |
| --- | --- |
| Tipo de persona | `A`: administrativa; `C`: conductor |
| Estado de conductor en un vehículo | `EA`: estado inicial de asociación; `PO`: puede operar; `RO`: restricción para operar |
| Estado de documento de vehículo | `EN_VERIFICACION`, `HABILITADO`, `VENCIDO` |
| Aplicación del documento | `A`: automóvil/vehículo; `M`: moto; `AM`: ambos |

Para crear o actualizar un trayecto, el servicio exige que exista el conductor y
el vehículo, que su asociación esté en `PO` y que el vehículo tenga documentos
registrados y habilitados.

## Árbol del proyecto

```text
Proyecto-Vehiculos/
├── README.md
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .env.example
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── db/
│   ├── schema.sql
│   └── excel-procedures.sql
└── src/
    ├── main/
    │   ├── java/com/ppooii/proyecto_vehiculos/
    │   │   ├── ProyectoVehiculosApplication.java
    │   │   ├── APIs/
    │   │   │   └── GoogleMaps/
    │   │   │       └── Geocoder.java
    │   │   ├── component/
    │   │   │   └── ScheduledTask.java
    │   │   ├── config/             # JWT, seguridad y planificador
    │   │   │   └── model/          # Constants, JwtRequest y JwtResponse
    │   │   ├── controller/         # Endpoints REST
    │   │   ├── entities/           # Entidades JPA y claves compuestas
    │   │   ├── repository/         # Spring Data JPA y llamadas a procedimientos
    │   │   └── services/           # Reglas de negocio y lectura de Excel
    │   │       └── interfaces/     # Contratos de los servicios
    │   └── resources/
    │       ├── application.properties
    │       └── application-dev.properties
    └── test/java/com/ppooii/proyecto_vehiculos/
        ├── ProyectoVehiculosApplicationTests.java
        └── config/
            └── GoogleMapsConfigurationTests.java
```

Las peticiones pasan de `controller` a `services` y después a `repository`.
Las entidades de `entities` representan los datos persistidos en MySQL. Las
operaciones especiales reciben mapas JSON y los CRUD reciben entidades directamente,
según el requisito académico de trabajar sin una capa de DTOs.

## Instalar una base nueva

Con un usuario de instalación con permisos para crear bases, tablas y rutinas:

```sh
mysql -u usuario_instalacion -p < db/schema.sql
mysql -u usuario_instalacion -p < db/excel-procedures.sql
```

Los scripts crean `PPOOII`, las siete tablas de entidades, `trayecto_tmp` y los
tres procedimientos usados por el cargue de Excel. Ejecutarlos una sola vez en
una base nueva. Para una base existente, respaldar y comparar el esquema antes
de aplicar cambios; estos scripts no son migraciones.

Los procedimientos son una implementación inicial reconstruida desde las firmas
Java y las validaciones de `TrayectoService`; no se dispone de los originales.
Validan conductores, vehículos, usuarios, permiso `PO` y documentos habilitados.
Las filas inválidas quedan con estado `ERROR` y una observación; las válidas se
insertan una vez y quedan `PROCESADO`. El identificador de cargue y los tipos
escalares `INT` conservan el contrato actual de Java. El cargador actual identifica
lotes por minuto: evitar subir dos archivos durante el mismo minuto.

Crear una cuenta MySQL propia para la aplicación y conceder `SELECT`, `INSERT`,
`UPDATE`, `DELETE` y `EXECUTE` sobre `PPOOII.*`. Las rutinas se ejecutan con los
permisos de esa cuenta (`SQL SECURITY INVOKER`). No se incluyen usuarios,
contraseñas ni datos personales de ejemplo.

## Configuración y ejecución

Copiar `.env.example` a `.env` y completar los valores localmente:

```sh
cp .env.example .env
```

- `GOOGLE_MAPS_API_KEY`: clave nueva de Google Maps con acceso a la API utilizada.

La conexión MySQL está en `src/main/resources/application-dev.properties`;
ajustarla a la instancia local si es necesario. El secreto JWT permanece en
`Constants.java`, como en el ejercicio original.

Spring Boot no lee `.env` automáticamente. Exportar sus variables en la terminal
que ejecutará Maven, o configurarlas en Eclipse o en el gestor de secretos del
despliegue. Para cargar un `.env` local de confianza en zsh/bash:

```sh
set -a
source .env
set +a
./mvnw spring-boot:run
```

El perfil predeterminado es `dev`, puerto `9002`. Base de la API:
`http://localhost:9002/proyecto-vehiculos/api`; Swagger: `/swagger-ui.html`.
La aplicación exige configurar `GOOGLE_MAPS_API_KEY` para arrancar.

## Endpoints

Base local para todas las rutas de las tablas:

```text
http://localhost:9002/proyecto-vehiculos/api
```

Las peticiones con cuerpo usan JSON, salvo la carga de Excel, que usa
`multipart/form-data`. En las actualizaciones que reciben una entidad se incluye
su `id` dentro del JSON. Las fechas se envían como `YYYY-MM-DD` y los archivos PDF
como una cadena Base64, sin el prefijo `data:application/pdf;base64,`.

### Autenticación y cabeceras

| Método | Ruta | Función / datos de entrada |
| --- | --- | --- |
| POST | `/authenticate` | Generar un token para un usuario existente. JSON: `username`, `password`. Respuesta: `token`, cuyo valor incluye el prefijo `Bearer `. |

Para una ruta protegida, enviar ambas cabeceras:

```http
Authorization: Bearer <token-jwt>
x-api-key: <api-key-del-usuario>
```

La configuración permite consultar sin estas cabeceras:

- `GET /vehiculos/placa/{placa}`.
- `GET /vehiculos/documento/estado/VENCIDO`.
- `GET /vehiculos/documento/por-vencer?dias=30`.
- `GET /personas/tipo/{tipoPersona}`.
- `GET /personas/estado/PO`.

`POST /authenticate`, las rutas `/swagger-ui/**` y `/api-docs/**`, y las peticiones
`OPTIONS` también están permitidas. Las demás operaciones requieren autenticación.
Si se envía una cabecera Bearer incluso a una ruta pública, el filtro comprueba
el token y exige también `x-api-key`.

### Vehículos

| Método | Ruta | Función / datos de entrada |
| --- | --- | --- |
| GET | `/vehiculos` | Listar vehículos. |
| GET | `/vehiculos/{id}` | Consultar por ID interno. |
| GET | `/vehiculos/placa/{placa}` | Consultar por placa. |
| GET | `/vehiculos/tipo/{tipoVehiculo}` | Filtrar por tipo de vehículo. |
| GET | `/vehiculos/documento/codigo/{codigo}` | Filtrar por código del documento. |
| GET | `/vehiculos/documento/estado/{estado}` | Filtrar por estado del documento. |
| GET | `/vehiculos/documento/por-vencer` | Consultar documentos próximos a vencer; parámetro obligatorio `dias`. |
| POST | `/vehiculos` | Registrar una entidad `Vehiculos`. |
| PUT | `/vehiculos` | Actualizar una entidad `Vehiculos`, incluyendo `id`. |
| PUT | `/vehiculos/documento` | Asociar documento. JSON: `idVehiculo`, `idDocumento`, `fechaExp`, `fechaVenc`. |
| PUT | `/vehiculos/documento/estado` | Cambiar estado. JSON: `idVehiculo`, `idDocumentoVehiculo`, `nuevoEstado`. |
| PUT | `/vehiculos/documento/pdf` | Adjuntar PDF. JSON: `idVehiculo`, `idDocumentoVehiculo`, `archivoPdf`. |
| DELETE | `/vehiculos/{id}` | Eliminar por ID. |

### Personas y conductores

| Método | Ruta | Función / datos de entrada |
| --- | --- | --- |
| GET | `/personas` | Listar personas. |
| GET | `/personas/{id}` | Consultar por ID interno. |
| GET | `/personas/estado/{estado}` | Filtrar por estado de la relación conductor–vehículo: `EA`, `PO` o `RO`. |
| GET | `/personas/tipo/{tipoPersona}` | Filtrar por `A` o `C`. |
| POST | `/personas` | Registrar una entidad `Personas`; el tipo `A` también crea un usuario. |
| PUT | `/personas` | Actualizar una entidad `Personas`, incluyendo `id`. |
| PUT | `/personas/conductor` | Asociar conductor. JSON: `vehiculoId`, `personaId`, `fechaAso`. |
| PUT | `/personas/conductor/licencia` | Actualizar licencia. JSON: `idPersona`, `fechaVenc`, `archivoPdf`. |
| PUT | `/personas/conductor/estado` | Cambiar estado. JSON: `idPersona`, `idVehiculo`, `nuevoEstado`. |
| DELETE | `/personas/{id}` | Eliminar por ID interno. |

En `/personas/conductor/licencia`, `idPersona` es la identificación de la persona
almacenada en `ID_PERSONA`. En las asociaciones y cambios de estado se usa el ID
interno de la entidad.

### Catálogo de documentos

| Método | Ruta | Función / datos de entrada |
| --- | --- | --- |
| GET | `/documentos` | Listar el catálogo de documentos. |
| GET | `/documentos/{id}` | Consultar por ID. |
| GET | `/documentos/codigo/{codigo}` | Consultar por código. |
| POST | `/documentos` | Registrar una entidad `Documentos`. |
| POST | `/documentos/list` | Registrar un arreglo JSON de entidades `Documentos`. |
| PUT | `/documentos` | Actualizar una entidad `Documentos`, incluyendo `id`. |
| DELETE | `/documentos/{id}` | Eliminar por ID. |

### Trayectos

| Método | Ruta | Función / datos de entrada |
| --- | --- | --- |
| GET | `/trayectos` | Listar trayectos. |
| GET | `/trayectos/{codigoRuta}` | Listar trayectos de una ruta. |
| GET | `/trayectos/conductor/{idPersona}` | Obtener códigos de ruta por identificación `ID_PERSONA` del conductor. |
| GET | `/trayectos/vehiculo/{placa}` | Listar trayectos por placa. |
| POST | `/trayectos` | Registrar una entidad `Trayecto`. |
| PUT | `/trayectos` | Actualizar una entidad `Trayecto`, incluyendo `id`. |
| DELETE | `/trayectos/{id}` | Eliminar por ID interno. |

### Usuarios

| Método | Ruta | Función / datos de entrada |
| --- | --- | --- |
| GET | `/usuarios` | Listar usuarios. |
| GET | `/usuarios/apikey` | Generar una nueva API key. Recibe un cuerpo JSON `UsuarioPK`: `login`, `persona`. |
| PUT | `/usuarios/{login}` | Cambiar contraseña. JSON: `password`. |

`GET /usuarios/apikey` conserva el contrato original con cuerpo JSON, aunque
algunos clientes y la interfaz de Swagger no admiten cuerpo en una petición GET.
Para esa operación se puede utilizar `curl` o Postman.

### Carga de Excel

| Método | Ruta | Función / datos de entrada |
| --- | --- | --- |
| POST | `/uploadExcel` | Subir un archivo Excel como campo multipart `file`. |
| GET | `/cargue/{idCargue}` | Consultar filas del cargue, estado y observación de validación. |

Se lee la primera hoja del archivo y se omite la primera fila, destinada a
encabezados. Las columnas deben seguir este orden:

| Columna | Dato | Tipo de celda esperado |
| --- | --- | --- |
| A | ID interno de la persona conductora | Numérico entero |
| B | ID interno del vehículo | Numérico entero |
| C | Código de ruta | Numérico entero |
| D | Ubicación o dirección | Texto |
| E | Orden de parada | Numérico entero |
| F | Login del usuario | Texto |

El cargador genera `idCargue` con el formato `yyyyMMddHHmm`, por ejemplo
`202211011430`. El detalle devuelve `ID_TRAYECTO_TMP`, `CODIGO_RUTA`,
`ORDEN_PARADA`, `UBICACION`, `ESTADO` y `OBSERVACION`. El mensaje de subida no
sustituye la consulta del detalle para comprobar la validación de cada fila.

### Ejemplos de uso

Consultar vehículos con documentos próximos a vencer, sin autenticación:

```sh
curl 'http://localhost:9002/proyecto-vehiculos/api/vehiculos/documento/por-vencer?dias=30'
```

Solicitar un token con los datos de un usuario existente:

```sh
curl -X POST 'http://localhost:9002/proyecto-vehiculos/api/authenticate' \
  -H 'Content-Type: application/json' \
  -d '{"username":"USUARIO_EXISTENTE","password":"CONTRASENA_DEL_USUARIO"}'
```

Consultar una ruta protegida o cargar un archivo, sustituyendo los marcadores:

```sh
curl 'http://localhost:9002/proyecto-vehiculos/api/trayectos' \
  -H 'Authorization: Bearer <token-jwt>' \
  -H 'x-api-key: <api-key-del-usuario>'

curl -X POST 'http://localhost:9002/proyecto-vehiculos/api/uploadExcel' \
  -H 'Authorization: Bearer <token-jwt>' \
  -H 'x-api-key: <api-key-del-usuario>' \
  -F 'file=@trayectos.xlsx'
```

## Swagger y OpenAPI

Con la aplicación arrancada en el perfil `dev`:

| Recurso | URL local |
| --- | --- |
| Interfaz Swagger UI | <http://localhost:9002/proyecto-vehiculos/api/swagger-ui/index.html> |
| Ruta de entrada configurada | <http://localhost:9002/proyecto-vehiculos/api/swagger-ui.html> |
| Documento OpenAPI en JSON | <http://localhost:9002/proyecto-vehiculos/api/api-docs> |
| Configuración de Swagger UI | <http://localhost:9002/proyecto-vehiculos/api/api-docs/swagger-config> |

Abrir preferentemente `/swagger-ui/index.html`: está cubierta por la regla pública
`/swagger-ui/**`. La entrada `/swagger-ui.html` no tiene una excepción propia en
la configuración de seguridad original y puede exigir autenticación antes de redirigir.

Las operaciones se agrupan en Vehículos, Personas, Documentos, Trayectos, Usuarios,
Carga Excel y Autenticación JWT. Al desplegar una operación se muestran sus
parámetros y modelos; **Try it out** permite enviar las peticiones públicas.

El proyecto documenta las operaciones con anotaciones `@Tag`, `@Operation` y
`@ApiResponse`, pero no declara esquemas OpenAPI de seguridad para JWT ni API key.
Por ello la interfaz no ofrece una configuración global de ambas cabeceras mediante
**Authorize**. Para probar las operaciones protegidas con el contrato actual,
usar los ejemplos de `curl` o un cliente como Postman.

Las rutas se configuran en `application-dev.properties` mediante
`springdoc.swagger-ui.path`, `springdoc.api-docs.path`,
`springdoc.swagger-ui.enabled` y `springdoc.api-docs.enabled`.

## Verificación

Con la clave de Google Maps exportada y MySQL inicializado y configurado:

```sh
SPRING_JPA_HIBERNATE_DDL_AUTO=validate ./mvnw clean verify
```

La prueba de contexto utiliza MySQL y valida las entidades contra el esquema.
Para comprobar también las llamadas Java a los tres procedimientos en una base
de pruebas inicializada con estos scripts, añadir `VERIFY_DATABASE_BOOTSTRAP=true`
al comando anterior. Esa prueba inserta una fila inválida en `trayecto_tmp` y la
elimina al terminar.
Las pruebas enfocadas de configuración de Google Maps pueden ejecutarse sin BD
ni acceso a Google:

```sh
./mvnw -Dtest=GoogleMapsConfigurationTests test
```

Para generar el JAR sin arrancar el contexto ni necesitar una BD:

```sh
./mvnw -DskipTests package
```

