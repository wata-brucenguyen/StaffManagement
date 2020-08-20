package com.example.staffmanagement.MVVM.Model.FirebaseDb.StateRequest;

import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Entity.StateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface StateRequestApi {
    @GET("database/StateRequest.json")
    Call<List<StateRequest>> getAll();

    @POST("database/StateRequest/.json")
    Call<StateRequest> post( @Body StateRequest stateRequest);

    @DELETE("database/StateRequest/{id}.json")
    Call<String> delete(@Path("id") int id);

    @PUT("database/StateRequest/{id}.json")
    Call<StateRequest> put(@Path("id") int id, @Body StateRequest stateRequest);
}
