package com.ppooii.proyecto_vehiculos.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ppooii.proyecto_vehiculos.entities.Personas;
import com.ppooii.proyecto_vehiculos.services.PersonasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Personas", description = "Gestión de personas y conductores")
@RestController
public class PersonasController {
	
	private final PersonasService personasService;
	
	public PersonasController(PersonasService personasService) {
		this.personasService = personasService;
	}

	@Operation(summary = "Obtener todas las personas")
	@GetMapping("/personas")
	public List<Personas> getAllPersonas() {
		return this.personasService.findAll();
	}

	@Operation(summary = "Obtener persona por ID")
	@GetMapping("/personas/{id}")
	public Optional<Personas> getPersonaById(
		@Parameter(description = "ID de la persona", required = true) @PathVariable Long id) {
		return this.personasService.findById(id);
	}

	@Operation(summary = "Buscar personas por estado del conductor")
	@GetMapping("/personas/estado/{estado}")
	public List<Personas> findByEstadoConductor(
		@Parameter(description = "Estado del conductor (ACTIVO, INACTIVO, etc.)") @PathVariable String estado) {
		return this.personasService.findByEstadoConductor(estado.toUpperCase());
	}

	@Operation(summary = "Buscar personas por tipo de persona")
	@GetMapping("/personas/tipo/{tipoPersona}")
	public List<Personas> findByTipoPersona(
		@Parameter(description = "Tipo de persona (CONDUCTOR, PROPIETARIO, etc.)") @PathVariable String tipoPersona) {
		return this.personasService.findByTipoPersona(tipoPersona.toUpperCase());
	}

	@Operation(summary = "Agregar nueva persona")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Persona agregada correctamente"),
		@ApiResponse(responseCode = "400", description = "Error en la validación de datos")
	})
    @PostMapping("/personas")
	public Object addPersona(@RequestBody @Validated Personas persona) {
		return personasService.addPersona(persona);
	}

	@Operation(summary = "Actualizar información de una persona")
    @PutMapping("/personas")
	public boolean updatePersona(@RequestBody @Validated Personas persona) {
		return personasService.updatePersona(persona);
	}

	@Operation(summary = "Asociar conductor a un vehículo")
    @PutMapping("/personas/conductor")
	public Optional<Personas> addConductorToVehiculo(@RequestBody Map<String, Object> documento) {
    	Long vehiculoId = Long.valueOf(documento.get("vehiculoId").toString());
        Long personaId = Long.valueOf(documento.get("personaId").toString());
        String fechaAso = documento.get("fechaAso").toString();
        
		return personasService.addConductorToVehiculo(vehiculoId, personaId, fechaAso);
	}

	@Operation(summary = "Actualizar licencia del conductor")
    @PutMapping("/personas/conductor/licencia")
	public boolean updateLicencia(@RequestBody Map<String, Object> persona) {
		String idPersona = persona.get("idPersona").toString();
		String fechaLicencia = persona.get("fechaVenc").toString();
    	String archivoPdf = persona.get("archivoPdf").toString();
    	
		return personasService.addLicencia(idPersona, archivoPdf, fechaLicencia);
	}

	@Operation(summary = "Actualizar estado del conductor en un vehículo")
    @PutMapping("/personas/conductor/estado")
	public boolean updateEstadoConductor(@RequestBody Map<String, Object> documento) {
		Long idPersona = Long.valueOf(documento.get("idPersona").toString());
		Long idVehiculo = Long.valueOf(documento.get("idVehiculo").toString());
		String nuevoEstado = documento.get("nuevoEstado").toString();
		
		return personasService.updateEstadoConductor(idPersona, idVehiculo, nuevoEstado);
	}

	@Operation(summary = "Eliminar persona por ID")
	@DeleteMapping("/personas/{id}")
	public boolean deletePersona(
		@Parameter(description = "ID de la persona a eliminar") @PathVariable Long id) {
		return personasService.deletePersona(id);
	}
}
