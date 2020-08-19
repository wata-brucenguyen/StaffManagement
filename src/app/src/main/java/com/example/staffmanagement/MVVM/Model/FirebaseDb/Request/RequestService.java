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

import java.util.ArrayList;
import java.util.List;

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

    public void populateData() {
        initialize();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("database")
                .child("Request");
        final Query query = ref.orderByChild("id");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Request request = null;
                for (DataSnapshot s : snapshot.getChildren()) {
                    request = s.getValue(Request.class);
                }
                Log.i("Fetch", "get : " + request.getId());
                int max = request.getId() + 1;
                for (int i = 1; i <= 56; i++) {
                    for (int j = 1; j <= 30; j++) {
                        String key = ref.push().getKey();
                        ref.child(key).setValue(new Request(max, i, 1, "Nghỉ phép " + j, "Tôi muốn nghỉ 1 ngày thứ 6", GeneralFunc.convertDateStringToLong("22/03/2017 11:18:32")));
                        max += 1;
                    }
                }
                query.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getAll(final ApiResponse apiResponse) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("database")
                .child("Request");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Request> list = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Request request = s.getValue(Request.class);
                    list.add(request);
                    Log.i("FETCH", "data : " + request.getContent());
                }
                Resource<List<Request>> resource = new Success<>(list);
                apiResponse.onSuccess(resource);
                ref.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Resource<List<Request>> mError = new Error<>(null, error.getMessage());
                apiResponse.onError(mError);
                ref.removeEventListener(this);
            }
        });
    }

    public void getAlll(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StringApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestApi api = retrofit.create(RequestApi.class);
        api.getAll().enqueue(new Callback<List<Request>>() {
            @Override
            public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                if(response.isSuccessful()){
                    List<Request> list = response.body();
                    for(int i = 0 ;i<list.size();i++){
                        Log.i("FETCH","data : " + list.get(i).getId());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Request>> call, Throwable t) {

            }
        });
    }
}
