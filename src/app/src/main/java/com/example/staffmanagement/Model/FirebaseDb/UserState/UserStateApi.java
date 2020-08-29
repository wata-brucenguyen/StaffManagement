package com.example.staffmanagement.Model.FirebaseDb.UserState;

import com.example.staffmanagement.Model.Entity.UserState;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserStateApi {
    @GET("database/UserState.json")
    Call<Object> getAll();

    @POST("database/UserState/.json")
    Call<UserState> post(@Body UserState userState);

    @DELETE("database/UserState/user_st_id_{id}.json")
    Call<String> delete(@Path("id") int id);

    @PUT("database/UserState/user_st_id_{id}.json")
    Call<UserState> put(@Path("id") int id, @Body UserState userState);
}
