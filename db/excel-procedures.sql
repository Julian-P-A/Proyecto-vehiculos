-- Implementación inicial a partir de las firmas Java y las reglas de TrayectoService.
-- No reemplaza procedimientos de instalaciones existentes.
USE PPOOII;
DELIMITER $$

CREATE PROCEDURE CARGAR_DATOS_EXCEL(
    IN p_persona_id VARCHAR(255), IN p_vehiculo_id VARCHAR(255),
    IN p_codigo_ruta VARCHAR(255), IN p_ubicacion VARCHAR(255),
    IN p_orden_parada VARCHAR(255), IN p_login VARCHAR(255), IN p_id_cargue DOUBLE
)
SQL SECURITY INVOKER
BEGIN
    INSERT INTO trayecto_tmp (persona_id, vehiculo_id, codigo_ruta, ubicacion, orden_parada, login, id_cargue)
    VALUES (TRIM(p_persona_id), TRIM(p_vehiculo_id), TRIM(p_codigo_ruta),
            TRIM(p_ubicacion), TRIM(p_orden_parada), TRIM(p_login), p_id_cargue);
END$$

CREATE PROCEDURE VALIDAR_DATOS_CARGUE(IN p_id_cargue DOUBLE)
SQL SECURITY INVOKER
BEGIN
    UPDATE trayecto_tmp t
    SET t.observacion = CASE
        WHEN COALESCE(t.persona_id, '') NOT REGEXP '^[0-9]{1,10}$'
          OR COALESCE(t.vehiculo_id, '') NOT REGEXP '^[0-9]{1,10}$'
          OR COALESCE(t.orden_parada, '') NOT REGEXP '^[0-9]{1,10}$'
            THEN 'Persona, vehiculo y orden deben ser enteros no negativos'
        WHEN CAST(t.persona_id AS UNSIGNED) NOT BETWEEN 1 AND 2147483647
          OR CAST(t.vehiculo_id AS UNSIGNED) NOT BETWEEN 1 AND 2147483647
          OR CAST(t.orden_parada AS UNSIGNED) > 2147483647
            THEN 'Identificador u orden fuera del rango INT'
        WHEN COALESCE(t.codigo_ruta, '') = '' OR COALESCE(t.ubicacion, '') = ''
          OR COALESCE(t.login, '') = '' THEN 'Ruta, ubicacion y login son obligatorios'
        WHEN NOT EXISTS (SELECT 1 FROM personas p WHERE p.id = CAST(t.persona_id AS UNSIGNED)
                         AND p.tipo_persona = 'C') THEN 'Conductor inexistente o tipo de persona invalido'
        WHEN NOT EXISTS (SELECT 1 FROM vehiculos v WHERE v.id = CAST(t.vehiculo_id AS UNSIGNED))
            THEN 'Vehiculo inexistente'
        WHEN NOT EXISTS (SELECT 1 FROM usuarios u WHERE u.login = t.login)
            THEN 'Usuario inexistente'
        WHEN NOT EXISTS (SELECT 1 FROM vehiculo_persona vp
                         WHERE vp.persona_id = CAST(t.persona_id AS UNSIGNED)
                           AND vp.vehiculo_id = CAST(t.vehiculo_id AS UNSIGNED) AND vp.estado = 'PO')
            THEN 'Conductor sin permiso de operacion para el vehiculo'
        WHEN NOT EXISTS (SELECT 1 FROM documento_vehiculo dv
                         WHERE dv.vehiculo_id = CAST(t.vehiculo_id AS UNSIGNED))
          OR EXISTS (SELECT 1 FROM documento_vehiculo dv
                     WHERE dv.vehiculo_id = CAST(t.vehiculo_id AS UNSIGNED)
                       AND UPPER(dv.estado) <> 'HABILITADO')
            THEN 'Vehiculo sin documentos habilitados'
        ELSE NULL END
    WHERE t.id_cargue = p_id_cargue AND t.estado <> 'PROCESADO';

    UPDATE trayecto_tmp SET estado = IF(observacion IS NULL, 'VALIDO', 'ERROR')
    WHERE id_cargue = p_id_cargue AND estado <> 'PROCESADO';
END$$

CREATE PROCEDURE PROCESAR_DATOS_EXCEL(IN p_id_cargue DOUBLE)
SQL SECURITY INVOKER
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;
    -- Bloquea las filas del lote para evitar duplicados si se procesa simultáneamente.
    UPDATE trayecto_tmp SET estado = 'PROCESANDO'
    WHERE id_cargue = p_id_cargue AND estado = 'VALIDO';

    INSERT INTO trayecto (persona_id, vehiculo_id, codigo_ruta, ubicacion, orden_parada, login)
    SELECT CAST(persona_id AS UNSIGNED), CAST(vehiculo_id AS UNSIGNED), codigo_ruta,
           ubicacion, CAST(orden_parada AS UNSIGNED), login
    FROM trayecto_tmp WHERE id_cargue = p_id_cargue AND estado = 'PROCESANDO';

    UPDATE trayecto_tmp SET estado = 'PROCESADO'
    WHERE id_cargue = p_id_cargue AND estado = 'PROCESANDO';
    COMMIT;
END$$

DELIMITER ;
