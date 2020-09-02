package com.example.staffmanagement.Model.FirebaseDb.User;

import com.example.staffmanagement.Model.Entity.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {
    @GET("database/User.json")
    Call<Object> getAll();

    @POST("database/User/.json")
    Call<User> post( @Body User user);

    @DELETE("database/User/uid_{id}.json")
    Call<String> delete(@Path("id") int id);

    @PUT("database/User/uid_{id}.json")
    Call<User> put(@Path("id") int id, @Body User user);

    @PUT("database/User/uid_{id}.json")
    Call<User> update(@Path("id") int id, @Body User user);

    @GET("database/User/uid_{id}.json")
    Call<User> getById(@Path("id") int id);

}
