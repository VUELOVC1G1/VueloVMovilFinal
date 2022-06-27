package com.luna.vuelav.sqlite.models;

import java.io.Serializable;

public class PromocionM  implements Serializable {
    private Long id;
    private Long vueloId;
    private String descripcion;
    private int descuento;
    private double precio;
    private String fechaVuelo;
    private String rutaDescripcion;
    private String origen;
    private String destino;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVueloId() {
        return vueloId;
    }

    public void setVueloId(Long vueloId) {
        this.vueloId = vueloId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFechaVuelo() {
        return fechaVuelo;
    }

    public void setFechaVuelo(String fechaVuelo) {
        this.fechaVuelo = fechaVuelo;
    }

    public String getRutaDescripcion() {
        return rutaDescripcion;
    }

    public void setRutaDescripcion(String rutaDescripcion) {
        this.rutaDescripcion = rutaDescripcion;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
}
