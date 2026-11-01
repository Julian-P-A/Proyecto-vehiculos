package com.ppooii.proyecto_vehiculos.component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ppooii.proyecto_vehiculos.APIs.GoogleMaps.Geocoder;
import com.ppooii.proyecto_vehiculos.entities.Trayecto;
import com.ppooii.proyecto_vehiculos.entities.VehiculoPersona;
import com.ppooii.proyecto_vehiculos.entities.DocumentoVehiculo;
import com.ppooii.proyecto_vehiculos.entities.Personas;
import com.ppooii.proyecto_vehiculos.repository.TrayectoRepository;
import com.ppooii.proyecto_vehiculos.repository.VehiculoPersonaRepository;
import com.ppooii.proyecto_vehiculos.repository.VehiculosRepository;
import com.ppooii.proyecto_vehiculos.repository.DocumentoVehiculoRepository;
import com.ppooii.proyecto_vehiculos.repository.PersonasRepository;

@Component
public class ScheduledTask {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	@Autowired
	@Qualifier("IPersonasRepo")
	private PersonasRepository PersonaRepository;
	
	@Autowired
	@Qualifier("IVehiculosRepo")
	private VehiculosRepository VehiculosRepository;
	
	@Autowired
	@Qualifier("IVehiculoPersonaRepo")
	private VehiculoPersonaRepository vehiculoPersonaRepository;
	
	@Autowired
	@Qualifier("IDocumentoVehiculoRepo")
	private DocumentoVehiculoRepository documentoVehiculoRepository;
	
	@Autowired
	@Qualifier("ITrayectoRepo")
	private TrayectoRepository trayectoRepository;

	@Autowired
	private Geocoder geocoder;
	
	@Scheduled (fixedRate = 90000)
	public void scheduleTaskWithFixedrRate() {
		logger.info("Update Latitud y Longitud :: Executing at {}", dateTimeFormatter.format(LocalTime.now()));
		
		try {
            List<Trayecto> trayectos = trayectoRepository.findAll();
            if (trayectos.isEmpty()) return;

            for (Trayecto trayecto : trayectos) {
                String LatLng = geocoder.getLatLng(trayecto.getUbicacion());
                String[] coor = LatLng.split(",");

                trayecto.setLatitud(Double.parseDouble(coor[0]));
                trayecto.setLongitud(Double.parseDouble(coor[1]));

                trayectoRepository.save(trayecto);

                logger.info(LatLng + " - {}", dateTimeFormatter.format(LocalTime.now()));
            }

        } catch (Exception e) {
            logger.error("Error ejecutando tarea programada", e);
        }
    }
	
	@Scheduled (cron = "0 */2 * * * * ")
	public void scheduleTaskWithCronExpression() {
		logger.info("Verificacion conductor :: Executing at {}", dateTimeFormatter.format(LocalTime.now()));
		
		try {
            List<Personas> personas = PersonaRepository.findByTipoPersona("C");
            
            if (personas.isEmpty()) return;
            
            
            
            for (Personas persona : personas) {
            	if(persona.getFechaVencimiento() != null &&
            		    persona.getFechaVencimiento().isBefore(LocalDate.now())) {
            		
            		
            		List<VehiculoPersona> vehiculoPersona = vehiculoPersonaRepository.findByPersona_Id(persona.getId());
            		
            		vehiculoPersona.forEach(vp -> {
            			if (!"RO".equals(vp.getEstado())) {
						vp.setEstado("RO");
						vehiculoPersonaRepository.save(vp);
						}
					});
            	}
            }

        } catch (Exception e) {
            logger.error("Error ejecutando tarea programada", e);
        }
    }
	
	@Scheduled (cron = "0 */2 * * * * ")
	public void scheduleTaskWithCronExpression2() {
		logger.info("Verificacion documentos vehiculo :: Executing at {}", dateTimeFormatter.format(LocalTime.now()));
		
		try {
            List<DocumentoVehiculo> documentosVehiculo = documentoVehiculoRepository.findAll();
            
            if (documentosVehiculo.isEmpty()) return;
            
            for (DocumentoVehiculo documentoVehiculo : documentosVehiculo) {
            	if(documentoVehiculo.getFechaVencimiento().isBefore(LocalDate.now())
            			&&!"VENCIDO".equals(documentoVehiculo.getEstado())) {
            		documentoVehiculo.setEstado("VENCIDO");
					documentoVehiculoRepository.save(documentoVehiculo);
            	}
            }

        } catch (Exception e) {
            logger.error("Error ejecutando tarea programada", e);
        }
    }
		
}
