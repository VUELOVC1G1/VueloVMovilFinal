package com.luna.vuelav.retrofit.service;

import com.luna.vuelav.retrofit.models.Pasajero;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PasajeroService {

    @GET("/api/v1/pasajeros/usuario/{id}")
    Call<Pasajero> findPasajeroByUserId(@Path("id") long id);

    @PUT("/api/v1/pasajeros/{idPasajero}")
    Call<Pasajero> update(@Path("idPasajero") long id, @Body Pasajero pasajero);
}
