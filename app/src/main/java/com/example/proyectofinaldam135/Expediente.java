package com.example.proyectofinaldam135;

public class Expediente {
    private int id;
    private String usuario;
    private String cliente;
    private String telefono;
    private String direccion;
    private String problema;
    private String tipoServicio;
    private String urgencia;
    private String fecha;



    public Expediente(int id, String usuario, String cliente, String telefono,
                      String direccion, String problema, String tipoServicio,
                      String urgencia, String fecha) {
        this.id = id;
        this.usuario = usuario;
        this.cliente = cliente;
        this.telefono = telefono;
        this.direccion = direccion;
        this.problema = problema;
        this.tipoServicio = tipoServicio;
        this.urgencia = urgencia;
        this.fecha = fecha;
    }

    // Getters
    public int getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getCliente() { return cliente; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public String getProblema() { return problema; }
    public String getTipoServicio() { return tipoServicio; }
    public String getUrgencia() { return urgencia; }
    public String getFecha() { return fecha; }
}