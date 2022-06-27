package com.luna.vuelav.retrofit.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BoletoRequest {

    private List<Asiento> asientos = new ArrayList<>();
    private Date fecha;
    private List<Maleta> maletas = new ArrayList<>();
    private Pago pago;
    private Long pasajeroId;
    private Long vueloId;

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<Asiento> asientos) {
        this.asientos = asientos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<Maleta> getMaletas() {
        return maletas;
    }

    public void setMaletas(List<Maleta> maletas) {
        this.maletas = maletas;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public Long getPasajeroId() {
        return pasajeroId;
    }

    public void setPasajeroId(Long pasajeroId) {
        this.pasajeroId = pasajeroId;
    }

    public Long getVueloId() {
        return vueloId;
    }

    public void setVueloId(Long vueloId) {
        this.vueloId = vueloId;
    }
}

/*
{
  "asientos": [
    {
      "id": 0,
      "nombre": "string",
      "precio": 0,
      "tipoAsiento": {
        "id": 0,
        "nombre": "string"
      }
    }
  ],
  "fecha": "2022-06-27T01:47:47.249Z",
  "maletas": [
    {
      "peso": "string",
      "precio": 0
    }
  ],
  "pago": {
    "estado": true,
    "tipo": "string",
    "valor": 0
  },
  "pasajeroId": 0,
  "qr": "string",
  "vueloId": 0
}
 */