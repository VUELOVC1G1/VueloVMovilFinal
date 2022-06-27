package com.luna.vuelav.retrofit.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Boleto implements Serializable {
    private long id;
    private Date fecha;
    private String qr;
    private List<Maleta> maletas = new ArrayList<>();
    private List<Asiento> asientos = new ArrayList<>();
    private Pasajero pasajero;
    private Vuelo vuelo;
    private Pago pago;

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public List<Maleta> getMaletas() {
        return maletas;
    }

    public void setMaletas(List<Maleta> maletas) {
        this.maletas = maletas;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<Asiento> asientos) {
        this.asientos = asientos;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public void setPasajero(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }
}
