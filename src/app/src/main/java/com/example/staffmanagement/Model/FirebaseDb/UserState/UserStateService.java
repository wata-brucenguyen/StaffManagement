package com.example.staffmanagement.Model.FirebaseDb.UserState;

import android.util.Log;

import com.example.staffmanagement.Model.Data.SeedData;
import com.example.staffmanagement.Model.Entity.UserState;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Base.RetrofitCall;
import com.example.staffmanagement.Model.FirebaseDb.Base.Success;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserStateService {
    public void initialize() {
        FirebaseDatabase.getInstance().getReference("database")
                .child("UserState").setValue(SeedData.getRoleList());
    }

    public void populateData() {
        Retrofit retrofit = RetrofitCall.create();
        UserStateApi api = retrofit.create(UserStateApi.class);
        List<UserState> list = SeedData.getUserStateList();
        for(int i=0;i<list.size();i++){
            api.put(list.get(i).getId(),list.get(i)).enqueue(new Callback<UserState>() {
                @Override
                public void onResponse(Call<UserState> call, Response<UserState> response) {

                }

                @Override
                public void onFailure(Call<UserState> call, Throwable t) {

                }
            });
        }
    }

    public void getAll(final ApiResponse apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        UserStateApi api = retrofit.create(UserStateApi.class);
        api.getAll().enqueue(new Callback<List<UserState>>() {
            @Override
            public void onResponse(Call<List<UserState>> call, Response<List<UserState>> response) {
                List<UserState> list = response.body();
                List<UserState> userStates = new ArrayList<>();
                if (response.body() != null)
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) != null) {
                            userStates.add(list.get(i));
                        }
                    }

                Resource<List<UserState>> success = new Success<>(userStates);
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<List<UserState>> call, Throwable t) {
                Resource<List<UserState>> error = new Error<>(new ArrayList<>(), t.getMessage());
                apiResponse.onError(error);
            }
        });
    }

    public void delete(int id) {
        Retrofit retrofit = RetrofitCall.create();
        UserStateApi api = retrofit.create(UserStateApi.class);
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

    public void put(UserState UserState) {
        Retrofit retrofit = RetrofitCall.create();
        UserStateApi api = retrofit.create(UserStateApi.class);
        api.getAll().enqueue(new Callback<List<UserState>>() {
            @Override
            public void onResponse(Call<List<UserState>> call, Response<List<UserState>> response) {
                int maxId = 0;
                if (response.body() != null)
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i) != null) {
                            if (response.body().get(i).getId() > maxId)
                                maxId = response.body().get(i).getId();
                        }
                    }
                UserState.setId(maxId + 1);
                api.put(maxId + 1, UserState).enqueue(new Callback<UserState>() {
                    @Override
                    public void onResponse(Call<UserState> call, Response<UserState> response) {

                    }

                    @Override
                    public void onFailure(Call<UserState> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<UserState>> call, Throwable t) {
                Log.i("FETCH", "error : " + t.getMessage());
            }
        });
    }
}
