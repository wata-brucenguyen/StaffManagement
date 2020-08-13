package com.example.staffmanagement.Model.Repository.Role;

import androidx.annotation.NonNull;

import com.example.staffmanagement.Model.LocalDb.Database.Data.SeedData;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.Role;
import com.example.staffmanagement.Model.Repository.Base.ApiResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoleService {
    public void initialize() {
        FirebaseDatabase.getInstance().getReference("database")
                .child("Role").setValue(SeedData.getRoleList());
    }

    public void getAll(final ApiResponse apiResponse) {
        FirebaseDatabase.getInstance().getReference("data")
                .child("Role")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Role> roleList = new ArrayList<>();
                        for (DataSnapshot s : snapshot.getChildren()) {
                            Role role = s.getValue(Role.class);
                            roleList.add(role);
                        }
                        apiResponse.onSuccess(roleList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getRoleNameById(final ApiResponse apiResponse, final int id) {
        FirebaseDatabase.getInstance().getReference("data")
                .child("Role")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Role> roleList = new ArrayList<>();
                        for (DataSnapshot s : snapshot.getChildren()) {
                            Role role = s.getValue(Role.class);
                            if(role.getId() == id){
                                apiResponse.onSuccess(role.getName());
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
