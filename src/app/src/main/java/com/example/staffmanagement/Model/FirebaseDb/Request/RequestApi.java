package com.example.staffmanagement.Model.FirebaseDb.Request;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.Rule;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RequestApi {
    @GET("database/Request.json")
    Call<Object> getAll();

    @GET("database/Request/uid_{idUser}.json")
    Call<Object> getListByIdUser(@Path("idUser") int idUser);

    @POST("database/Request/.json")
    Call<Request> post( @Body Request request);

    @DELETE("database/Request/uid_{idUser}/rid_{idRequest}.json")
    Call<String> delete(@Path("idUser") int idUser,@Path("idRequest") int idRequest);

    @PUT("database/Request/uid_{idUser}/rid_{idRequest}.json")
    Call<Request> put(@Path("idUser") int idUser,@Path("idRequest") int idRequest, @Body Request request);

    @PUT("database/Request/uid_{idUser}/rid_{idRequest}.json")
    Call<Request> update(@Path("idUser") int idUser,@Path("idRequest") int idRequest, @Body Request request);

    @GET("database/Rule/rule_id_1.json")
    Call<Rule> getRule();

    @PUT("database/Rule/rule_id_1.json")
    Call<Rule> updateRule(@Body Rule rule);


}