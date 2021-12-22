package com.example.myapp_sherkat.classs;

import android.os.Build;

import com.example.myapp_sherkat.interfaces.ApiInterface;

import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiClient mInstance;
    private Retrofit retrofit;
    private String url="https://advertisingisfahan.com/application/";


    private ApiClient() {
        retrofit = new Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized ApiClient getInstance() {
        if (mInstance == null) {
            mInstance = new ApiClient();
        }
        return mInstance;
    }

    public ApiInterface getApi() {
        return retrofit.create(ApiInterface.class);
    }

}
