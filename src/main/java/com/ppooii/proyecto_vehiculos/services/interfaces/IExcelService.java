package com.ppooii.proyecto_vehiculos.services.interfaces;

public interface IExcelService {
	
	boolean cargarDatosExcel(String personaId, String vehiculoId, String codigoRuta, String ubicacion, String ordenParada, String login,  Double idCargue);
	
	boolean validarDatosCargue(Double idCargue);
	
	boolean procesarDatosExcel(Double idCargue);
}
