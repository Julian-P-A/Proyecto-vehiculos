package com.ppooii.proyecto_vehiculos.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ppooii.proyecto_vehiculos.entities.DocumentoVehiculo;
import com.ppooii.proyecto_vehiculos.entities.Vehiculos;

@Repository("IDocumentoVehiculoRepo")
public interface DocumentoVehiculoRepository extends JpaRepository<DocumentoVehiculo, Long> {
	
	 @Query("SELECT d.vehiculo FROM DocumentoVehiculo d WHERE d.fechaVencimiento BETWEEN CURRENT_DATE AND :fechaLimite")
	 public abstract List<Vehiculos> findVehiculosConDocumentosPorVencer(@Param("fechaLimite") LocalDate fechaLimite);
	 
	 public abstract List<DocumentoVehiculo> findAll();

}
