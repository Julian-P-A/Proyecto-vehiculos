package com.ppooii.proyecto_vehiculos.controller;

import java.util.List;
import java.util.Map;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ppooii.proyecto_vehiculos.entities.UsuarioPK;
import com.ppooii.proyecto_vehiculos.entities.Usuarios;
import com.ppooii.proyecto_vehiculos.services.UsuariosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Usuarios", description = "Gestión de usuarios")
@RestController
public class UsuariosController {

	private final UsuariosService usuariosService;

	public UsuariosController(UsuariosService usuariosService) {
		this.usuariosService = usuariosService;
	}

	// MÉTODO GET
	@Operation(summary = "Obtiene la lista de todos los usuarios registrados.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Usuarios encontrados correctamente",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = Usuarios.class))),
		@ApiResponse(responseCode = "404", description = "No se encontraron usuarios", content = @Content)
	})
	@GetMapping("/usuarios")
	public List<Usuarios> getAllUsuarios() {
		return this.usuariosService.findAll();
	}

	@Operation(summary = "Genera una nueva API Key para un usuario válido.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "API Key generada correctamente",
			content = @Content(mediaType = "text/plain")),
		@ApiResponse(responseCode = "400", description = "Datos de usuario inválidos", content = @Content)
	})
	@GetMapping("/usuarios/apikey")
	public String getNewApiKey(@RequestBody @Validated UsuarioPK usuarioPK) {
		return this.usuariosService.getNewApiKey(usuarioPK);
	}

	// MÉTODO PUT
	@Operation(summary = "Actualiza la contraseña de un usuario.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Contraseña actualizada correctamente"),
		@ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
		@ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
	})
	@PutMapping("/usuarios/{login}")
	public boolean updatePassword(@PathVariable String login, @RequestBody Map<String, Object> documento) {
		String password = documento.get("password").toString();
		return this.usuariosService.updatePassword(login, password);
	}
}
