package com.ppooii.proyecto_vehiculos.services.interfaces;

import java.util.List;
import java.util.Optional;


import com.ppooii.proyecto_vehiculos.entities.UsuarioPK;
import com.ppooii.proyecto_vehiculos.entities.Usuarios;

public interface IUsuariosService {
	
	public boolean updatePassword(String login, String password);
	
	public List<Usuarios> findAll();
	public String getNewApiKey(UsuarioPK id);
	public Optional<Usuarios> getUsuarioById (UsuarioPK id);

}
