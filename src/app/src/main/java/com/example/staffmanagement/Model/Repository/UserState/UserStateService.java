package com.example.staffmanagement.Model.Repository.UserState;

import androidx.annotation.NonNull;

import com.example.staffmanagement.Model.LocalDb.Database.Data.SeedData;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.UserState;
import com.example.staffmanagement.Model.Repository.Base.ApiResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserStateService {
    public void initialize() {
        FirebaseDatabase.getInstance().getReference("database")
                .child("UserState").setValue(SeedData.getRoleList());
    }

    public void getAll(final ApiResponse apiResponse){
        FirebaseDatabase.getInstance().getReference("database")
                .child("UserState")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<UserState> list = new ArrayList<>();
                        for (DataSnapshot s : snapshot.getChildren()) {
                            UserState userState = s.getValue(UserState.class);
                            list.add(userState);
                        }
                        apiResponse.onSuccess(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
