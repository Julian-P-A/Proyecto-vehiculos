package com.ppooii.proyecto_vehiculos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppooii.proyecto_vehiculos.entities.VehiculoPersona;
import com.ppooii.proyecto_vehiculos.entities.Vehiculos;

@Repository("IVehiculoPersonaRepo")
public interface VehiculoPersonaRepository extends JpaRepository<VehiculoPersona, Long> {
	
	public abstract Optional<VehiculoPersona> findByVehiculo_IdAndPersona_Id(Long idVehiculo, Long idPersona);
	
	public abstract List<VehiculoPersona> findByPersona_Id(Long idPersona);

}
