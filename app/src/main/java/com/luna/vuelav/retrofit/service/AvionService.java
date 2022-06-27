package com.luna.vuelav.retrofit.service;

import com.luna.vuelav.retrofit.models.AvionAsientos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AvionService {

    @GET("/api/v1/aviones/{id}/asientos/disponible")
    Call<AvionAsientos> getById(@Path("id") long id);
}
