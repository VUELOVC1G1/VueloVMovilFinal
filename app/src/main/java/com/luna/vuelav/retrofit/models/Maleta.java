package com.luna.vuelav.retrofit.models;

import java.io.Serializable;

public class Maleta implements Serializable {
    private long id;
    private String peso;
    private double precio;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
