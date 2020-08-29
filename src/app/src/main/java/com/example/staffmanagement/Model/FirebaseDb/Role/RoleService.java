package com.example.staffmanagement.Model.FirebaseDb.Role;

import android.util.Log;

import com.example.staffmanagement.Model.Data.SeedData;
import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Error;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Base.RetrofitCall;
import com.example.staffmanagement.Model.FirebaseDb.Base.Success;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RoleService {
    public void initialize() {
        FirebaseDatabase.getInstance().getReference("database")
                .child("Role").setValue(SeedData.getRoleList());
    }

    public void populateData() {
        Retrofit retrofit = RetrofitCall.create();
        RoleApi api = retrofit.create(RoleApi.class);
        List<Role> list = SeedData.getRoleList();

        for (int i = 0; i < list.size(); i++) {
            api.put(list.get(i).getId(), list.get(i)).enqueue(new Callback<Role>() {
                @Override
                public void onResponse(Call<Role> call, Response<Role> response) {

                }

                @Override
                public void onFailure(Call<Role> call, Throwable t) {

                }
            });
        }
    }

    public void getById(int idRole,final ApiResponse apiResponse){
        Retrofit retrofit = RetrofitCall.create();
        RoleApi api = retrofit.create(RoleApi.class);
        api.getById(idRole).enqueue(new Callback<Role>() {
            @Override
            public void onResponse(Call<Role> call, Response<Role> response) {
                Resource<Role> success = new Success<>(response.body());
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<Role> call, Throwable t) {
                Resource<Role> error = new Error<>(null, t.getMessage());
                apiResponse.onError(error);
            }
        });
    }

    public void getAll(final ApiResponse<List<Role>> apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        RoleApi api = retrofit.create(RoleApi.class);
        api.getAll().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                List<Role> list = new ArrayList<>();
                if (response.body() != null) {
                    try {
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        JSONObject rootObject = new JSONObject(json);
                        Iterator<String> rootKeys = rootObject.keys();
                        while (rootKeys.hasNext()){
                            String key = rootKeys.next();
                            JSONObject itemObject = new JSONObject(rootObject.getJSONObject(key).toString());
                            Role role = gson.fromJson(itemObject.toString(),Role.class);
                            list.add(role);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Resource<List<Role>> success = new Success<>(list);
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Resource<List<Role>> error = new Error<>(new ArrayList<>(), t.getMessage());
                apiResponse.onError(error);
            }
        });
    }

    public void delete(int id) {
        Retrofit retrofit = RetrofitCall.create();
        RoleApi api = retrofit.create(RoleApi.class);
        api.delete(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public void put(Role role) {
        Retrofit retrofit = RetrofitCall.create();
        RoleApi api = retrofit.create(RoleApi.class);
        getAll(new ApiResponse<List<Role>>() {
            @Override
            public void onSuccess(Resource<List<Role>> success) {
                int maxId = 0;
                if (success.getData() != null)
                    for (int i = 0; i < success.getData().size(); i++) {
                        if (success.getData().get(i) != null) {
                            if (success.getData().get(i).getId() > maxId)
                                maxId = success.getData().get(i).getId();
                        }
                    }
                role.setId(maxId + 1);
                api.put(maxId + 1, role).enqueue(new Callback<Role>() {
                    @Override
                    public void onResponse(Call<Role> call, Response<Role> response) {

                    }

                    @Override
                    public void onFailure(Call<Role> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onLoading(Resource<List<Role>> loading) {

            }

            @Override
            public void onError(Resource<List<Role>> error) {

            }
        });
    }
}
