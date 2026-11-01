package com.ppooii.proyecto_vehiculos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ppooii.proyecto_vehiculos.entities.Documentos;

@Repository("IDocumentosRepo")
public interface DocumentosRepository extends JpaRepository<Documentos, Long> {
	
	public abstract Optional<Documentos> findById(Long id);
	
	public abstract Optional<Documentos> findByCodigo(int codigo);
	
	public abstract Optional<Documentos> findByNombreDocumento(String nombreDocumento);
	
	@Query("SELECT v FROM Documentos v JOIN v.documentosVehiculo vd WHERE vd.vehiculo.id = :id")
	public abstract List<Documentos> findByVehiculoId(@Param("id") Long id);
	
	@Query("SELECT v FROM Documentos v JOIN v.documentosVehiculo vd WHERE vd.vehiculo.placa = :placa")
	public abstract List<Documentos> findByVehiculoPlaca(@Param("placa") String placa);
	
	public abstract List<Documentos> findAll();

}
