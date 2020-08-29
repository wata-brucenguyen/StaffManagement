package com.example.staffmanagement.Model.FirebaseDb.UserState;

import android.util.Log;

import com.example.staffmanagement.Model.Data.SeedData;
import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.Model.Entity.UserState;
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

    public void getAll(final ApiResponse<List<UserState>> apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        UserStateApi api = retrofit.create(UserStateApi.class);
        api.getAll().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                List<UserState> list = new ArrayList<>();
                if (response.body() != null) {
                    try {
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        JSONObject rootObject = new JSONObject(json);
                        Iterator<String> rootKeys = rootObject.keys();
                        while (rootKeys.hasNext()){
                            String key = rootKeys.next();
                            JSONObject itemObject = new JSONObject(rootObject.getJSONObject(key).toString());
                            UserState userState = gson.fromJson(itemObject.toString(),UserState.class);
                            list.add(userState);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Resource<List<UserState>> success = new Success<>(list);
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
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
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public void put(UserState UserState) {
        Retrofit retrofit = RetrofitCall.create();
        UserStateApi api = retrofit.create(UserStateApi.class);
        getAll(new ApiResponse<List<com.example.staffmanagement.Model.Entity.UserState>>() {
            @Override
            public void onSuccess(Resource<List<UserState>> success) {
                int maxId = 0;
                if (success.getData() != null)
                    for (int i = 0; i < success.getData().size(); i++) {
                        if (success.getData().get(i) != null) {
                            if (success.getData().get(i).getId() > maxId)
                                maxId = success.getData().get(i).getId();
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
            public void onLoading(Resource<List<UserState>> loading) {

            }

            @Override
            public void onError(Resource<List<UserState>> error) {

            }
        });
    }
}
