package com.luna.vuelav.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.luna.vuelav.retrofit.service.AuthService;
import com.luna.vuelav.retrofit.service.AvionService;
import com.luna.vuelav.retrofit.service.BoletoService;
import com.luna.vuelav.retrofit.service.ContactoService;
import com.luna.vuelav.retrofit.service.PasajeroService;
import com.luna.vuelav.retrofit.service.PromotionsService;
import com.luna.vuelav.retrofit.service.TokenService;
import com.luna.vuelav.retrofit.service.VueloService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiClient instance;
    private Retrofit retrofit;

    private final static String BASE_URL = "https://vuelav-api.herokuapp.com";
    //private final static String BASE_URL = "http://192.168.0.111:8080";

    private ApiClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public AuthService authService() {
        return retrofit.create(AuthService.class);
    }

    public PromotionsService promotionsService() {
        return retrofit.create(PromotionsService.class);
    }

    public PasajeroService pasajeroService() {
        return retrofit.create(PasajeroService.class);
    }

    public BoletoService boletoService() {
        return retrofit.create(BoletoService.class);
    }

    public VueloService vueloService() {
        return retrofit.create(VueloService.class);
    }

    public AvionService avionService() {
        return retrofit.create(AvionService.class);
    }

    public ContactoService contactoService() {
        return retrofit.create(ContactoService.class);
    }

    public TokenService tokenService() {
        return retrofit.create(TokenService.class);
    }

}
