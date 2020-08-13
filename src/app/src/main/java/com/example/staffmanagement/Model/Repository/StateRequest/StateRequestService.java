package com.example.staffmanagement.Model.Repository.StateRequest;

import androidx.annotation.NonNull;

import com.example.staffmanagement.Model.LocalDb.Database.Data.SeedData;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.StateRequest;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.UserState;
import com.example.staffmanagement.Model.Repository.Base.ApiResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StateRequestService {
    public void initialize() {
        FirebaseDatabase.getInstance().getReference("database")
                .child("StateRequest").setValue(SeedData.getRoleList());
    }

    public void getAll(final ApiResponse apiResponse){
        FirebaseDatabase.getInstance().getReference("database")
                .child("StateRequest")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<StateRequest> list = new ArrayList<>();
                        for (DataSnapshot s : snapshot.getChildren()) {
                            StateRequest stateRequest= s.getValue(StateRequest.class);
                            list.add(stateRequest);
                        }
                        apiResponse.onSuccess(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
