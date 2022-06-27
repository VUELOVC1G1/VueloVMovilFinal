package com.luna.vuelav.retrofit.models;

import java.io.Serializable;
import java.util.Date;

public class Pasajero implements Serializable {
    private Long id;
    private String cedula;
    private String nombre;
    private String apellido;

    private Date fechaNacimiento;
    private UsuarioCorreo usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public UsuarioCorreo getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioCorreo usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Pasajero{" +
                "id=" + id +
                ", cedula='" + cedula + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", usuario=" + usuario +
                '}';
    }
}
