package com.ppooii.proyecto_vehiculos.services.interfaces;

import com.ppooii.proyecto_vehiculos.entities.Personas;

import java.util.List;
import java.util.Optional;

public interface IPersonasService {
	
	public Object addPersona(Personas persona);
	public boolean updatePersona(Personas persona);
	public boolean deletePersona(Long id);
	
	public List<Personas> findAll();
	public Optional<Personas> findById(Long id);
	public List<Personas> findByEstadoConductor(String estado);
	public List<Personas> findByTipoPersona(String tipoPersona);
	
	Optional<Personas> addConductorToVehiculo(Long vehiculoId, Long personaId, String fechaAso);
	public boolean updateEstadoConductor(Long idPersona, Long idVehiculoPersona, String nuevoEstado);
	public boolean addLicencia(String idPersona, String licencia, String fechaLicencia);

}
