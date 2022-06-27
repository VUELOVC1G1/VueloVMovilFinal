package com.luna.vuelav.retrofit.service;

import com.luna.vuelav.retrofit.models.Boleto;
import com.luna.vuelav.retrofit.models.BoletoRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BoletoService {

    @GET("/api/v1/boletos/pasajero/{id}")
    Call<List<Boleto>> getBoletosByPasajeroId(@Path("id") long id);

    @POST("/api/v1/boletos/")
    Call<Void> save(@Body BoletoRequest request);

}
