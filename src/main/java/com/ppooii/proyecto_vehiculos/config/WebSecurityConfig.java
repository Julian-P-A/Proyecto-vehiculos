package com.ppooii.proyecto_vehiculos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ppooii.proyecto_vehiculos.config.JWTAuthorizationFilter;
import com.ppooii.proyecto_vehiculos.config.model.Constants;

@EnableWebSecurity
@Configuration
class WebSecurityConfig {
	
	@Autowired
	JWTAuthorizationFilter jwtAuthorizationFilter;
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .cors(cors -> cors.configurationSource(request -> {
	            var corsConfig = new org.springframework.web.cors.CorsConfiguration();
	            corsConfig.setAllowedOrigins(java.util.List.of(
	                "http://localhost",
	                "http://localhost:5500",
	                "http://127.0.0.1:5500"
	            ));
	            corsConfig.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	            corsConfig.setAllowedHeaders(java.util.List.of("Authorization", "x-api-key", "Content-Type"));
	            corsConfig.setAllowCredentials(true);
	            return corsConfig;
	        }))
	        .authorizeHttpRequests(authz -> authz
	            .requestMatchers(Constants.LOGIN_URL).permitAll()
	            .requestMatchers("/api-docs/**").permitAll()
	            .requestMatchers("/swagger-ui/**").permitAll()
	            .requestMatchers(HttpMethod.GET, "/vehiculos/documento/estado/VENCIDO").permitAll()
	            .requestMatchers(HttpMethod.GET, "/vehiculos/placa/**").permitAll()
	            .requestMatchers(HttpMethod.GET, "/personas/tipo/**").permitAll()
	            .requestMatchers(HttpMethod.GET, "/personas/estado/PO").permitAll()
	            .requestMatchers(HttpMethod.GET, "/vehiculos/documento/por-vencer").permitAll()
	            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	            .anyRequest().authenticated()
	        )
	        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}

}
