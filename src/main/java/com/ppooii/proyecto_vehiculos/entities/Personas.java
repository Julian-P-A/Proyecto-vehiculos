package com.ppooii.proyecto_vehiculos.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.jsonwebtoken.io.Decoders;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "personas")
public class Personas implements Serializable{
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "ID_PERSONA", nullable = false, unique = true)
	private String idPersona;
	
	@Column(name = "TIPO_ID", nullable = false, length = 2)
	private String tipoId;
	
	@Column(name = "NOMBRES", nullable = false)
	private String nombres;
	
	@Column(name = "APELLIDOS", nullable = false)
	private String apellidos;
	
	@Column(name = "CORREO_ELECTRONICO", nullable = false)
	private String correoElectronico;
	
	@Column(name = "TIPO_PERSONA", nullable = false, length = 1)
	private String tipoPersona;
	
	@OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<VehiculoPersona> vehiculoPersona = new ArrayList<>();
	
	@Lob
	@Column (name = "LICENCIA", columnDefinition = "LONGBLOB")
	private byte[] licencia;
	
	@Column (name = "FECHA_VENCIMIENTO")
	private LocalDate fechaVencimiento;
	
	public Personas() {}
	
	public Personas(String idPersona, String tipoId, String nombres, String apellidos, String correoElectronico, String tipoPersona) {
		this.idPersona = idPersona;
		this.tipoId = tipoId;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.correoElectronico = correoElectronico;
		this.tipoPersona = tipoPersona;
	}
	
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public String getIdPersona() { return idPersona; }
	public void setIdPersona(String idPersona) { this.idPersona = idPersona; }
	
	public String getTipoId() { return tipoId; }
	public void setTipoId(String tipoId) { this.tipoId = tipoId; }
	
	public String getNombres() { return nombres; }
	public void setNombres(String nombres) { this.nombres = nombres; }
	
	public String getApellidos() { return apellidos; }
	public void setApellidos(String apellidos) { this.apellidos = apellidos; }
	
	public String getCorreoElectronico() { return correoElectronico; }
	public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
	
	public String getTipoPersona() { return tipoPersona; }
	public void setTipoPersona(String tipoPersona) { this.tipoPersona = tipoPersona; }
	
	public List<VehiculoPersona> getVehiculoPersona() { return vehiculoPersona; }
	public void setVehiculoPersona(List<VehiculoPersona> vehiculoPersona) { this.vehiculoPersona = vehiculoPersona; }
	
	public String getLicencia() { 
		if (licencia != null && licencia.length > 0) {
	        return "Tiene archivo";
	    } else {
	        return "No tiene archivo";
	    }
	}
	
	public void setLicencia(String licencia) { 
		if (licencia != null && !licencia.isEmpty()) {
            this.licencia = Decoders.BASE64.decode(licencia);
        }
	}
	
	public LocalDate getFechaVencimiento() { return fechaVencimiento; }
	public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

}
