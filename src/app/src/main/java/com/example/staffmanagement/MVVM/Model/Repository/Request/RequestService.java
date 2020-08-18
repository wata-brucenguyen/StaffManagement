package com.example.staffmanagement.MVVM.Model.Repository.Request;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Repository.Base.ApiResponse;
import com.example.staffmanagement.MVVM.Model.Repository.Base.Error;
import com.example.staffmanagement.MVVM.Model.Repository.Base.Resource;
import com.example.staffmanagement.MVVM.Model.Repository.Base.Success;
import com.example.staffmanagement.MVVM.View.Data.StaffRequestFilter;
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
                        ref.child(key).setValue(new Request(max, i, 1, "Nghỉ phép "+j, "Tôi muốn nghỉ 1 ngày thứ 6", GeneralFunc.convertDateStringToLong("22/03/2017 11:18:32")));
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
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("data")
                .child("Request");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount() == 0){
                            List<Request> list = new ArrayList<>();
                            for (DataSnapshot s : snapshot.getChildren()) {
                                Request request = s.getValue(Request.class);
                                list.add(request);
                            }
                            Resource<List<Request>> resource = new Success<>(list);
                            apiResponse.onSuccess(resource);
                            ref.removeEventListener(this);
                        }
                        else {
                            Resource<List<Request>> error = new Error<>(null,"End");
                            apiResponse.onError(error);
                            ref.removeEventListener(this);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
