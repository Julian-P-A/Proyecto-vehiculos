package com.ppooii.proyecto_vehiculos.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.ppooii.proyecto_vehiculos.entities.Documentos;


public interface IDocumentosService {
	
	public boolean addDocumento(Documentos documento);
	public boolean addDocumentos(List<Documentos> documentos);
	public boolean updateDocumento(Documentos documento);
	public boolean deleteDocumento(Long id);
	
	Optional<Documentos> findById(Long id);
    Optional<Documentos> findByCodigo(int codigo);
	
	List<Documentos> findAll();
	
	

}
