package com.ppooii.proyecto_vehiculos.repository;

import org.springframework.stereotype.Repository;
import com.ppooii.proyecto_vehiculos.entities.Trayecto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ppooii.proyecto_vehiculos.entities.Personas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

@Repository("ITrayectoRepo")
public interface TrayectoRepository extends JpaRepository<Trayecto, Integer> {
	
	public abstract List<Trayecto> findAll();
	
	public abstract Optional<Trayecto> findById(Long id);
	
	public abstract List<Trayecto> findByCodigoRuta(String codigoRuta);
	
	@Query("SELECT t FROM Trayecto t JOIN Vehiculos v ON t.vehiculo = v.id WHERE v.placa = :placa")
	List<Trayecto> findByVehiculo_Placa(@Param("placa") String placa);

	@Query("SELECT DISTINCT t.codigoRuta FROM Trayecto t JOIN Personas p ON t.persona = p.id WHERE p.idPersona = :idPersona")
	List<String> findDistinctCodigoRutaByPersonaIdentificacion(@Param("idPersona") String idPersona);
	
	@Query(value = "SELECT t.ID_TRAYECTO_TMP, t.CODIGO_RUTA, t.ORDEN_PARADA, t.UBICACION, t.ESTADO, t.OBSERVACION " + 
            "FROM PPOOII.trayecto_tmp t WHERE t.ID_CARGUE = :idCargue", 
    nativeQuery = true)
	List<Map<String, Object>> consultarDetalleCargueMapeado(@Param("idCargue") Double idCargue);
	
	@Procedure(procedureName = "CARGAR_DATOS_EXCEL")
	public abstract void CARGAR_DATOS_EXCEL(@Param("p_persona_id") String personaId,
			@Param("p_vehiculo_id") String vehiculoId, @Param("p_codigo_ruta") String codigoRuta,
			@Param("p_ubicacion") String ubicacion, @Param("p_orden_parada") String ordenParada,
			@Param("p_login") String login, @Param("p_id_cargue") Double idCargue);
	
	@Procedure(procedureName = "VALIDAR_DATOS_CARGUE")
	public abstract void VALIDAR_DATOS_CARGUE(@Param("p_id_cargue") Double idCargue);
	
	@Procedure(procedureName = "PROCESAR_DATOS_EXCEL")
	public abstract void PROCESAR_DATOS_EXCEL(@Param("p_id_cargue") Double idCargue);
}
