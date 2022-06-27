package com.luna.vuelav.retrofit.service;

import com.luna.vuelav.retrofit.models.Contacto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ContactoService {

    @POST("/api/v1/contactanos")
    Call<Void> save(@Body Contacto request);
}
