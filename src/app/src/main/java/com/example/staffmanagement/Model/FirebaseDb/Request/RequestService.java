package com.example.staffmanagement.Model.FirebaseDb.Request;

import android.util.Log;

import com.example.staffmanagement.Model.Data.SeedData;
import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.Rule;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Error;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Base.RetrofitCall;
import com.example.staffmanagement.Model.FirebaseDb.Base.Success;
import com.example.staffmanagement.View.Ultils.GeneralFunc;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
                api.put(r.getIdUser(), r.getId(), r).enqueue(new Callback<Request>() {
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

    public void getAll(final ApiResponse<List<Request>> apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        RequestApi api = retrofit.create(RequestApi.class);
        api.getAll().enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                List<Request> list = new ArrayList<>();
                Gson gson = new Gson();
                if (response.body() != null)
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i) != null) {
                            String json = gson.toJson(response.body().get(i));
                            try {
                                JSONObject object = new JSONObject(json);
                                Iterator<String> keys = object.keys();
                                while(keys.hasNext()){
                                    String k = keys.next();
                                    Request request  = gson.fromJson(object.get(k).toString(),Request.class);
                                    list.add(request);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                Resource<List<Request>> success = new Success<>(list);
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Resource<List<Request>> error = new Error<>(new ArrayList<>(), t.getMessage());
                apiResponse.onError(error);
                Log.i("FETCH", t.getMessage());
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

    public void delete(int idUser, int idRequest) {
        Retrofit retrofit = RetrofitCall.create();
        RequestApi api = retrofit.create(RequestApi.class);
        api.delete(idUser, idRequest).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public void put(Request request, ApiResponse apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        RequestApi api = retrofit.create(RequestApi.class);
        getAll(new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success) {
                int maxId = 0;
                if (success.getData() != null)
                    for (int i = 0; i < success.getData().size(); i++) {
                        if (success.getData().get(i) != null) {
                            if (success.getData().get(i).getId() > maxId)
                                maxId = success.getData().get(i).getId();
                        }
                    }
                request.setId(maxId + 1);
                api.put(request.getIdUser(), maxId + 1, request).enqueue(new Callback<Request>() {
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
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {

            }
        });
    }

    public void update(Request request) {
        Retrofit retrofit = RetrofitCall.create();
        RequestApi api = retrofit.create(RequestApi.class);
        api.put(request.getIdUser(), request.getId(), request).enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {

            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {

            }
        });
    }

    public void getRule(ApiResponse<Rule> apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        RequestApi api = retrofit.create(RequestApi.class);
        api.getRule().enqueue(new Callback<Rule>() {
            @Override
            public void onResponse(Call<Rule> call, Response<Rule> response) {
                Resource<Rule> success = new Success<>(response.body());
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<Rule> call, Throwable t) {
                Resource<Rule> error = new Error<>(null, t.getMessage());
                apiResponse.onError(error);
            }
        });

    }

    public void updateRule(Rule rule, ApiResponse<Rule> apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        RequestApi api = retrofit.create(RequestApi.class);
        api.updateRule(rule).enqueue(new Callback<Rule>() {
            @Override
            public void onResponse(Call<Rule> call, Response<Rule> response) {
                Resource<Rule> success = new Success<>(response.body());
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<Rule> call, Throwable t) {
                Resource<Rule> error = new Error<>(null, t.getMessage());
                apiResponse.onError(error);
            }
        });

    }

    public void getById(int idUser, final ApiResponse apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        RequestApi api = retrofit.create(RequestApi.class);
        getAll(new ApiResponse<List<Request>>() {
            @Override
            public void onSuccess(Resource<List<Request>> success) {
                List<Request> list = new ArrayList<>();
                if (success.getData() != null)
                    for (int i = 0; i < success.getData().size(); i++) {
                        if (success.getData().get(i) != null && success.getData().get(i).getIdUser() == idUser) {
                            list.add(success.getData().get(i));
                        }
                    }
                Resource<List<Request>> successRes = new Success<>(list);
                apiResponse.onSuccess(successRes);
            }

            @Override
            public void onLoading(Resource<List<Request>> loading) {

            }

            @Override
            public void onError(Resource<List<Request>> error) {
                Resource<List<Request>> errorRes = new Error<>(new ArrayList<>(), error.getMessage());
                apiResponse.onError(errorRes);
            }
        });
    }

}
