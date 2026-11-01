package com.ppooii.proyecto_vehiculos.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ppooii.proyecto_vehiculos.entities.Vehiculos;
import com.ppooii.proyecto_vehiculos.services.VehiculosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Vehiculos", description = "Gestión de vehiculos")
@RestController
@CrossOrigin(origins = "http://localhost")
public class VehiculosController {
	
	private final VehiculosService vehiculosService;
	
	public VehiculosController(VehiculosService vehiculosService) {
		this.vehiculosService = vehiculosService;
	}

	// ========= MÉTODOS HTTP =========

	// MÉTODO GET
	@Operation(summary = "Obtiene todos los vehículos.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Lista de vehículos encontrada",
	        content = @Content(mediaType = "application/json",
	            schema = @Schema(implementation = Vehiculos.class))),
	    @ApiResponse(responseCode = "404", description = "No se encontraron vehículos", content = @Content)
	})
	@GetMapping("/vehiculos")
	public List<Vehiculos> getAllVehiculos() {
		 return vehiculosService.findAll();
	}

	@Operation(summary = "Obtiene un vehículo por su ID.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Vehículo encontrado",
	        content = @Content(mediaType = "application/json",
	            schema = @Schema(implementation = Vehiculos.class))),
	    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado", content = @Content)
	})
	@GetMapping("/vehiculos/{id}")
	public Optional<Vehiculos> getById(@PathVariable Long id) {
	    return vehiculosService.findById(id);
	}

	@Operation(summary = "Obtiene un vehículo por su placa.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Vehículo encontrado",
	        content = @Content(mediaType = "application/json",
	            schema = @Schema(implementation = Vehiculos.class))),
	    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado", content = @Content)
	})
	@GetMapping("/vehiculos/placa/{placa}")
	public Optional<Vehiculos> getByPlaca(@PathVariable String placa) {
	    return vehiculosService.findByPlaca(placa);
	}

	@Operation(summary = "Obtiene los vehículos por tipo de vehículo.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Vehículos encontrados",
	        content = @Content(mediaType = "application/json",
	            schema = @Schema(implementation = Vehiculos.class))),
	    @ApiResponse(responseCode = "404", description = "No se encontraron vehículos del tipo especificado", content = @Content)
	})
	@GetMapping("/vehiculos/tipo/{tipoVehiculo}")
	public List<Vehiculos> getByTipo(@PathVariable String tipoVehiculo) {
	    return vehiculosService.findVehiculosByTipoVehiculos(tipoVehiculo);
	}

	@Operation(summary = "Obtiene los vehículos filtrados por el código del documento asociado.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Vehículos encontrados",
	        content = @Content(mediaType = "application/json",
	            schema = @Schema(implementation = Vehiculos.class))),
	    @ApiResponse(responseCode = "404", description = "No se encontraron vehículos con ese código de documento", content = @Content)
	})
	@GetMapping("/vehiculos/documento/codigo/{codigo}")
	public List<Vehiculos> getByTipoDocumento(@PathVariable int codigo) {
	    return vehiculosService.findVehiculosByDocumentoCodigo(codigo);
	}

	@Operation(summary = "Obtiene los vehículos según el estado de sus documentos.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Vehículos encontrados",
	        content = @Content(mediaType = "application/json",
	            schema = @Schema(implementation = Vehiculos.class))),
	    @ApiResponse(responseCode = "404", description = "No se encontraron vehículos con ese estado", content = @Content)
	})
	@GetMapping("/vehiculos/documento/estado/{estado}")
	public List<Vehiculos> getByTipoEstadoDocumento(@PathVariable String estado) {
	    return vehiculosService.findVehiculosByDocumentoEstadoList(estado);
	}

	@Operation(summary = "Consulta vehículos con documentos por vencer dentro de un número de días.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Vehículos con documentos por vencer",
	        content = @Content(mediaType = "application/json",
	            schema = @Schema(implementation = Vehiculos.class))),
	    @ApiResponse(responseCode = "400", description = "Parámetro inválido", content = @Content)
	})
	@GetMapping("/vehiculos/documento/por-vencer")
	public ResponseEntity<List<Vehiculos>> consultarDocumentosPorVencer(@RequestParam int dias) {
	    List<Vehiculos> vehiculos = vehiculosService.getVehiculosConDocumentosPorVencer(dias);
	    return ResponseEntity.ok(vehiculos);
	}

	// MÉTODO POST
	@Operation(summary = "Agrega un nuevo vehículo.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Vehículo agregado correctamente"),
	    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
	})
	@PostMapping("/vehiculos")
	public boolean addVehiculo(@RequestBody @Validated Vehiculos vehiculo) {
	    return vehiculosService.addVehiculo(vehiculo);
	}

	// MÉTODO PUT
	@Operation(summary = "Actualiza un vehículo existente.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Vehículo actualizado correctamente"),
	    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
	})
	@PutMapping("/vehiculos")
	public boolean editVehiculo(@RequestBody @Validated Vehiculos vehiculo) {
	    return vehiculosService.updateVehiculo(vehiculo);
	}

	@Operation(summary = "Asocia un documento a un vehículo.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Documento asociado correctamente"),
	    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
	})
	@PutMapping("/vehiculos/documento")
	public Optional<Vehiculos> anadirDocumentoVehiculo(@RequestBody Map<String, Object> documento) {
	    Long idVehiculo = Long.valueOf(documento.get("idVehiculo").toString());
	    Long idDocumento = Long.valueOf(documento.get("idDocumento").toString());
	    String fechaExp = documento.get("fechaExp").toString();
	    String fechaVenc = documento.get("fechaVenc").toString();
	    return vehiculosService.addDocumentoToVehiculo(idVehiculo, idDocumento, fechaExp, fechaVenc);
	}

	@Operation(summary = "Actualiza el estado de un documento asociado a un vehículo.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
	    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
	})
	@PutMapping("/vehiculos/documento/estado")
	public boolean actualizarEstadoDocumento(@RequestBody Map<String, Object> documento) {
	    Long idVehiculo = Long.valueOf(documento.get("idVehiculo").toString());
	    Long idDocumentoVehiculo = Long.valueOf(documento.get("idDocumentoVehiculo").toString());
	    String nuevoEstado = documento.get("nuevoEstado").toString();
	    return vehiculosService.updateEstadoDocumento(idVehiculo, idDocumentoVehiculo, nuevoEstado);
	}

	@Operation(summary = "Asocia o actualiza el archivo PDF de un documento de vehículo.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Archivo PDF actualizado correctamente"),
	    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
	})
	@PutMapping("/vehiculos/documento/pdf")
	public boolean actualizarPdfDocumento(@RequestBody Map<String, Object> documento) {
	    Long idVehiculo = Long.valueOf(documento.get("idVehiculo").toString());
	    Long idDocumentoVehiculo = Long.valueOf(documento.get("idDocumentoVehiculo").toString());
	    String archivoPdf = documento.get("archivoPdf").toString();
	    return vehiculosService.addPdfDocumento(idVehiculo, idDocumentoVehiculo, archivoPdf);
	}

	// MÉTODO DELETE
	@Operation(summary = "Elimina un vehículo por su ID.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Vehículo eliminado correctamente"),
	    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado", content = @Content)
	})
	@DeleteMapping("/vehiculos/{id}")
	public boolean deleteVehiculo(@PathVariable Long id) {
	    return vehiculosService.deleteVehiculo(id);
	}
}
