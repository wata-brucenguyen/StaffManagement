package com.example.staffmanagement.Model.FirebaseDb.Request;

import com.example.staffmanagement.Model.Data.SeedData;
import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Error;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Base.RetrofitCall;
import com.example.staffmanagement.Model.FirebaseDb.Base.Success;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestService {

    public void initialize() {
        FirebaseDatabase.getInstance().getReference("database")
                .child("Request").setValue(SeedData.getRequestList());
    }

    public void populateData() {
        Retrofit retrofit = RetrofitCall.create();
        RequestApi api = retrofit.create(RequestApi.class);
        int id = 0;
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 10; j++) {
                id++;
                Request r = new Request(id, i, 1, "Nghỉ phép " + j + ",user: " + i, "Tôi muốn nghỉ 1 ngày thứ 6", GeneralFunc.convertDateStringToLong("22/03/2017 11:18:32"));
                api.put(r.getId(), r).enqueue(new Callback<Request>() {
                    @Override
                    public void onResponse(Call<Request> call, Response<Request> response) {

                    }

                    @Override
                    public void onFailure(Call<Request> call, Throwable t) {

                    }
                });
            }
        }
    }

    public void getAll(final ApiResponse apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        RequestApi api = retrofit.create(RequestApi.class);
        api.getAll().enqueue(new Callback<List<Request>>() {
            @Override
            public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                List<Request> list = new ArrayList<>();
                if (response.body() != null)
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i) != null) {
                            list.add(response.body().get(i));
                        }
                    }
                Resource<List<Request>> success = new Success<>(list);
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<List<Request>> call, Throwable t) {
                Resource<List<Request>> error = new Error<>(new ArrayList<>(), t.getMessage());
                apiResponse.onError(error);
            }
        });
    }

    public void post(Request request) {
        Retrofit retrofit = RetrofitCall.create();
        RequestApi api = retrofit.create(RequestApi.class);
        api.post(request).enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {
            }
        });
    }

    public void delete(int id) {
        Retrofit retrofit = RetrofitCall.create();
        RequestApi api = retrofit.create(RequestApi.class);
        api.delete(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public void put(Request request,ApiResponse apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        RequestApi api = retrofit.create(RequestApi.class);
        api.getAll().enqueue(new Callback<List<Request>>() {
            @Override
            public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                int maxId = 0;
                if (response.body() != null)
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i) != null) {
                            if (response.body().get(i).getId() > maxId)
                                maxId = response.body().get(i).getId();
                        }
                    }
                request.setId(maxId + 1);
                api.put(maxId + 1, request).enqueue(new Callback<Request>() {
                    @Override
                    public void onResponse(Call<Request> call, Response<Request> response) {
                        Resource<Request> success = new Success<>(request);
                        apiResponse.onSuccess(success);
                    }

                    @Override
                    public void onFailure(Call<Request> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Request>> call, Throwable t) {
            }
        });
    }

    public void update(Request request) {
        Retrofit retrofit = RetrofitCall.create();
        RequestApi api = retrofit.create(RequestApi.class);
        api.put(request.getId(), request).enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {

            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {

            }
        });
    }

}
