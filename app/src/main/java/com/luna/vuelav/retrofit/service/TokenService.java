package com.luna.vuelav.retrofit.service;

import com.luna.vuelav.retrofit.models.UsuarioToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TokenService {
    @POST("/api/v1/token")
    Call<ResponseBody> save(@Body UsuarioToken request);
}
