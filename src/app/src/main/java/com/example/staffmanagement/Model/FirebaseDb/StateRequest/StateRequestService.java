package com.example.staffmanagement.Model.FirebaseDb.StateRequest;


import android.util.Log;

import com.example.staffmanagement.Model.Data.SeedData;
import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Base.RetrofitCall;
import com.example.staffmanagement.Model.FirebaseDb.Base.Success;
import com.example.staffmanagement.Model.FirebaseDb.Base.Error;
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

public class StateRequestService {
    public void initialize() {
        FirebaseDatabase.getInstance().getReference("database")
                .child("StateRequest").setValue(SeedData.getStateList());
    }

    public void populateData() {
        Retrofit retrofit = RetrofitCall.create();
        StateRequestApi api = retrofit.create(StateRequestApi.class);
        List<StateRequest> list = SeedData.getStateList();
        for(int i=0;i<list.size();i++){
            api.put(list.get(i).getId(),list.get(i)).enqueue(new Callback<StateRequest>() {
                @Override
                public void onResponse(Call<StateRequest> call, Response<StateRequest> response) {

                }

                @Override
                public void onFailure(Call<StateRequest> call, Throwable t) {

                }
            });
        }
    }

    public void getAll(final ApiResponse<List<StateRequest>> apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        StateRequestApi api = retrofit.create(StateRequestApi.class);
        api.getAll().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                List<StateRequest> list = new ArrayList<>();
                if (response.body() != null) {
                    try {
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        JSONObject rootObject = new JSONObject(json);
                        Iterator<String> rootKeys = rootObject.keys();
                        while (rootKeys.hasNext()){
                            String key = rootKeys.next();
                            JSONObject itemObject = new JSONObject(rootObject.getJSONObject(key).toString());
                            StateRequest stateRequest = gson.fromJson(itemObject.toString(),StateRequest.class);
                            list.add(stateRequest);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Resource<List<StateRequest>> success = new Success<>(list);
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Resource<List<StateRequest>> error = new Error<>(new ArrayList<>(), t.getMessage());
                apiResponse.onError(error);
            }
        });
    }

    public void getById(int idStateRequest,final ApiResponse apiResponse){
        Retrofit retrofit = RetrofitCall.create();
        StateRequestApi api = retrofit.create(StateRequestApi.class);
        api.getById(idStateRequest).enqueue(new Callback<StateRequest>() {
            @Override
            public void onResponse(Call<StateRequest> call, Response<StateRequest> response) {
                Resource<StateRequest> success = new Success<>(response.body());
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<StateRequest> call, Throwable t) {
                Resource<StateRequest> error = new Error<>(null,t.getMessage());
                apiResponse.onError(error);
            }
        });
    }

    public void delete(int id) {
        Retrofit retrofit = RetrofitCall.create();
        StateRequestApi api = retrofit.create(StateRequestApi.class);
        api.delete(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public void put(StateRequest StateRequest) {
        Retrofit retrofit = RetrofitCall.create();
        StateRequestApi api = retrofit.create(StateRequestApi.class);
        getAll(new ApiResponse<List<StateRequest>>() {
            @Override
            public void onSuccess(Resource<List<StateRequest>> success) {
                int maxId = 0;
                if (success.getData() != null)
                    for (int i = 0; i < success.getData().size(); i++) {
                        if (success.getData().get(i) != null) {
                            if (success.getData().get(i).getId() > maxId)
                                maxId = success.getData().get(i).getId();
                        }
                    }
                StateRequest.setId(maxId + 1);
                api.put(maxId + 1, StateRequest).enqueue(new Callback<StateRequest>() {
                    @Override
                    public void onResponse(Call<StateRequest> call, Response<StateRequest> response) {

                    }

                    @Override
                    public void onFailure(Call<StateRequest> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onLoading(Resource<List<StateRequest>> loading) {

            }

            @Override
            public void onError(Resource<List<StateRequest>> error) {

            }
        });
    }
}
