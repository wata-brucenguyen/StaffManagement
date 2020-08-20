package com.example.staffmanagement.MVVM.Model.FirebaseDb.Request;

import com.example.staffmanagement.MVVM.Model.Entity.Request;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RequestApi {
    @GET("database/Request.json")
    Call<Map<String,Object>> getAll();

    @POST("database/Request/.json")
    Call<Request> post( @Body Request request);
}