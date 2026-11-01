package com.ppooii.proyecto_vehiculos.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "vehiculos")
public class Vehiculos implements Serializable{	
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "TIPO_VEHICULO", nullable = false)
	private String tipoVehiculo;
	
	@Column(name = "PLACA", nullable = false, length = 6, unique = true)
	private String placa;
	
	@Column(name = "TIPO_SERVICIO", nullable = false, length = 2)
	private String tipoServicio;
	
	@Column(name = "TIPO_COMBUSTIBLE", nullable = false, length = 20)
	private String tipoCombustible;
	
	@Column(name = "CAPACIDAD", nullable = false)
	private Integer capacidadPasajeros;
	
	@Column(name = "COLOR", nullable = false, length = 20)
	private String color;
	
	@Column(name = "MODELO", nullable = false, length = 4)
	private Integer modelo;
	
	@Column(name = "MARCA", nullable = false, length = 20)
	private String marca;
	
	@Column(name = "LINEA", nullable = false, length = 20)
	private String linea;
	
	@OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<DocumentoVehiculo> documentosVehiculo = new ArrayList<>();
	
		@OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
		public List<VehiculoPersona> vehiculoPersona = new ArrayList<>();
	
	public Vehiculos() {}
	
	public Vehiculos(String tipoVehiculo, String placa, String tipoServicio, String tipoCombustible,
			Integer capacidadPasajeros, String color, Integer modelo, String marca, String linea) {
		this.tipoVehiculo = tipoVehiculo;
		this.placa = placa;
		this.tipoServicio = tipoServicio;
		this.tipoCombustible = tipoCombustible;
		this.capacidadPasajeros = capacidadPasajeros;
		this.color = color;
		this.modelo = modelo;
		this.marca = marca;
		this.linea = linea;
	}
	
	public Vehiculos(String tipoVehiculo, String placa, String tipoServicio, String tipoCombustible,
			Integer capacidadPasajeros, String color, Integer modelo, String marca, String linea, List<DocumentoVehiculo> documentosVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
		this.placa = placa;
		this.tipoServicio = tipoServicio;
		this.tipoCombustible = tipoCombustible;
		this.capacidadPasajeros = capacidadPasajeros;
		this.color = color;
		this.modelo = modelo;
		this.marca = marca;
		this.linea = linea;
		this.documentosVehiculo = documentosVehiculo;
	}
	
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public String getTipoVehiculo() { return tipoVehiculo; }
	public void setTipoVehiculo(String tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }
	
	public String getPlaca() { return placa; }
	public void setPlaca(String placa) { this.placa = placa; }
	
	public String getTipoServicio() { return tipoServicio; }
	public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }
	
	public String getTipoCombustible() { return tipoCombustible; }
	public void setTipoCombustible(String tipoCombustible) { this.tipoCombustible = tipoCombustible; }
	
	public Integer getCapacidadPasajeros() { return capacidadPasajeros; }
	public void setCapacidadPasajeros(Integer capacidadPasajeros) { this.capacidadPasajeros = capacidadPasajeros; }
	
	public String getColor() { return color; }
	public void setColor(String color) { this.color = color; }
	
	public Integer getModelo() { return modelo; }
	public void setModelo(Integer modelo) { this.modelo = modelo; }
	
	public String getMarca() { return marca; }
	public void setMarca(String marca) { this.marca = marca; }
	
	public String getLinea() { return linea; }
	public void setLinea(String linea) { this.linea = linea; }
	
	public List<DocumentoVehiculo> getDocumentosVehiculo() { return documentosVehiculo; }
	public void setDocumentosVehiculo(List<DocumentoVehiculo> documentosVehiculo) { this.documentosVehiculo = documentosVehiculo; }
	
	public List<VehiculoPersona> getVehiculoPersona() { return vehiculoPersona; }
	public void setVehiculoPersona(List<VehiculoPersona> vehiculoPersona) { this.vehiculoPersona = vehiculoPersona; }
	

}
