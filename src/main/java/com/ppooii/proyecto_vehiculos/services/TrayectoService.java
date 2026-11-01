package com.ppooii.proyecto_vehiculos.services;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ppooii.proyecto_vehiculos.entities.Personas;
import com.ppooii.proyecto_vehiculos.entities.Vehiculos;
import com.ppooii.proyecto_vehiculos.entities.Trayecto;
import com.ppooii.proyecto_vehiculos.entities.VehiculoPersona;
import com.ppooii.proyecto_vehiculos.repository.PersonasRepository;
import com.ppooii.proyecto_vehiculos.repository.TrayectoRepository;
import com.ppooii.proyecto_vehiculos.repository.VehiculoPersonaRepository;
import com.ppooii.proyecto_vehiculos.repository.VehiculosRepository;
import com.ppooii.proyecto_vehiculos.services.interfaces.ITrayectoService;

@Service
public class TrayectoService implements ITrayectoService {
	
	@Autowired
	@Qualifier("ITrayectoRepo")
	private TrayectoRepository trayectoRepository;
	
	@Autowired
	@Qualifier("IVehiculosRepo")
	private VehiculosRepository vehiculosRepository;
	
	@Autowired
	@Qualifier("IPersonasRepo")
	private PersonasRepository personasRepository;
	
	@Autowired
	@Qualifier("IVehiculoPersonaRepo")
	private VehiculoPersonaRepository vehiculoPersonaRepository;
	
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(TrayectoService.class);

	@Override
	public boolean addTrayecto(Trayecto trayecto) {
		
		try {
			if(trayecto.getId() != null) {
				logger.info("No se puede agregar un trayecto con ID");
				return false;
			}
			
			Personas persona = personasRepository.findById(Long.valueOf(trayecto.getPersona())).orElse(null);
			
			if(persona == null) {
				logger.info("No se puede agregar un trayecto sin persona");
				return false;
			}
			
			if(persona.getTipoPersona().equals("A")){
				logger.info("La persona para el trayecto tiene que ser conductor");
				return false;
			}
			
			Vehiculos vehiculo = vehiculosRepository.findById(Long.valueOf(trayecto.getVehiculo())).orElse(null); 
			
			if(vehiculo == null) {
				logger.info("No se puede agregar un trayecto sin vehiculo");
				return false;
			}
			
			if (vehiculo.getDocumentosVehiculo().isEmpty()) {
			    logger.info("El vehículo no tiene documentos registrados");
			    return false;
			}
			
			boolean documentosHabilitados = vehiculo.getDocumentosVehiculo()
				    .stream()
				    .allMatch(doc -> "Habilitado".equalsIgnoreCase(doc.getEstado()));

				if (!documentosHabilitados) {
				    logger.info("El vehículo no tiene todos los documentos en estado Habilitado");
				    return false;
				}
			
			VehiculoPersona vehiculoPersona = vehiculoPersonaRepository
				    .findByVehiculo_IdAndPersona_Id(
				        vehiculo.getId(), persona.getId()
				    ).orElse(null);
			
			if(!"PO".equals(vehiculoPersona.getEstado())) {
				logger.info(" El conductor en relacion con el vehiculo no puede operar");
				return false;
			}
			
			trayectoRepository.save(trayecto);
			return true;
			
		}catch(Exception e) {
			logger.info("No se pudo agregar el trayecto");
			return false;
		}
		
	}

	@Override
	public boolean updateTrayecto(Trayecto trayecto) {
		
		try {
			if(trayecto.getId() == null) {
				logger.info("Necesitas agregar un ID para actualizar un trayecto");
				return false;
			}
			
			Personas persona = personasRepository.findById(Long.valueOf(trayecto.getPersona())).orElse(null);
			
			if(persona == null) {
				logger.info("No se puede actualizar un trayecto sin persona");
				return false;
			}
			
			if(persona.getTipoPersona().equals("A")){
				logger.info("La persona para el trayecto tiene que ser conductor");
				return false;
			}
			
			Vehiculos vehiculo = vehiculosRepository.findById(Long.valueOf(trayecto.getVehiculo())).orElse(null); 
			
			if(vehiculo == null) {
				logger.info("No se puede actualizar un trayecto sin vehiculo");
				return false;
			}
			
			if (vehiculo.getDocumentosVehiculo().isEmpty()) {
			    logger.info("El vehículo no tiene documentos registrados");
			    return false;
			}
			
			boolean documentosHabilitados = vehiculo.getDocumentosVehiculo()
				    .stream()
				    .allMatch(doc -> "Habilitado".equalsIgnoreCase(doc.getEstado()));

				if (!documentosHabilitados) {
				    logger.info("El vehículo no tiene todos los documentos en estado Habilitado");
				    return false;
				}
			
			VehiculoPersona vehiculoPersona = vehiculoPersonaRepository
				    .findByVehiculo_IdAndPersona_Id(
				        vehiculo.getId(), persona.getId()
				    ).orElse(null);
			
			if(!"PO".equals(vehiculoPersona.getEstado())) {
				logger.info(" El conductor en relacion con el vehiculo no puede operar");
				return false;
			}
			
			trayectoRepository.save(trayecto);
			return true;
			
		}catch(Exception e) {
			logger.info("No se pudo actualizar el trayecto");
			return false;
		}
	}

	@Override
	public boolean deleteTrayecto(Long id) {
		 try {
		        if (id != null && trayectoRepository.existsById(id.intValue())) {
		            trayectoRepository.deleteById(id.intValue());
		            return true;
		        }
		        logger.error("Error: El trayecto con ese id no existe");
		        return false;
		    } catch (Exception e) {
		        logger.error("Error: Eliminar el trayecto no fue posible", e);
		        return false;
		    }
	}

	@Override
	public List<Trayecto> findAll() {
		return trayectoRepository.findAll();
	}


	@Override
	public List<Trayecto> findByVehiculo_Placa(String placa) {
		try {
			return trayectoRepository.findByVehiculo_Placa(placa);
		}catch(Exception e) {
			return null;
		}

	}

	@Override
	public List<Trayecto> findByCodigoRuta(String codigoRuta) {
		try {
			return trayectoRepository.findByCodigoRuta(codigoRuta);
		}catch(Exception e) {
			return null;
		}
	}

	@Override
	public List<String> findDistinctCodigoRutaByPersonaIdentificacion(String idPersona) {
		try {
			return trayectoRepository.findDistinctCodigoRutaByPersonaIdentificacion(idPersona);
		}catch(Exception e) {
			return null;
		}
	}

}
