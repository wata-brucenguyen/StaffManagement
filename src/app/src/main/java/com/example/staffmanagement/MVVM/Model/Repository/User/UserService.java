package com.example.staffmanagement.MVVM.Model.Repository.User;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.staffmanagement.MVVM.Model.Entity.User;
import com.example.staffmanagement.MVVM.Model.Repository.Base.ApiResponse;
import com.example.staffmanagement.MVVM.Model.Repository.Base.Success;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    public List<UserClone> getUserList() {
        List<UserClone> list = new ArrayList<>();
        list.add(new UserClone(1, 1, "Hoang", "hoang", "123456", "0123456789", "hoang@gmail.com", "12/12, Quang Trung, Q.12, TP.HCM", null, 1));
        list.add(new UserClone(2, 1, "Triet", "triet", "123456", "0123444789", "triet@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", null, 1));
        list.add(new UserClone(3, 1, "Vuong", "vuong", "123456", "0123488993", "vuong@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", null, 1));
        list.add(new UserClone(4, 2, "Tèo", "teo", "123456", "0123444789", "teo@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", null, 1));
        list.add(new UserClone(5, 2, "Tí", "ti", "123456", "0123444789", "ti@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", null, 1));
        list.add(new UserClone(6, 2, "Sửu", "suu", "123456", "0123444789", "suu@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", null, 1));
        return list;
    }

    public void initialize() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("database")
                .child("User");
        ref.setValue(getUserList());
    }

    public void populateData() {
        //initialize();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("database")
                .child("User");
        final Query query = ref.orderByChild("id");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = null;
                for (DataSnapshot s : snapshot.getChildren()) {
                    user = s.getValue(User.class);
                }
                Log.i("Fetch", "get : " + user.getId());
                int max = user.getId() + 1;
                for (int i = 1; i <= 50; i++) {
                    String key = ref.push().getKey();
                    ref.child(key).setValue(new User(max, 2, "Hoàng" + max, "hoang" + max, "123456", "0123444789", "hoang" + max + "@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", null, 1));
                    max += 1;
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
                .child("User");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> list = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    User user = s.getValue(User.class);
                    list.add(user);
                }
                Success<List<User>> resource = new Success<>(list);
                apiResponse.onSuccess(resource);
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
