package com.luna.vuelav.retrofit.models;

public class Promocion {
    private Long id;
    private String descripcion;
    private int descuento;
    private Vuelo vuelo;

    public Promocion(Long id, String descripcion, int descuento, Vuelo vuelo) {
        this.id = id;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.vuelo = vuelo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }
}