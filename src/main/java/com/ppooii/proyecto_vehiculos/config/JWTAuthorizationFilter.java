package com.ppooii.proyecto_vehiculos.config;

import static com.ppooii.proyecto_vehiculos.config.model.Constants.HEADER_AUTHORIZATION_KEY;
import static com.ppooii.proyecto_vehiculos.config.model.Constants.SUPER_SECRET_KEY;
import static com.ppooii.proyecto_vehiculos.config.model.Constants.TOKEN_BEARER_PREFIX;
import static com.ppooii.proyecto_vehiculos.config.model.Constants.getSigningKey;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ppooii.proyecto_vehiculos.repository.UsuariosRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter  {
	
	@Autowired
	private UsuariosRepository usuarioRepository;
	
	private Claims setSigningKey(HttpServletRequest request) {
		String jwtToken = request
				.getHeader(HEADER_AUTHORIZATION_KEY)
				.replace(TOKEN_BEARER_PREFIX,"");
		
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey(SUPER_SECRET_KEY))
				.build()
				.parseClaimsJws(jwtToken)
				.getBody();
	}
	
	@SuppressWarnings("unchecked")
	private void setAuthentication(Claims claims) {
		List<String> authorities = (List<String>) claims.get("authorities");
		
		UsernamePasswordAuthenticationToken auth =
				new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream().map(SimpleGrantedAuthority:: new ).collect(Collectors.toList()));
		
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	private boolean isJWTValid(HttpServletRequest request) {
		String authenticationHeader = request.getHeader(HEADER_AUTHORIZATION_KEY);
		return authenticationHeader != null && authenticationHeader.startsWith(TOKEN_BEARER_PREFIX);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws IOException, ServletException {

	    try {
	        if (isJWTValid(request)) {
	            Claims claims = setSigningKey(request);
	            if (claims.get("authorities") != null) {
	                setAuthentication(claims);
	            } else {
	                SecurityContextHolder.clearContext();
	            }

	            String apiKey = request.getHeader("x-api-key");
	            if (apiKey == null || usuarioRepository.findByApikey(apiKey) == null) {
	                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "API Key inválida o faltante");
	                return;
	            }
	        } else {
	            SecurityContextHolder.clearContext();
	        }

	        filterChain.doFilter(request, response);

	    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
	        response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
	    }
	}
}
