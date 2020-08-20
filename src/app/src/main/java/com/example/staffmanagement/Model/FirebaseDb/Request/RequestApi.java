package com.example.staffmanagement.Model.FirebaseDb.Request;

import com.example.staffmanagement.Model.Entity.Request;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RequestApi {
    @GET("database/Request.json")
    Call<Map<String,Object>> getAll();

    @POST("database/Request/.json")
    Call<Request> post( @Body Request request);
}