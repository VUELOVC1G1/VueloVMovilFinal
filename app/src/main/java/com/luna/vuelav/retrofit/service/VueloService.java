package com.luna.vuelav.retrofit.service;

import com.luna.vuelav.retrofit.models.Vuelo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VueloService {

    @GET("/api/v1/vuelos")
    Call<List<Vuelo>> getAll();

    @GET("/api/v1/vuelos/{id}")
    Call<Vuelo> getById(@Path("id") long id);
}
