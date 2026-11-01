package com.ppooii.proyecto_vehiculos.entities;

import java.io.Serializable;

import com.ppooii.proyecto_vehiculos.entities.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "trayecto")
public class Trayecto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_TRAYECTO")
	private Long id;

	@Column(name = "PERSONA_ID", nullable = false)
	private int persona;

	@Column(name = "VEHICULO_ID", nullable = false)
	private int vehiculo;
	
	@Column(name = "CODIGO_RUTA")
	public String codigoRuta;
	
	@Column(name = "UBICACION")
	public String ubicacion;
	
	@Column(name = "ORDEN_PARADA")
	public int ordenParada;
	
	@Column(name = "LONGITUD")
	private Double longitud;
	
	@Column(name = "LATITUD")
	private Double latitud;
	
	@Column(name = "LOGIN")
	private String login;
	
	public Trayecto() {}
	
	public Trayecto(Long id, int persona, int vehiculo, String codigoRuta, String ubicacion, int ordenParada, String login) {
		this.id = id;
		this.persona = persona;
		this.vehiculo = vehiculo;
		this.codigoRuta = codigoRuta;
		this.ubicacion = ubicacion;
		this.ordenParada = ordenParada;
		this.login = login;
	}
	
	public Trayecto( int persona, int vehiculo, String codigoRuta, String ubicacion, int ordenParada, String login) {
		this.persona = persona;
		this.vehiculo = vehiculo;
		this.codigoRuta = codigoRuta;
		this.ubicacion = ubicacion;
		this.ordenParada = ordenParada;
		this.login = login;
	}
	
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public int getPersona() { return persona; }
	public void setPersona(int persona) { this.persona = persona; }
	
	public int getVehiculo() { return vehiculo; }
	public void setVehiculo(int vehiculo) { this.vehiculo = vehiculo; }
	
	public String getCodigoRuta() { return codigoRuta; }
	public void setCodigoRuta(String codigoRuta) { this.codigoRuta = codigoRuta; }
	
	public String getUbicacion() { return ubicacion; }
	public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
	
	public int getOrdenParada() { return ordenParada; }
	public void setOrdenParada(int ordenParada) { this.ordenParada = ordenParada; }
	
	public double getLongitud() { return longitud; }
	public void setLongitud(Double longitud) { this.longitud = longitud; }
	
	public double getLatitud() { return latitud; }
	public void setLatitud(Double latitud) { this.latitud = latitud; }
	
	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }

}
