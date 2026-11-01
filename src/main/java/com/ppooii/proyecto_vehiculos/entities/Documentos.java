package com.ppooii.proyecto_vehiculos.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "documentos")
public class Documentos implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "CODIGO", nullable = false, unique = true)
	private int codigo;
	
	@Column(name = "NOMBRE_DOCUMENTO", nullable = false)
	private String nombreDocumento;
	
	@Column(name = "TIPO_APLICA", nullable = false, length = 2)
	private String tipoAplica;
	
	@Column(name = "OBLIGATORIO", nullable = false, length = 2)
	private String obligatorio;
	
	@Column(name = "DESCRIPCION", nullable = false)
	private String descripcion;
	
	@OneToMany(mappedBy = "documento", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DocumentoVehiculo> documentosVehiculo = new ArrayList<>();
	
	public Documentos() {}
	
	public Documentos(int codigo) {
		this.codigo = codigo;
	}
	
	public Documentos(int codigo, String nombreDocumento, String tipoAplica, String obligatorio, String descripcion) {
		this.codigo = codigo;
		this.nombreDocumento = nombreDocumento;
		this.tipoAplica = tipoAplica;
		this.obligatorio = obligatorio;
		this.descripcion = descripcion;
	}
	
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public int getCodigo() { return codigo; }
	public void setCodigo(int codigo) { this.codigo = codigo; }
	
	public String getNombreDocumento() { return nombreDocumento; }
	public void setNombreDocumento(String nombreDocumento) { this.nombreDocumento = nombreDocumento; }
	
	public String getTipoAplica() { return tipoAplica; }
	public void setTipoAplica(String tipoAplica) { this.tipoAplica = tipoAplica; }
	
	public String getObligatorio() { return obligatorio; }
	public void setObligatorio(String obligatorio) { this.obligatorio = obligatorio; }
	
	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
	
	public List<DocumentoVehiculo> getDocumentosVehiculo() { return documentosVehiculo; }
	public void setDocumentosVehiculo(List<DocumentoVehiculo> documentosVehiculo) { this.documentosVehiculo = documentosVehiculo; }

}
