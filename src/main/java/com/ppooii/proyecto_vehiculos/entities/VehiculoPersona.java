package com.ppooii.proyecto_vehiculos.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import com.ppooii.proyecto_vehiculos.entities.Personas;
import com.ppooii.proyecto_vehiculos.entities.Vehiculos;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table (name="vehiculo_persona")
public class VehiculoPersona implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    @ManyToOne(optional = false)
    @JoinColumn(name = "VEHICULO_ID", nullable = false)
    @JsonIgnore 
    private Vehiculos vehiculo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PERSONA_ID", nullable = false)
    private Personas persona;
    
    @JsonProperty("vehiculo")
    public Long getVehiculoId() {
        return vehiculo != null ? vehiculo.getId() : null;
    }

    @JsonProperty("persona")
    public Long getPersonaId() {
        return persona != null ? persona.getId() : null;
    }
    
    @Column (name = "FECHA_ASOCIACION", nullable = false)
	private LocalDate fechaAsociacion;
	
	@Column (name = "ESTADO", nullable = false, length = 2)
	private String estado;
	
	public VehiculoPersona() {}
	
	public VehiculoPersona(Vehiculos vehiculo, Personas persona, LocalDate fechaAsociacion) {
		this.vehiculo = vehiculo;
		this.persona = persona;
		this.fechaAsociacion = fechaAsociacion;
	}
	
	public VehiculoPersona(Vehiculos vehiculo, Personas persona, LocalDate fechaAsociacion, String estado) {
		this.vehiculo = vehiculo;
		this.persona = persona;
		this.fechaAsociacion = fechaAsociacion;
		this.estado = estado;
	}
	
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public Vehiculos getVehiculo() { return vehiculo; }
	public void setVehiculo(Vehiculos vehiculo) { this.vehiculo = vehiculo; }
	
	public Personas getPersona() { return persona; }
	public void setPersona(Personas persona) { this.persona = persona; }
	
	public LocalDate getFechaAsociacion() { return fechaAsociacion; }
	public void setFechaAsociacion(LocalDate fechaAsociacion) { this.fechaAsociacion = fechaAsociacion; }
	
	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }
	
    
}