package com.ppooii.proyecto_vehiculos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ppooii.proyecto_vehiculos.entities.Usuarios;
import com.ppooii.proyecto_vehiculos.entities.UsuarioPK;

@Repository("IUsuariosRepo")
public interface UsuariosRepository extends JpaRepository<Usuarios, UsuarioPK> {
	
	@Query("SELECT usr FROM Usuarios usr WHERE usr.id.login = :login AND usr.id.persona = :persona")
	public abstract Usuarios getUsuarioANDPersona(@Param("persona") String persona, @Param("login") int login);
	
	@Query("SELECT usr FROM Usuarios usr WHERE usr.id.login = :login")
	public abstract Usuarios findByUsername(@Param("login") String login);
	
	@Query("SELECT usr FROM Usuarios usr WHERE usr.apiKey = :apikey")
    public abstract Usuarios findByApikey (@Param("apikey") String apikey);
}
