-- Esquema inicial para una base nueva. No ejecutar sobre una instalación con datos.
CREATE DATABASE IF NOT EXISTS PPOOII
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE PPOOII;

CREATE TABLE personas (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_persona VARCHAR(255) NOT NULL UNIQUE,
    tipo_id VARCHAR(2) NOT NULL,
    nombres VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255) NOT NULL,
    correo_electronico VARCHAR(255) NOT NULL,
    tipo_persona VARCHAR(1) NOT NULL,
    licencia LONGBLOB,
    fecha_vencimiento DATE
) ENGINE=InnoDB;

CREATE TABLE vehiculos (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tipo_vehiculo VARCHAR(255) NOT NULL,
    placa VARCHAR(6) NOT NULL UNIQUE,
    tipo_servicio VARCHAR(2) NOT NULL,
    tipo_combustible VARCHAR(20) NOT NULL,
    capacidad INT NOT NULL,
    color VARCHAR(20) NOT NULL,
    modelo INT NOT NULL,
    marca VARCHAR(20) NOT NULL,
    linea VARCHAR(20) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE documentos (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    codigo INT NOT NULL UNIQUE,
    nombre_documento VARCHAR(255) NOT NULL,
    tipo_aplica VARCHAR(2) NOT NULL,
    obligatorio VARCHAR(2) NOT NULL,
    descripcion VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE usuarios (
    login VARCHAR(255) NOT NULL,
    persona_id INT NOT NULL,
    password VARCHAR(255) NOT NULL,
    apikey VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (login, persona_id)
) ENGINE=InnoDB;

CREATE TABLE documento_vehiculo (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    vehiculo_id BIGINT NOT NULL,
    documento_id BIGINT NOT NULL,
    fecha_expedicion DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado VARCHAR(255) NOT NULL,
    archivo_pdf LONGBLOB,
    CONSTRAINT fk_documento_vehiculo_vehiculo FOREIGN KEY (vehiculo_id) REFERENCES vehiculos(id),
    CONSTRAINT fk_documento_vehiculo_documento FOREIGN KEY (documento_id) REFERENCES documentos(id)
) ENGINE=InnoDB;

CREATE TABLE vehiculo_persona (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    vehiculo_id BIGINT NOT NULL,
    persona_id BIGINT NOT NULL,
    fecha_asociacion DATE NOT NULL,
    estado VARCHAR(2) NOT NULL,
    CONSTRAINT fk_vehiculo_persona_vehiculo FOREIGN KEY (vehiculo_id) REFERENCES vehiculos(id),
    CONSTRAINT fk_vehiculo_persona_persona FOREIGN KEY (persona_id) REFERENCES personas(id)
) ENGINE=InnoDB;

-- Los identificadores escalares INT corresponden a los tipos Java actuales.
CREATE TABLE trayecto (
    id_trayecto BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    persona_id INT NOT NULL,
    vehiculo_id INT NOT NULL,
    codigo_ruta VARCHAR(255),
    ubicacion VARCHAR(255),
    orden_parada INT,
    longitud DOUBLE,
    latitud DOUBLE,
    login VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE trayecto_tmp (
    id_trayecto_tmp BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    persona_id VARCHAR(255),
    vehiculo_id VARCHAR(255),
    codigo_ruta VARCHAR(255),
    ubicacion VARCHAR(255),
    orden_parada VARCHAR(255),
    login VARCHAR(255),
    id_cargue DOUBLE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    observacion VARCHAR(500),
    INDEX idx_trayecto_tmp_cargue (id_cargue, estado)
) ENGINE=InnoDB;
