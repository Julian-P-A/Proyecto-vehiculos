package com.ppooii.proyecto_vehiculos.services;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ppooii.proyecto_vehiculos.repository.PersonasRepository;
import com.ppooii.proyecto_vehiculos.repository.VehiculosRepository;
import com.ppooii.proyecto_vehiculos.repository.UsuariosRepository;
import com.ppooii.proyecto_vehiculos.repository.VehiculoPersonaRepository;
import com.ppooii.proyecto_vehiculos.entities.DocumentoVehiculo;
import com.ppooii.proyecto_vehiculos.entities.Documentos;
import com.ppooii.proyecto_vehiculos.entities.Personas;
import com.ppooii.proyecto_vehiculos.entities.Usuarios;
import com.ppooii.proyecto_vehiculos.entities.Vehiculos;
import com.ppooii.proyecto_vehiculos.entities.VehiculoPersona;
import com.ppooii.proyecto_vehiculos.entities.UsuarioPK;
import com.ppooii.proyecto_vehiculos.services.interfaces.IPersonasService;

import jakarta.transaction.Transactional;

@Service
public class PersonasService implements IPersonasService {
	@Autowired
	@Qualifier("IPersonasRepo")
	private PersonasRepository personasRepository;
	
	@Autowired
	@Qualifier("IVehiculosRepo")
	private VehiculosRepository vehiculosRepository;
	
	@Autowired
	@Qualifier("IUsuariosRepo")
	private UsuariosRepository usuariosRepository;
	
	@Autowired
	@Qualifier("IVehiculoPersonaRepo")
	private VehiculoPersonaRepository vehiculoPersonaRepository;
	
	//Logs
	//Wrong Logs
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(VehiculosService.class);
	
	public PersonasService(PersonasRepository personasRepository, VehiculosRepository vehiculosRepository, UsuariosRepository usuariosRepository) {
		this.personasRepository = personasRepository;
		this.vehiculosRepository = vehiculosRepository;
		this.usuariosRepository = usuariosRepository;
	}
	
	@Transactional
	@Override
	public Object addPersona(Personas persona) {
		try {
		    if (persona == null) {
		        logger.error("Error: La persona es nulo");
		        return false;
		    }

		    if (persona.getId() != null) {
		        logger.error("Error: Una persona con ese ID ya existe");
		        return false;
		    }
		    
		    personasRepository.save(persona);
		    
		    if (persona.getTipoPersona().equalsIgnoreCase("A")) {
		    	
		    	String login = (persona.getNombres().substring(0,1) + persona.getApellidos().substring(0,1) + persona.getIdPersona()).toUpperCase();
		    	String password = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
		    	String apiKey = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
		    	
		    	UsuarioPK pk = new UsuarioPK(login, persona.getId().intValue());
		    	Usuarios usuario = new Usuarios(pk, password, apiKey);
		    	
		    	usuariosRepository.save(usuario);
		    	
		    	Map<String, Object> response = new HashMap<>();
		    	response.put("resultado", "usuario creado");
	            response.put("persona", persona);
	            response.put("usuario", usuario);
	            return response;
		    }

		    return true;

		} catch (Exception e) {
		    logger.error("Error: No se pudo crear la persona", e);
		    return false;
		}
	}

	@Override
	public boolean updatePersona(Personas persona) {
		try {
	        if (persona == null) {
	            logger.error("Error: La persona es nulA");
	            return false;
	        }

	        if (persona.getId() == null) {
	            logger.error("Error: La persona no tiene ID asignado");
	            return false;
	        }

	        if (persona.getTipoPersona().equalsIgnoreCase("A")) {
	        	logger.error("Error: La persona no se puede actualizar a tipo 'A'");
	            return false;
	        }

	        Personas existente = personasRepository.findById(persona.getId())
	                .orElseThrow(() -> new IllegalArgumentException(
	                        "No se encontró la persona con ID: " + persona.getId())
	                );

	        personasRepository.save(persona);
	        logger.info("Persona actualizado correctamente con ID: " + persona.getId());
	        return true;

	    } catch (Exception e) {
	        logger.error("Error: No se pudo actualizar la persona", e);
	        return false;
	    }
	}

	@Override
	public boolean deletePersona(Long id) {
		
		try {
			if(id > 0) {
				if(personasRepository.findById(id).isPresent()) {
					personasRepository.deleteById(id);
					return true;
				}
				logger.error("Error: La persona con ese id no existe");
				return false;
			}
			logger.error("Error: El id es invalido");
			return false;
		}catch(Exception e) {
			logger.error("Error: Eliminar la persona no fue posible");
			return false;
		}
		
	}

	@Override
	public List<Personas> findAll() {
		return personasRepository.findAll();
	}

	@Override
	public Optional<Personas> findById(Long id) {
		
		try {
			if(id > 0) {
				Optional<Personas> persona = personasRepository.findById(id);
				
				if(!persona.isPresent()) {
					logger.error("Error: No se ha encontrado una persona con ese id");
					return Optional.empty();
				}
				return persona;
			}
			logger.error("Error: El id es invalido");
			return Optional.empty();
		}catch(Exception e) {
			return Optional.empty();
		}
		
	}
	
