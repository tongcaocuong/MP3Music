package com.doan.mp3music.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

abstract public class ApiBuilder {

    public static String URL = "https://caocuongttn3.000webhostapp.com/";
//    public static String URL = "http://192.168.1.15/music/";
    private static Api api;

    public static Api getInstance() {
        if (api == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            api = new Retrofit.Builder()
                    .baseUrl(URL+ "api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                    .create(Api.class);
        }
        return api;
    }
}
