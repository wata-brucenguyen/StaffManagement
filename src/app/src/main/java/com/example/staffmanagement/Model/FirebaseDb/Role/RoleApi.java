package com.example.staffmanagement.Model.FirebaseDb.Role;

import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.Entity.User;

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
    Call<Object> getAll();

    @POST("database/Role/.json")
    Call<Role> post( @Body Role role);

    @DELETE("database/Role/role_id_{id}.json")
    Call<String> delete(@Path("id") int id);

    @PUT("database/Role/role_id_{id}.json")
    Call<Role> put(@Path("id") int id, @Body Role role);

    @GET("database/Role/role_id_{id}.json")
    Call<Role> getById(@Path("id") int id);
}
