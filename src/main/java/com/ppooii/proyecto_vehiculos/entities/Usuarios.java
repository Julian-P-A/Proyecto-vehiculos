package com.ppooii.proyecto_vehiculos.entities;

import java.io.Serializable;

import com.ppooii.proyecto_vehiculos.entities.UsuarioPK;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "usuarios")
public class Usuarios implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
	private UsuarioPK id;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "APIKEY", nullable = false, unique = true)
    private String apiKey;

    public Usuarios() {}

    public Usuarios( UsuarioPK id, String password) {
		this.id = id;
        this.password = password;
    }
    
    public Usuarios( UsuarioPK id, String password, String apiKey) {
		this.id = id;
        this.password = password;
		this.apiKey = apiKey;
    }

	public UsuarioPK getId() { return id; }
	public void setId(UsuarioPK id) { this.id = id; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
}
