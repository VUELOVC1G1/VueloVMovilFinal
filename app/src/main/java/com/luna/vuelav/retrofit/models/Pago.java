package com.luna.vuelav.retrofit.models;

import java.io.Serializable;

public class Pago implements Serializable {
    private boolean estado;
    private String tipo;
    private double valor;

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
