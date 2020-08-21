package com.example.staffmanagement.Model.FirebaseDb.StateRequest;


import com.example.staffmanagement.Model.Data.SeedData;
import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Base.RetrofitCall;
import com.example.staffmanagement.Model.FirebaseDb.Base.Success;
import com.example.staffmanagement.Model.FirebaseDb.Base.Error;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
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

    public void getAll(final ApiResponse apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        StateRequestApi api = retrofit.create(StateRequestApi.class);
        api.getAll().enqueue(new Callback<List<StateRequest>>() {
            @Override
            public void onResponse(Call<List<StateRequest>> call, Response<List<StateRequest>> response) {
                List<StateRequest> list = response.body();
                List<StateRequest> stateRequests = new ArrayList<>();
                if (response.body() != null)
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) != null) {
                            stateRequests.add(list.get(i));
                        }
                    }

                Resource<List<StateRequest>> success = new Success<>(stateRequests);
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<List<StateRequest>> call, Throwable t) {
                Resource<List<StateRequest>> error = new Error<>(new ArrayList<>(), t.getMessage());
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
        api.getAll().enqueue(new Callback<List<StateRequest>>() {
            @Override
            public void onResponse(Call<List<StateRequest>> call, Response<List<StateRequest>> response) {
                int maxId = 0;
                if (response.body() != null)
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i) != null) {
                            if (response.body().get(i).getId() > maxId)
                                maxId = response.body().get(i).getId();
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
            public void onFailure(Call<List<StateRequest>> call, Throwable t) {
            }
        });
    }
}
