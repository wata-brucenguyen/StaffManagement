package com.example.staffmanagement.MVVM.Model.FirebaseDb;

import androidx.annotation.NonNull;

import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.Base.Success;
import com.example.staffmanagement.Model.LocalDb.Database.Data.SeedData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
        final DatabaseReference ref =  FirebaseDatabase.getInstance().getReference("data")
                .child("Role");

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Role> roleList = new ArrayList<>();
                        for (DataSnapshot s : snapshot.getChildren()) {
                            Role role = s.getValue(Role.class);
                            roleList.add(role);
                        }
                        Success<List<Role>> resource = new Success<>(roleList);
                        apiResponse.onSuccess(resource);
                        ref.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getRoleNameById(final ApiResponse apiResponse, final int id) {
        FirebaseDatabase.getInstance().getReference("database")
                .child("Role")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Role> roleList = new ArrayList<>();
                        for (DataSnapshot s : snapshot.getChildren()) {
                            Role role = s.getValue(Role.class);
                            if(role.getId() == id){
                                Success<String> resource = new Success<>(role.getName());
                                apiResponse.onSuccess(resource);
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
