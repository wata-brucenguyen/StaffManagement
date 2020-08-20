package com.example.staffmanagement.Model.FirebaseDb.Role;

import android.util.Log;
import com.example.staffmanagement.Model.Data.SeedData;
import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.RetrofitCall;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
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

    public void getAll(final ApiResponse apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        RoleApi api = retrofit.create(RoleApi.class);
        api.getAll().enqueue(new Callback<List<Role>>() {
            @Override
            public void onResponse(Call<List<Role>> call, Response<List<Role>> response) {
                List<Role> list = response.body();
                List<Role> roles = new ArrayList<>();
                if (response.body() != null)
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) != null) {
                            roles.add(list.get(i));
                        }
                    }
                Resource<List<Role>> success = new Success<>(roles);
                apiResponse.onSuccess(success);

            }

            @Override
            public void onFailure(Call<List<Role>> call, Throwable t) {
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
                Log.i("FETCH", "delete success ");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("FETCH", "delete error : " + t.getMessage());
            }
        });
    }

    public void put(Role role) {
        Retrofit retrofit = RetrofitCall.create();
        RoleApi api = retrofit.create(RoleApi.class);
        api.getAll().enqueue(new Callback<List<Role>>() {
            @Override
            public void onResponse(Call<List<Role>> call, Response<List<Role>> response) {
                int maxId = 0;
                if (response.body() != null)
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i) != null) {
                            if (response.body().get(i).getId() > maxId)
                                maxId = response.body().get(i).getId();
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
            public void onFailure(Call<List<Role>> call, Throwable t) {
                Log.i("FETCH", "error : " + t.getMessage());
            }
        });
    }
}
