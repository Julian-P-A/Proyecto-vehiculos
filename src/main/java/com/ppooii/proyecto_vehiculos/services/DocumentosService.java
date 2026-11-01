package com.ppooii.proyecto_vehiculos.services;

import com.ppooii.proyecto_vehiculos.services.interfaces.IDocumentosService;
import com.ppooii.proyecto_vehiculos.entities.Documentos;
import com.ppooii.proyecto_vehiculos.repository.DocumentosRepository;
import com.ppooii.proyecto_vehiculos.repository.VehiculosRepository;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Service;

@Service
public class DocumentosService implements IDocumentosService {
	@Autowired
	@Qualifier("IDocumentosRepo")
	private final DocumentosRepository documentosRepository;
	
	@Autowired
	@Qualifier("IVehiculosRepo")
	private final VehiculosRepository vehiculosRepository;
	
	//Logs
	//Wrong Logs
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(VehiculosService.class);
	
	public DocumentosService(DocumentosRepository documentosRepository, VehiculosRepository vehiculosRepository) {
		this.documentosRepository = documentosRepository;
		this.vehiculosRepository = vehiculosRepository;
	}
	
	@Override
	public boolean addDocumento(Documentos documento) {
		
		try {
			if(documento != null) {
				if(documento.getId() == null) {
					documentosRepository.save(documento);
					return true;	
				}
				logger.error("Error: Un documento con ese Id ya existe");
				return false;
			}
			logger.error("Error: El documento es Nulo");
			return false;
		}catch(Exception e) {
			logger.error("Error: No se pudo agregar el documento");
			return false;
		}
		
	}
	
	@Override
	public boolean addDocumentos(List<Documentos> documentos) {
		
		try {
			if(documentos != null) {
				boolean verificacion = documentos.stream().allMatch(doc -> doc.getId() == null);
				if(verificacion == true) {
					documentosRepository.saveAll(documentos);
					return true;	
				}
				logger.error("Error: Un documento con ese Id ya existe");
				return false;
			}
			logger.error("Error: El documento es Nulo");
			return false;
		}catch(Exception e) {
			logger.error("Error: No se pudo agregar el documento");
			return false;
		}
		
	}

	@Override
	public boolean updateDocumento(Documentos documento) {
		
		try {
			if(documento != null) {
				documentosRepository.findById(documento.getId());
				documentosRepository.save(documento);
				return true;
			}
				return false;
		}catch(Exception e) {
			logger.error("Error: El documento es Nulo");
			return false;

		}
	}

	@Override
	public boolean deleteDocumento(Long id) {
		
		try {
			if(id > 0) {
				if(documentosRepository.findById(id).isPresent()) {
					documentosRepository.deleteById(id);
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
    public Optional<Documentos> findById(Long id) {
        return documentosRepository.findById(id);
    }

    @Override
    public Optional<Documentos> findByCodigo(int codigo) {
        return documentosRepository.findByCodigo(codigo);
    }

	@Override
	public List<Documentos> findAll() {
		return documentosRepository.findAll();
	}

}
