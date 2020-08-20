package com.example.staffmanagement.MVVM.Model.FirebaseDb.Role;

import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Entity.Role;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RoleApi {
    @GET("database/Role.json")
    Call<List<Role>> getAll();

    @POST("database/Role/.json")
    Call<Role> post( @Body Role role);

    @DELETE("database/Role/{id}.json")
    Call<String> delete(@Path("id") int id);

    @PUT("database/Role/{id}.json")
    Call<Role> put(@Path("id") int id, @Body Role role);
}
