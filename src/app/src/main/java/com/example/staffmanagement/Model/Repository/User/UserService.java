package com.example.staffmanagement.Model.Repository.User;

import androidx.annotation.NonNull;

import com.example.staffmanagement.Model.LocalDb.Database.Data.SeedData;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.User;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.UserState;
import com.example.staffmanagement.Model.Repository.Base.ApiResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    public void initialize() {
        FirebaseDatabase.getInstance().getReference("database")
                .child("User").setValue(SeedData.getRoleList());
    }

    public void getAll(final ApiResponse apiResponse){
        FirebaseDatabase.getInstance().getReference("database")
                .child("User")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<User> list = new ArrayList<>();
                        for (DataSnapshot s : snapshot.getChildren()) {
                            User user = s.getValue(User.class);
                            list.add(user);
                        }
                        apiResponse.onSuccess(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
