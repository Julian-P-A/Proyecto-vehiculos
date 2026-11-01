# Proyecto Vehículos

API Spring Boot para gestionar vehículos, personas, documentos y trayectos.

Proyecto universitario antiguo, desarrollado aproximadamente en 2022. Su
estructura sigue los requisitos del curso: no utiliza DTOs porque así se
solicitó y expone las entidades directamente. La configuración de MySQL y JWT
conserva los valores del ejercicio académico. La clave de Google Maps se
configura mediante una variable de entorno.

Requiere JDK 17 o posterior y MySQL. El wrapper descarga Maven; no se necesita
el proyecto Laboratorio1.

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

