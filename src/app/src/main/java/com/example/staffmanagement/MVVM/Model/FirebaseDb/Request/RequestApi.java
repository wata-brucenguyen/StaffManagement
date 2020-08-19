package com.example.staffmanagement.MVVM.Model.FirebaseDb.Request;

import com.example.staffmanagement.MVVM.Model.Entity.Request;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestApi {
    @GET("database/Request.json")
    Call<List<Request>> getAll();
}