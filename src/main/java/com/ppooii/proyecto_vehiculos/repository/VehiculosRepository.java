package com.ppooii.proyecto_vehiculos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ppooii.proyecto_vehiculos.entities.Vehiculos;

@Repository("IVehiculosRepo")
public interface VehiculosRepository extends JpaRepository<Vehiculos, Long> {
	
	public abstract Optional<Vehiculos> findByPlaca(String placa);
	
	public abstract Optional<Vehiculos> findById(Long id);
	
	public abstract List<Vehiculos> findByTipoVehiculo(String tipoVehiculo);
	
    @Query("SELECT v FROM Vehiculos v JOIN v.documentosVehiculo vd WHERE vd.documento.codigo = :codigo")
	public abstract List<Vehiculos> findByDocumentoCodigo(@Param("codigo") int codigo);
	
	@Query("SELECT v FROM Vehiculos v JOIN v.documentosVehiculo vd WHERE vd.estado = :estado")
	public abstract List<Vehiculos> findByDocumentoEstado(@Param("estado") String estado);
	
	public abstract List<Vehiculos> findAll();

}
