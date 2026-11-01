package com.ppooii.proyecto_vehiculos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.PPOOII.Laboratorio.Config.Model.JwtRequest;
import com.PPOOII.Laboratorio.Config.Model.JwtResponse;
import com.ppooii.proyecto_vehiculos.config.JWTAuthenticationConfig;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Autenticación JWT", description = "Generación y validación de tokens JWT")
@RestController
@CrossOrigin
public class JwtAuthenticationController {
	
	@Autowired
	private JWTAuthenticationConfig jwtAuthenticationConfig;
	
	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;
	
	@Operation(summary = "Autenticar usuario y generar token JWT")
	@RequestMapping(
			value = "/authenticate",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE	
	)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
	throws Exception {
		final UserDetails userDetails = jwtInMemoryUserDetailsService
		    .loadUserByUsername(authenticationRequest.getUsername());
		
		final String token = jwtAuthenticationConfig.getJWTToken(userDetails.getUsername());
		return ResponseEntity.ok(new JwtResponse(token));
	}
}
