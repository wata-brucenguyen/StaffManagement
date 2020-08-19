package com.example.staffmanagement.MVVM.Model.FirebaseDb;

import androidx.annotation.NonNull;

import com.example.staffmanagement.MVVM.Model.Entity.StateRequest;
import com.example.staffmanagement.Model.LocalDb.Database.Data.SeedData;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.Base.Success;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StateRequestService {
    public void initialize() {
        FirebaseDatabase.getInstance().getReference("database")
                .child("StateRequest").setValue(SeedData.getStateList());
    }

    public void getAll(final ApiResponse apiResponse) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("data")
                .child("StateRequest");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<StateRequest> list = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    StateRequest stateRequest = s.getValue(StateRequest.class);
                    list.add(stateRequest);
                }
                Success<List<StateRequest>> resource = new Success<>(list);
                apiResponse.onSuccess(resource);
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
