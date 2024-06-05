package com.example.ominformatics2.DataSource.Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClient {

    public static RetrofitApiClient apiClient;
    private static final String BASE_URL = "https://ominfo.in/test_app/";

    private Retrofit retrofit  = null;

    public static RetrofitApiClient getInstance(){
        if (apiClient == null){
            apiClient = new RetrofitApiClient();
        }
        return apiClient;
    }

    public Retrofit getClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

//        if (BuildConfig.DEBUG) {
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);   // development build
//        } else {
//            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);    // production build
//        }
        client.addInterceptor(interceptor);
        client.readTimeout(60, TimeUnit.SECONDS);
        client.writeTimeout(60, TimeUnit.SECONDS);
        client.connectTimeout(60, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client.build()).addConverterFactory(GsonConverterFactory.create()).build();

        return retrofit;
    }

}
