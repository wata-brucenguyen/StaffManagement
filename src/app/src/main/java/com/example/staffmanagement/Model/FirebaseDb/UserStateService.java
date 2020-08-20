package com.example.staffmanagement.Model.FirebaseDb;

import androidx.annotation.NonNull;

import com.example.staffmanagement.Model.Data.SeedData;
import com.example.staffmanagement.Model.Entity.UserState;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Success;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserStateService {
    public void initialize() {
        FirebaseDatabase.getInstance().getReference("database")
                .child("UserState").setValue(SeedData.getRoleList());
    }

    public void getAll(final ApiResponse apiResponse) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("data")
                .child("UserState");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UserState> list = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    UserState userState = s.getValue(UserState.class);
                    list.add(userState);
                }
                Success<List<UserState>> resource = new Success<>(list);
                apiResponse.onSuccess(resource);
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
