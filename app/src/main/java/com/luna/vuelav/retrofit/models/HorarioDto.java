package com.luna.vuelav.retrofit.models;

import java.io.Serializable;
import java.util.Date;

public class HorarioDto implements Serializable {
    private long id;
    private Date fechaInicio;
    private Date fechaFin;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
