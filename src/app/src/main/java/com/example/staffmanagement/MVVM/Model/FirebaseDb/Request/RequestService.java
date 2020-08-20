package com.example.staffmanagement.MVVM.Model.FirebaseDb.Request;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.Base.Error;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.Base.Success;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.StringApi;
import com.example.staffmanagement.Model.LocalDb.Database.Data.SeedData;
import com.example.staffmanagement.MVVM.View.Ultils.GeneralFunc;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestService {

    public void initialize() {
        FirebaseDatabase.getInstance().getReference("database")
                .child("Request").setValue(SeedData.getRequestList());
    }


    public void getAll(final ApiResponse apiResponse) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StringApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestApi api = retrofit.create(RequestApi.class);
        api.getAll().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                List<Request> list = new ArrayList<>();
                Gson gson = new Gson();
                response.body().forEach((s, o) -> {
                    try {
                        String sJson = gson.toJson(o);
                        Request request = gson.fromJson(sJson, Request.class);
                        list.add(request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                Resource<List<Request>> success = new Success<>(list);
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.i("FETCH", "error : " + t.getMessage());
                Resource<List<Request>> error = new Error<>(new ArrayList<>(),t.getMessage());
                apiResponse.onError(error);
            }
        });
    }

    public void post(Request request) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StringApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestApi api = retrofit.create(RequestApi.class);
        api.post(request).enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
                Log.i("FETCH", "success " + response.body());
            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {
                Log.i("FETCH", "error : " + t.getMessage());
            }
        });
    }
}
