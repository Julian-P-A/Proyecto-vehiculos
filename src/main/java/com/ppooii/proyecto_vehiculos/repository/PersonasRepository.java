package com.ppooii.proyecto_vehiculos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ppooii.proyecto_vehiculos.entities.Personas;
import com.ppooii.proyecto_vehiculos.entities.Vehiculos;

@Repository("IPersonasRepo")
public interface PersonasRepository extends JpaRepository<Personas, Long>{
	
	public abstract List<Personas> findAll();
	
	public abstract Optional<Personas> findById(int id);
	
	public abstract Optional<Personas> findByIdPersona(String idPersona);
    
    public abstract List<Personas> findByNombres(String nombres);
    
	public abstract List<Personas> findByApellidos(String apellidos);
	
	public abstract List<Personas> findByTipoPersona(String tipoPersona);
	
	@Query("SELECT p FROM Personas p JOIN p.vehiculoPersona vp WHERE vp.estado = :estado")
	public abstract List<Personas> findByEstadoConductor(@Param("estado") String estado);

}
