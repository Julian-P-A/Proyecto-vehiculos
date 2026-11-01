package com.ppooii.proyecto_vehiculos.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ppooii.proyecto_vehiculos.services.ExcelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Carga Excel", description = "Carga y procesamiento de archivos Excel")
@RestController
public class CargueFileExcel {
	
	private final ExcelService	excelService;
	
	public CargueFileExcel(ExcelService excelService) {
		this.excelService = excelService;
	}

	@Operation(summary = "Obtener detalle de un cargue de Excel")
	@GetMapping("/cargue/{idCargue}")
	public ResponseEntity<List<Map<String, Object>>> getDetalleCargue(@PathVariable Double idCargue) {
	    List<Map<String, Object>> detalle = excelService.consultarDetalleCargue(idCargue);
	    if (detalle.isEmpty()) {
	        return ResponseEntity.notFound().build(); 
	    }
	    return ResponseEntity.ok(detalle);
	}

	@Operation(summary = "Subir archivo Excel y procesarlo")
	@PostMapping("/uploadExcel")
	public String handleFileUpload(@RequestParam("file") MultipartFile file) {
		excelService.processExcel(file);
		return "Archivo subido y cargado exitosamente";
	}
}
