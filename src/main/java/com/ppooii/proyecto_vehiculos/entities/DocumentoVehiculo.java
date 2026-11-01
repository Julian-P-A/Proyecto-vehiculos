package com.ppooii.proyecto_vehiculos.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import com.ppooii.proyecto_vehiculos.entities.Documentos;
import com.ppooii.proyecto_vehiculos.entities.Vehiculos;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

import io.jsonwebtoken.io.Decoders;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table (name="documento_vehiculo")
public class DocumentoVehiculo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    @ManyToOne(optional = false)
    @JoinColumn(name = "VEHICULO_ID", nullable = false)
    @JsonIgnore 
    private Vehiculos vehiculo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "DOCUMENTO_ID", nullable = false)
    private Documentos documento;
    
    @JsonProperty("vehiculo")
    public Long getVehiculoId() {
        return vehiculo != null ? vehiculo.getId() : null;
    }

    @JsonProperty("documento")
    public Long getDocumentoId() {
        return documento != null ? documento.getId() : null;
    }
	
	@Column (name = "FECHA_EXPEDICION", nullable = false)
	private LocalDate fechaExpedicion;
	
	@Column (name = "FECHA_VENCIMIENTO", nullable = false)
	private LocalDate fechaVencimiento;
	
	@Column (name = "ESTADO", nullable = false)
	private String estado;
	
	@Lob
	@Column (name = "ARCHIVO_PDF", columnDefinition = "LONGBLOB")
	private byte[] archivoPdf;
	
	public DocumentoVehiculo() {}
	
	public DocumentoVehiculo(Vehiculos vehiculo, Documentos documento, LocalDate fechaExpedicion, LocalDate fechaVencimiento) {
		this.vehiculo = vehiculo;
		this.documento = documento;
		this.fechaExpedicion = fechaExpedicion;
		this.fechaVencimiento = fechaVencimiento;
	}
	
	public DocumentoVehiculo(Vehiculos vehiculo, Documentos documento, LocalDate fechaExpedicion, LocalDate fechaVencimiento, String estado) {
		this.vehiculo = vehiculo;
		this.documento = documento;
		this.fechaExpedicion = fechaExpedicion;
		this.fechaVencimiento = fechaVencimiento;
		this.estado = estado;
	}
	
	public DocumentoVehiculo(Vehiculos vehiculo, Documentos documento, LocalDate fechaExpedicion, LocalDate fechaVencimiento, String estado, String archivoPdf) {
		this.vehiculo = vehiculo;
		this.documento = documento;
		this.fechaExpedicion = fechaExpedicion;
		this.fechaVencimiento = fechaVencimiento;
		this.estado = estado;
		this.setArchivoPdf(archivoPdf);
	}
	
	
	
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public Vehiculos getVehiculo() { return vehiculo; }
	public void setVehiculo(Vehiculos vehiculo) { this.vehiculo = vehiculo; }
	
	public Documentos getDocumento() { return documento; }
	public void setDocumento(Documentos documento) { this.documento = documento; }
	
	public LocalDate getFechaExpedicion() { return fechaExpedicion; }
	public void setFechaExpedicion(LocalDate fechaExpedicion) { this.fechaExpedicion = fechaExpedicion; }
	
	public LocalDate getFechaVencimiento() { return fechaVencimiento; }
	public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
	
	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }
	
	public String getArchivoPdf() { 
		if (archivoPdf != null && archivoPdf.length > 0) {
	        return "Tiene archivo";
	    } else {
	        return "No tiene archivo";
	    }
	}
	
	public void setArchivoPdf(String archivoPdf) { 
		if (archivoPdf != null && !archivoPdf.isEmpty()) {
            this.archivoPdf = Decoders.BASE64.decode(archivoPdf);
        }
	}
	

}
