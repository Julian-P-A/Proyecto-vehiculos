package com.ppooii.proyecto_vehiculos.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

import com.ppooii.proyecto_vehiculos.services.DocumentosService;
import com.ppooii.proyecto_vehiculos.entities.Documentos;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Documentos", description = "Gestión de documentos asociados a vehículos o personas")
@RestController
public class DocumentosController {
	
	private final DocumentosService documentosService;
	
	public DocumentosController(DocumentosService documentosService) {
		this.documentosService = documentosService;
	}
	
	@Operation(summary = "Obtener todos los documentos")
	@GetMapping("/documentos")
	public List<Documentos> getAllDocumentos() {
		return this.documentosService.findAll();
	}

	@Operation(summary = "Obtener documento por ID")
	@GetMapping("/documentos/{id}")
	public Optional<Documentos> getById(@PathVariable Long id) {
		return documentosService.findById(id);
	}

	@Operation(summary = "Obtener documento por código")
	@GetMapping("/documentos/codigo/{codigo}")
	public Optional<Documentos> getByCodigo(@PathVariable int codigo) {
		return documentosService.findByCodigo(codigo);
	}

	@Operation(summary = "Agregar un nuevo documento")
    @PostMapping("/documentos")
    public boolean addDocumento(@RequestBody @Validated Documentos documento) {
        return documentosService.addDocumento(documento);
    }

	@Operation(summary = "Agregar varios documentos")
    @PostMapping("/documentos/list")
    public boolean addDocumentos(@RequestBody @Validated List<Documentos> documentos) {
        return documentosService.addDocumentos(documentos);
    }

	@Operation(summary = "Actualizar un documento existente")
    @PutMapping("/documentos")
    public boolean editDocumento(@RequestBody @Validated Documentos documento) {
        return documentosService.updateDocumento(documento);
    }

	@Operation(summary = "Eliminar documento por ID")
    @DeleteMapping("/documentos/{id}")
    public boolean deleteVehiculo(@PathVariable Long id) {
        return documentosService.deleteDocumento(id);
    }
}
