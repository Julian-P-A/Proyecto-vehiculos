package com.ppooii.proyecto_vehiculos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ppooii.proyecto_vehiculos.entities.UsuarioPK;
import com.ppooii.proyecto_vehiculos.entities.Usuarios;
import com.ppooii.proyecto_vehiculos.repository.PersonasRepository;
import com.ppooii.proyecto_vehiculos.repository.UsuariosRepository;
import com.ppooii.proyecto_vehiculos.repository.VehiculosRepository;
import com.ppooii.proyecto_vehiculos.services.interfaces.IUsuariosService;

@Service
public class UsuariosService implements IUsuariosService, UserDetailsService {
	@Autowired
	@Qualifier("IPersonasRepo")
	private PersonasRepository personasRepository;
	
	@Autowired
	@Qualifier("IVehiculosRepo")
	private VehiculosRepository vehiculosRepository;
	
	@Autowired
	@Qualifier("IUsuariosRepo")
	private UsuariosRepository usuariosRepository;
	
	//Logs
	//Wrong Logs
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(VehiculosService.class);
	
	@Override
	public boolean updatePassword(String login, String password) {
		if (login == null || login.isEmpty()) {
			logger.error("Error: debes ingresar el login del usuario");
			return false;
		}
		
		if (password == null || password.isEmpty()) {
			logger.error("Error: debes ingresar la password del usuario");
			return false;
		}
		
		try {
			Optional<Usuarios> usuarioOp = Optional.ofNullable(usuariosRepository.findByUsername(login));
	        if (usuarioOp.isEmpty()) {
	            logger.error("Error: no se encontró el usuario {}", login);
	            return false;
	        }
	        
	        Usuarios usuario = usuarioOp.get();
	        
	        usuario.setPassword(password);
	        usuariosRepository.save(usuario);
	        
	        return true;
	        
		}catch(Exception e) {
			logger.error("Error editar_password: El password no se ha editado");
			return false;
		}
				
	}


	@Override
	public List<Usuarios> findAll() {
		return usuariosRepository.findAll();
	}
	
	@Override
	public String getNewApiKey(UsuarioPK id) {
		
		  if (id == null) {
		        logger.error("Error: debes ingresar el id del usuario");
		        return null;
		    }

		    if (id.getLogin() == null || id.getLogin().isEmpty()) {
		        logger.error("Error: debes ingresar el login del usuario");
		        return null;
		    }

		    try {
		        Optional<Usuarios> usuarioOp = usuariosRepository.findById(id);
		        if (usuarioOp.isEmpty()) {
		            logger.error("Error: no se encontró el usuario con ID {}", id);
		            return null;
		        }

		        Usuarios usuario = usuarioOp.get();

		        String apiKey = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

		        usuario.setApiKey(apiKey);
		        usuariosRepository.save(usuario);

		        logger.info("Nueva API key generada para el usuario {}", id.getLogin());
		        return apiKey;

		    } catch (Exception e) {
		        logger.error("Error al generar API key para el usuario {}", id.getLogin(), e);
		        return null;
		    }
		
	}

	@Override
	public Optional<Usuarios> getUsuarioById(UsuarioPK id) {
		return Optional.of(usuariosRepository.getUsuarioANDPersona(id.getLogin(),id.getPersona()));
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
System.out.println("Buscar el usuario con el repositorio y si existe lanza la excepcion");
		
		Usuarios appUser = usuariosRepository.findByUsername(username);
		List grantList = new ArrayList();
		System.out.println("Crear el objeto en userDetails que va a ir en sesion y devolverlo");
		
		UserDetails user = (UserDetails) new User(appUser.getId().getLogin(), appUser.getPassword(), grantList);
		System.out.println("Use:[" + user + "]");
		return user;
	}

}