	@Override
	public List<Personas> findByEstadoConductor(String estado) {
		
		if(estado == null) {
			logger.error("Error: El estado es nulo");
			return Collections.emptyList();
		}
		
		if(estado.isEmpty()) {
			logger.error("Error: El estado es vacio");
			return Collections.emptyList();
		}
		
		try {
			List<Personas> personas = personasRepository.findByEstadoConductor(estado);
			if (personas.isEmpty()) {
	            logger.error("Error: El estado es inválido o no hay personas con ese estado");
	            return Collections.emptyList();
	        }
			return personas;
		}catch(Exception e) {
			logger.error("Error: No se pudieron obtener los vehículos por ese estado de documentos");
	        return Collections.emptyList();
		}
		
	}
	
	@Override
	public List<Personas> findByTipoPersona(String tipoPersona) {
		
		if(tipoPersona == null) {
			logger.error("Error: El tipo de persona es nulo");
			return null;
		}
		
		if(tipoPersona.isEmpty()) {
			logger.error("Error: El tipo de persona es vacio");
			return null;
		}
		
		if(!tipoPersona.equals("A") && !tipoPersona.equals("C")) {
			logger.error("Error: El tipo de persona es inválido");
			return null;
		}
		
		try {
			List<Personas> personas = personasRepository.findByTipoPersona(tipoPersona);
			if (personas.isEmpty()) {
	            logger.error("Error: El tipo de persona es inválido o no hay personas con ese tipo");
	            return null;
	        }
			return personas;
		}catch(Exception e) {
			logger.error("Error: No se pudieron obtener los vehículos por ese tipo de persona");
	        return null;
		}
	}

	@Override
	public Optional<Personas> addConductorToVehiculo(Long vehiculoId, Long personaId, String fechaAso) {
		
		try {
		    Vehiculos vehiculo = vehiculosRepository.findById(vehiculoId)
		            .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado con ID: " + vehiculoId));

		    Personas persona = personasRepository.findById(personaId)
		            .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + personaId));

		    String tipoPersona = persona.getTipoPersona();

		    if (!tipoPersona.equals("C")) {  	
		            throw new IllegalArgumentException("Persona no valida para conducir");
		   }

		    VehiculoPersona vp = new VehiculoPersona();
		    vp.setVehiculo(vehiculo);
		    vp.setPersona(persona);

		    if (fechaAso != null) {
		        vp.setFechaAsociacion(LocalDate.parse(fechaAso));
		    }

		    vp.setEstado("EA");

		    persona.getVehiculoPersona().add(vp);
		    Personas personaGuardada = personasRepository.save(persona);

		    return Optional.of(personaGuardada);

		} catch (Exception e) {
		    logger.error("Error: No se pudo agregar el documento al vehículo", e);
		    return Optional.empty();
		}
	}

	@Override
	public boolean updateEstadoConductor(Long idPersona, Long idVehiculo, String nuevoEstado) {
		 try {
		        Personas persona = personasRepository.findById(idPersona)
		                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + idPersona));

		        Vehiculos vehiculo = vehiculosRepository.findById(idVehiculo)
		                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado con ID: " + idVehiculo));

		        VehiculoPersona vehiculoPersona = vehiculoPersonaRepository
		                .findByVehiculo_IdAndPersona_Id(idVehiculo, idPersona)
		                .orElseThrow(() -> new IllegalArgumentException(
		                        "No se ha encontrado una relación entre persona y vehículo"));

		        List<String> estadosPermitidos = List.of("PO", "EA", "RO");
		        if (!estadosPermitidos.contains(nuevoEstado.toUpperCase())) {
		            throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
		        }

		        vehiculoPersona.setEstado(nuevoEstado.toUpperCase());
		        vehiculoPersonaRepository.save(vehiculoPersona);

		        logger.info("Estado actualizado a '{}' para Vehículo ID: '{}' con Persona ID: {}", nuevoEstado, idVehiculo, idPersona);
		        return true;

		    } catch (Exception e) {
		        logger.error("Error: No se pudo actualizar el estado del Vehículo ID: '{}' con Persona ID: {} ", idVehiculo, idPersona, e);
		        return false;
		    }
	}

	@Override
	public boolean addLicencia(String idPersona, String licencia, String fechaLicencia) {
		
		try {
	        Personas persona = personasRepository.findByIdPersona(idPersona)
	                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID_PERSONA: " + idPersona));

	        if (!persona.getTipoPersona().equals("C")) {
	            throw new IllegalArgumentException("La persona no es un conductor");
	        }
	        
	        if (licencia == null || licencia.trim().isEmpty()) {
	            throw new IllegalArgumentException("El archivo de licencia está vacío o es nulo");
	        }

	        persona.setLicencia(licencia);
	        persona.setFechaVencimiento(LocalDate.parse(fechaLicencia));
	        personasRepository.save(persona);

	        logger.info("Licencia actualizada para persona con ID_PERSONA: {}", idPersona);
	        return true;

	    } catch (Exception e) {
	        logger.error("Error: No se pudo agregar la licencia a la persona con ID_PERSONA: {}", idPersona, e);
	        return false;
	    }
	}
}
