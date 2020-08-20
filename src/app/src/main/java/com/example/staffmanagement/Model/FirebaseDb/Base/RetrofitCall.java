package com.example.staffmanagement.Model.FirebaseDb.Base;

import com.example.staffmanagement.Model.FirebaseDb.StringApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCall {
    public static Retrofit create() {
        return new Retrofit.Builder()
                .baseUrl(StringApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
