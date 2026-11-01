package com.ppooii.proyecto_vehiculos.services.interfaces;

import java.util.List;

import com.ppooii.proyecto_vehiculos.entities.Personas;
import com.ppooii.proyecto_vehiculos.entities.Trayecto;

public interface ITrayectoService {
	
	public boolean addTrayecto(Trayecto trayecto);
	public boolean updateTrayecto(Trayecto trayecto);
	public boolean deleteTrayecto(Long id);
	
	List<Trayecto> findAll();
	List<Trayecto> findByVehiculo_Placa(String placa);
	List<Trayecto> findByCodigoRuta(String codigoRuta);
	List<String> findDistinctCodigoRutaByPersonaIdentificacion(String idPersona);

}
