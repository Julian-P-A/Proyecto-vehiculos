package com.ppooii.proyecto_vehiculos.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.ppooii.proyecto_vehiculos.entities.DocumentoVehiculo;
import com.ppooii.proyecto_vehiculos.entities.Documentos;
import com.ppooii.proyecto_vehiculos.entities.Vehiculos;
import com.ppooii.proyecto_vehiculos.repository.DocumentoVehiculoRepository;
import com.ppooii.proyecto_vehiculos.repository.DocumentosRepository;
import com.ppooii.proyecto_vehiculos.repository.VehiculosRepository;
import com.ppooii.proyecto_vehiculos.services.interfaces.IVehiculosService;


@Service
public class VehiculosService implements IVehiculosService {
	@Autowired
	@Qualifier("IVehiculosRepo")
	final VehiculosRepository vehiculosRepository;
	
	@Autowired
	@Qualifier("IDocumentosRepo")
	final DocumentosRepository documentosRepository;
	
	@Autowired
	@Qualifier("IDocumentoVehiculoRepo")
	final DocumentoVehiculoRepository documentoVehiculoRepository;
	
	//Logs
	//Wrong Logs
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(VehiculosService.class);
	
	public VehiculosService(VehiculosRepository vehiculosRepository, DocumentosRepository documentosRepository, DocumentoVehiculoRepository documentoVehiculoRepository) {
		this.vehiculosRepository = vehiculosRepository;
		this.documentosRepository = documentosRepository;
		this.documentoVehiculoRepository = documentoVehiculoRepository;
		
	}
	
	@Override
	public boolean addVehiculo(Vehiculos vehiculo) {
		
		try {
		    if (vehiculo == null) {
		        logger.error("Error: El vehículo es nulo");
		        return false;
		    }

		    if (vehiculo.getId() != null) {
		        logger.error("Error: Un vehículo con ese ID ya existe");
		        return false;
		    }

		    if (vehiculo.getDocumentosVehiculo() == null || vehiculo.getDocumentosVehiculo().isEmpty()) {
		        logger.error("Error: El vehículo debe tener al menos un documento asociado");
		        return false;
		    }

		    String tipoVehiculo = vehiculo.getTipoVehiculo();

		    for (DocumentoVehiculo dv : vehiculo.getDocumentosVehiculo()) {

		        if (dv.getDocumento() == null || dv.getDocumento().getId() == null) {
		            logger.error("Error: No se ha proporcionado el ID del documento");
		            return false;
		        }

		        Documentos documento = documentosRepository.findById(dv.getDocumento().getId())
		                .orElseThrow(() -> new RuntimeException(
		                        "Documento no encontrado: " + dv.getDocumento().getId()
		                ));

		        String tipoAplica = documento.getTipoAplica();

		        if ((tipoVehiculo.equalsIgnoreCase("vehiculo") &&
		        	!(tipoAplica.equalsIgnoreCase("A") || tipoAplica.equalsIgnoreCase("AM")))
		        	|| (tipoVehiculo.equalsIgnoreCase("moto") &&
		        	!(tipoAplica.equalsIgnoreCase("M") || tipoAplica.equalsIgnoreCase("AM")))){
		        	
		            throw new IllegalArgumentException("Documento no valido para " + tipoVehiculo);
		        }

		        dv.setDocumento(documento);
		        dv.setVehiculo(vehiculo);
		        dv.setEstado("EN_VERIFICACION");
		    }

		    vehiculosRepository.save(vehiculo);
		    return true;

		} catch (Exception e) {
		    logger.error("Error: No se pudo agregar el vehículo", e);
		    return false;
		}

	}

