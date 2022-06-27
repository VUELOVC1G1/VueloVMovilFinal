package com.luna.vuelav.retrofit.service;


import com.luna.vuelav.retrofit.models.LoginRequest;
import com.luna.vuelav.retrofit.models.Pasajero;
import com.luna.vuelav.retrofit.models.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("/api/v1/auth/login")
    Call<Usuario> login(@Body LoginRequest request);


    @POST("/api/v1/auth/signup/pasajero")
    Call<Pasajero> registro(@Body Pasajero request);
}

