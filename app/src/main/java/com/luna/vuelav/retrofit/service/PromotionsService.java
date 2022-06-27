package com.luna.vuelav.retrofit.service;

import com.luna.vuelav.retrofit.models.Promocion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PromotionsService {

    @GET("/api/v1/promociones")
    Call<Promocion> getAll();

    @GET("/api/v1/promociones/comerciales")
    Call<List<Promocion>> getAllComerciales();

    @GET("/api/v1/promociones/vuelo/{vueloId}")
    Call<List<Promocion>> getPromocionesByVuelo(@Path("vueloId") long id);
}