	@Override
	public boolean updateVehiculo(Vehiculos vehiculo) {
		try {
	        if (vehiculo == null) {
	            logger.error("Error: El vehículo es nulo");
	            return false;
	        }

	        if (vehiculo.getId() == null) {
	            logger.error("Error: El vehículo no tiene ID asignado");
	            return false;
	        }

	        if (vehiculo.getDocumentosVehiculo() == null || vehiculo.getDocumentosVehiculo().isEmpty()) {
	            logger.error("Error: El vehículo debe tener al menos un documento asociado");
	            return false;
	        }

	        Vehiculos existente = vehiculosRepository.findById(vehiculo.getId())
	                .orElseThrow(() -> new IllegalArgumentException(
	                        "No se encontró el vehículo con ID: " + vehiculo.getId())
	                );

	        for (DocumentoVehiculo dv : vehiculo.getDocumentosVehiculo()) {
	            if (dv.getDocumento() == null || dv.getDocumento().getId() == null) {
	                logger.error("Error: DocumentoVehiculo sin documento o ID de documento nulo");
	                return false;
	            }

	            Documentos documento = documentosRepository.findById(dv.getDocumento().getId())
	                    .orElseThrow(() -> new RuntimeException(
	                            "Documento no encontrado: " + dv.getDocumento().getId())
	                    );

	            String tipoVehiculo = vehiculo.getTipoVehiculo();
	            String tipoAplica = documento.getTipoAplica();

	            if ((tipoVehiculo.equalsIgnoreCase("vehiculo") &&
			        !(tipoAplica.equalsIgnoreCase("A") || tipoAplica.equalsIgnoreCase("AM")))
			        || (tipoVehiculo.equalsIgnoreCase("moto") &&
			        !(tipoAplica.equalsIgnoreCase("M") || tipoAplica.equalsIgnoreCase("AM")))){
			        	
			            throw new IllegalArgumentException("Documento no valido para " + tipoVehiculo);
			    }

	            dv.setDocumento(documento);
	            dv.setVehiculo(vehiculo);

	            if (dv.getEstado() == null || dv.getEstado().isEmpty()) {
	                dv.setEstado("EN_VERIFICACION");
	            }
	        }

	        vehiculosRepository.save(vehiculo);
	        logger.info("Vehículo actualizado correctamente con ID: " + vehiculo.getId());
	        return true;

	    } catch (Exception e) {
	        logger.error("Error: No se pudo actualizar el vehículo", e);
	        return false;
	    }
	}

	@Override
	public boolean deleteVehiculo(Long id) {
		
		try {
			if(id > 0) {
				if(vehiculosRepository.findById(id).isPresent()) {
					vehiculosRepository.deleteById(id);
					return true;
				}
				logger.error("Error: El vehiculo con ese id no existe");
				return false;
			}
			logger.error("Error: El id es invalido");
			return false;
		}catch(Exception e) {
			logger.error("Error: Eliminar el vehiculo no fue posible");
			return false;
		}
		
	}
	
	@Override
	public Optional<Vehiculos> findByPlaca(String placa) {
		return Optional.ofNullable(vehiculosRepository.findByPlaca(placa).orElse(null));
	}
	
	@Override
	public List<Vehiculos> findAll() {
		return vehiculosRepository.findAll();
	}

	@Override
	public Optional<Vehiculos> findById(Long id) {
		
		try {
			if(id > 0) {
				Optional<Vehiculos> vehiculo = vehiculosRepository.findById(id);
				
				if(!vehiculo.isPresent()) {
					logger.error("Error: No se ha encontrado un vehiculo con ese id");
					return Optional.empty();
				}
				return vehiculo;
			}
			logger.error("Error: El id es invalido");
			return Optional.empty();
		}catch(Exception e) {
			return Optional.empty();
		}
		
	}

	@Override
	public List<Vehiculos> findVehiculosByTipoVehiculos(String tipoVehiculo) {
		
		try {
			if (tipoVehiculo == null || tipoVehiculo.trim().isEmpty()) {
	            logger.error("Error: El tipo de vehículo es inválido o está vacío");
	            return Collections.emptyList();
	        }
			return vehiculosRepository.findByTipoVehiculo(tipoVehiculo);
		}catch(Exception e) {
			logger.error("Error: No se pudieron obtener los vehículos por tipo");
	        return Collections.emptyList();
		}

	}

	@Override
	public List<Vehiculos> findVehiculosByDocumentoCodigo(int codigo) {
		
		try {
			List<Vehiculos> vehiculos = vehiculosRepository.findByDocumentoCodigo(codigo);
			
			if (vehiculos.isEmpty()) {
	            logger.error("Error: El codigo es inválido o no hay vehiculos con ese codigo");
	            return Collections.emptyList();
	        }
			return vehiculos;
		}catch(Exception e) {
			logger.error("Error: No se pudieron obtener los vehículos por tipo");
	        return Collections.emptyList();
		}
		
	}

	@Override
	public List<Vehiculos> findVehiculosByDocumentoEstadoList(String estado) {
		
		try {
			List<Vehiculos> vehiculos = vehiculosRepository.findByDocumentoEstado(estado);
			if (vehiculos.isEmpty()) {
	            logger.error("Error: El estado es inválido o no hay vehiculos con ese estado");
	            return Collections.emptyList();
	        }
			return vehiculos;
		}catch(Exception e) {
			logger.error("Error: No se pudieron obtener los vehículos por ese estado de documentos");
	        return Collections.emptyList();
		}
		
	}

