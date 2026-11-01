package com.ppooii.proyecto_vehiculos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ppooii.proyecto_vehiculos.entities.Trayecto;
import com.ppooii.proyecto_vehiculos.services.TrayectoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Trayecto", description = "Gestión de trayectos")
@RestController
public class TrayectoController {

	private final TrayectoService trayectoService;

	@Autowired
	public TrayectoController(TrayectoService trayectoService) {
		this.trayectoService = trayectoService;
	}

	// MÉTODO GET
	@Operation(summary = "Obtiene todos los trayectos.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Trayectos encontrados correctamente",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = Trayecto.class))),
		@ApiResponse(responseCode = "404", description = "No se encontraron trayectos", content = @Content)
	})
	@CrossOrigin(origins = "http://localhost")
	@GetMapping("/trayectos")
	public List<Trayecto> getAllTrayectos() {
		return this.trayectoService.findAll();
	}
	
	@Operation(summary = "Obtiene los trayectos asociados a una ruta específica.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Trayectos encontrados correctamente",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = Trayecto.class))),
		@ApiResponse(responseCode = "404", description = "No se encontraron trayectos", content = @Content)
	})
	@CrossOrigin(origins = "http://localhost")
	@GetMapping("/trayectos/{codigoRuta}")
	public List<Trayecto> getTrayectoByCodigoRuta(@PathVariable String codigoRuta) {
		return this.trayectoService.findByCodigoRuta(codigoRuta);
	}

	@Operation(summary = "Obtiene los códigos de ruta asociados a un conductor específico.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Códigos de ruta encontrados correctamente",
			content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "Conductor sin rutas asociadas", content = @Content)
	})
	@GetMapping("/trayectos/conductor/{idPersona}")
	public List<String> getTrayectoByConductor(@PathVariable String idPersona) {
		return this.trayectoService.findDistinctCodigoRutaByPersonaIdentificacion(idPersona);
	}

	@Operation(summary = "Obtiene los trayectos asociados a un vehículo por su placa.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Trayectos encontrados correctamente",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = Trayecto.class))),
		@ApiResponse(responseCode = "404", description = "No se encontraron trayectos para el vehículo", content = @Content)
	})
	@GetMapping("/trayectos/vehiculo/{placa}")
	public List<Trayecto> getTrayectoByVehiculo(@PathVariable String placa) {
		return this.trayectoService.findByVehiculo_Placa(placa);
	}

	// MÉTODO POST
	@Operation(summary = "Agrega un nuevo trayecto al sistema.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Trayecto agregado correctamente"),
		@ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
	})
	@PostMapping("/trayectos")
	public boolean addTrayecto(@RequestBody @Validated Trayecto trayecto) {
		return trayectoService.addTrayecto(trayecto);
	}

	// MÉTODO PUT
	@Operation(summary = "Actualiza los datos de un trayecto existente.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Trayecto actualizado correctamente"),
		@ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
	})
	@PutMapping("/trayectos")
	public boolean updateTrayecto(@RequestBody @Validated Trayecto trayecto) {
		return trayectoService.updateTrayecto(trayecto);
	}

	// MÉTODO DELETE
	@Operation(summary = "Elimina un trayecto por su ID.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Trayecto eliminado correctamente"),
		@ApiResponse(responseCode = "404", description = "Trayecto no encontrado", content = @Content)
	})
	@DeleteMapping("/trayectos/{id}")
	public boolean deleteTrayecto(@PathVariable Long id) {
		return trayectoService.deleteTrayecto(id);
	}
}
