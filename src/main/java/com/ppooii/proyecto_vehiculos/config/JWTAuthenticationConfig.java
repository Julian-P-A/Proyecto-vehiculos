package com.ppooii.proyecto_vehiculos.config;

import static com.ppooii.proyecto_vehiculos.config.model.Constants.SUPER_SECRET_KEY;
import static com.ppooii.proyecto_vehiculos.config.model.Constants.TOKEN_EXPIRATION_TIME;
import static com.ppooii.proyecto_vehiculos.config.model.Constants.getSigningKey;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Configuration
public class JWTAuthenticationConfig {
	
	public String getJWTToken(String username) {
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("PPOOII_JWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
				.signWith(getSigningKey(SUPER_SECRET_KEY), SignatureAlgorithm.HS512).compact();
		
		return "Bearer " + token;
	}

}
