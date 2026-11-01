package com.ppooii.proyecto_vehiculos.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.ppooii.proyecto_vehiculos.entities.DocumentoVehiculo;
import com.ppooii.proyecto_vehiculos.entities.Vehiculos;

public interface IVehiculosService {
	
	public boolean addVehiculo(Vehiculos vehiculo);
	public boolean updateVehiculo(Vehiculos vehiculo);
	public boolean deleteVehiculo(Long id);
	
	List<Vehiculos> findAll();
    Optional<Vehiculos> findById(Long id);
    Optional<Vehiculos> findByPlaca(String placa);
	
	List<Vehiculos> findVehiculosByTipoVehiculos(String tipoVehiculo);
	List<Vehiculos> findVehiculosByDocumentoCodigo(int codigo);
	List<Vehiculos> findVehiculosByDocumentoEstadoList(String estado);
 	
	Optional<Vehiculos> addDocumentoToVehiculo(Long vehiculoId, Long documentoId, String fechaExp, String fechaVenc);
	public boolean updateEstadoDocumento(Long idVehiculo, Long idDocumentoVehiculo, String nuevoEstado);
	public boolean addPdfDocumento(Long idVehiculo, Long idDocumentoVehiculo, String documentoPdf);
	public List<Vehiculos> getVehiculosConDocumentosPorVencer(int dias);

}
