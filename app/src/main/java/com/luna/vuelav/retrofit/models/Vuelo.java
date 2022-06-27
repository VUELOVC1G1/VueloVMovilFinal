package com.luna.vuelav.retrofit.models;

import java.io.Serializable;
import java.util.Date;

public class Vuelo implements Serializable {
    private long id;
    private Date fechaCreacion;
    private double precio;
    private String observacion;
    private Date fechaVuelo;
    private boolean estado;
    private String salaEspera;

    private HorarioDto horarioResponse;
    private RutaDto rutaResponse;
    private TipoVuelo tipoVueloResponse;
    private Avion avionResponse;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaVuelo() {
        return fechaVuelo;
    }

    public void setFechaVuelo(Date fechaVuelo) {
        this.fechaVuelo = fechaVuelo;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getSalaEspera() {
        return salaEspera;
    }

    public void setSalaEspera(String salaEspera) {
        this.salaEspera = salaEspera;
    }

    public HorarioDto getHorarioResponse() {
        return horarioResponse;
    }

    public void setHorarioResponse(HorarioDto horarioResponse) {
        this.horarioResponse = horarioResponse;
    }

    public RutaDto getRutaResponse() {
        return rutaResponse;
    }

    public void setRutaResponse(RutaDto rutaResponse) {
        this.rutaResponse = rutaResponse;
    }

    public TipoVuelo getTipoVueloResponse() {
        return tipoVueloResponse;
    }

    public void setTipoVueloResponse(TipoVuelo tipoVueloResponse) {
        this.tipoVueloResponse = tipoVueloResponse;
    }

    public Avion getAvionResponse() {
        return avionResponse;
    }

    public void setAvionResponse(Avion avionResponse) {
        this.avionResponse = avionResponse;
    }
}
