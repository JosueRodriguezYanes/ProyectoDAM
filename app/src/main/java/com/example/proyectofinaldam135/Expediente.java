package com.example.proyectofinaldam135;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expedientes")
public class Expediente {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    private String usuario;

    private String cliente;
    private String telefono;
    private String direccion;

    @NonNull
    private String problema;

    private String tipoServicio;
    private String urgencia;
    private String fecha;
    private String foto;

    public Expediente(@NonNull String usuario, String cliente, String telefono,
                      String direccion, @NonNull String problema, String tipoServicio,
                      String urgencia, String fecha, String foto) {
        this.usuario = usuario;
        this.cliente = cliente;
        this.telefono = telefono;
        this.direccion = direccion;
        this.problema = problema;
        this.tipoServicio = tipoServicio;
        this.urgencia = urgencia;
        this.fecha = fecha;
        this.foto = foto;
    }

    // Getters y Setters (mantener @NonNull donde corresponda)
    @NonNull
    public int getId() { return id; }
    public void setId(@NonNull int id) { this.id = id; }

    @NonNull
    public String getUsuario() { return usuario; }
    public void setUsuario(@NonNull String usuario) { this.usuario = usuario; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    @NonNull
    public String getProblema() { return problema; }
    public void setProblema(@NonNull String problema) { this.problema = problema; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public String getUrgencia() { return urgencia; }
    public void setUrgencia(String urgencia) { this.urgencia = urgencia; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
}