	@Override
	public Optional<Vehiculos> addDocumentoToVehiculo(Long idVehiculo, Long idDocumento, String fechaExp, String fechaVenc) {
		
		try {
		    Vehiculos vehiculo = vehiculosRepository.findById(idVehiculo)
		            .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado con ID: " + idVehiculo));

		    Documentos documento = documentosRepository.findById(idDocumento)
		            .orElseThrow(() -> new IllegalArgumentException("Documento no encontrado con ID: " + idDocumento));

		    String tipoVehiculo = vehiculo.getTipoVehiculo();
		    String tipoAplica = documento.getTipoAplica();

		    if ((tipoVehiculo.equalsIgnoreCase("vehiculo") &&
		        !(tipoAplica.equalsIgnoreCase("A") || tipoAplica.equalsIgnoreCase("AM")))
		        || (tipoVehiculo.equalsIgnoreCase("moto") &&
		        !(tipoAplica.equalsIgnoreCase("M") || tipoAplica.equalsIgnoreCase("AM")))){
		        	
		            throw new IllegalArgumentException("Documento no valido para " + tipoVehiculo);
		   }

		    DocumentoVehiculo dv = new DocumentoVehiculo();
		    dv.setVehiculo(vehiculo);
		    dv.setDocumento(documento);

		    if (fechaExp != null) {
		        dv.setFechaExpedicion(LocalDate.parse(fechaExp));
		    }
		    if (fechaVenc != null) {
		        dv.setFechaVencimiento(LocalDate.parse(fechaVenc));
		    }

		    dv.setEstado("EN_VERIFICACION");

		    vehiculo.getDocumentosVehiculo().add(dv);
		    Vehiculos vehiculoGuardado = vehiculosRepository.save(vehiculo);

		    return Optional.of(vehiculoGuardado);

		} catch (Exception e) {
		    logger.error("Error: No se pudo agregar el documento al vehículo", e);
		    return Optional.empty();
		}
		
	}
	
	@Override
	public boolean updateEstadoDocumento(Long idVehiculo, Long idDocumentoVehiculo, String nuevoEstado) {
	    try {
	        Vehiculos vehiculo = vehiculosRepository.findById(idVehiculo)
	                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado con ID: " + idVehiculo));

	        DocumentoVehiculo documentoVehiculo = vehiculo.getDocumentosVehiculo()
	                .stream()
	                .filter(dv -> dv.getId().equals(idDocumentoVehiculo))
	                .findFirst()
	                .orElseThrow(() -> new IllegalArgumentException(
	                        "DocumentoVehiculo no encontrado con ID: " + idDocumentoVehiculo)
	                );

	        List<String> estadosPermitidos = List.of("EN_VERIFICACION", "HABILITADO", "VENCIDO");
	        if (!estadosPermitidos.contains(nuevoEstado.toUpperCase())) {
	            throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
	        }

	        documentoVehiculo.setEstado(nuevoEstado.toUpperCase());

	        vehiculosRepository.save(vehiculo);

	        logger.info("Estado actualizado a '{}' para documentoVehiculo ID: {}", nuevoEstado, idDocumentoVehiculo);
	        return true;

	    } catch (Exception e) {
	        logger.error("Error: No se pudo actualizar el estado del documentoVehiculo con ID: " + idDocumentoVehiculo, e);
	        return false;
	    }
	}

	@Override
	public boolean addPdfDocumento(Long idVehiculo, Long idDocumentoVehiculo, String documentoPdf) {
		try {
	        Vehiculos vehiculo = vehiculosRepository.findById(idVehiculo)
	                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado con ID: " + idVehiculo));

	        DocumentoVehiculo documentoVehiculo = vehiculo.getDocumentosVehiculo()
	                .stream()
	                .filter(dv -> dv.getId().equals(idDocumentoVehiculo))
	                .findFirst()
	                .orElseThrow(() -> new IllegalArgumentException(
	                        "DocumentoVehiculo no encontrado con ID: " + idDocumentoVehiculo)
	                );

	        documentoVehiculo.setArchivoPdf(documentoPdf);

	        vehiculosRepository.save(vehiculo);

	        logger.info("Pdf actualizado para documentoVehiculo ID: {}", idDocumentoVehiculo);
	        return true;

	    } catch (Exception e) {
	        logger.error("Error: No se pudo actualizar el documento del documentoVehiculo con ID: " + idDocumentoVehiculo, e);
	        return false;
	    }
	}
	
	@Override
	public List<Vehiculos> getVehiculosConDocumentosPorVencer(int dias) {
        LocalDate fechaLimite = LocalDate.now().plusDays(dias);
        return documentoVehiculoRepository.findVehiculosConDocumentosPorVencer(fechaLimite);
    }
	
	


}
