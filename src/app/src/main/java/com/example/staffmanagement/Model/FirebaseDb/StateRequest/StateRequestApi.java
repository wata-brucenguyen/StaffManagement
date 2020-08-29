package com.example.staffmanagement.Model.FirebaseDb.StateRequest;

import com.example.staffmanagement.Model.Entity.StateRequest;

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
    Call<Object> getAll();

    @POST("database/StateRequest/.json")
    Call<StateRequest> post( @Body StateRequest stateRequest);

    @DELETE("database/StateRequest/st_req_id_{id}.json")
    Call<String> delete(@Path("id") int id);

    @PUT("database/StateRequest/st_req_id_{id}.json")
    Call<StateRequest> put(@Path("id") int id, @Body StateRequest stateRequest);

    @GET("database/StateRequest/st_req_id_{id}.json")
    Call<StateRequest> getById(@Path("id") int id);
}
