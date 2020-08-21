package com.example.staffmanagement.Model.FirebaseDb.User;

import android.util.Log;

import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Base.RetrofitCall;
import com.example.staffmanagement.Model.FirebaseDb.Base.Success;
import com.example.staffmanagement.Model.FirebaseDb.Base.Error;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserService {

    public List<User> getUserList() {
        List<User> list = new ArrayList<>();
        list.add(new User(1, 1, "Hoang", "hoang", "123456", "0123456789", "hoang@gmail.com", "12/12, Quang Trung, Q.12, TP.HCM", null, 1));
        list.add(new User(2, 1, "Triet", "triet", "123456", "0123444789", "triet@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", null, 1));
        list.add(new User(3, 1, "Vuong", "vuong", "123456", "0123488993", "vuong@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", null, 1));
        list.add(new User(4, 2, "Tèo", "teo", "123456", "0123444789", "teo@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", null, 1));
        list.add(new User(5, 2, "Tí", "ti", "123456", "0123444789", "ti@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", null, 1));
        list.add(new User(6, 2, "Sửu", "suu", "123456", "0123444789", "suu@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", null, 1));
        return list;
    }

    public void initialize() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("database")
                .child("User");
        ref.setValue(getUserList());
    }

    public void populateData() {
        Retrofit retrofit = RetrofitCall.create();
        UserApi api = retrofit.create(UserApi.class);
        List<User> list = getUserList();
        for (int i = 0; i < list.size(); i++) {
            api.put(list.get(i).getId(), list.get(i)).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }

        for (int i = 7; i <= 50; i++) {
            User u = new User(i, 2, "User clone", "userclone" + i, "123456", "0123488993", "userclone@gmail.com", "45/3D, Quang Trung, Q.Gò Vấp, TP.HCM", null, 1);
            api.put(i, u).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    public void getAll(final ApiResponse apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        UserApi api = retrofit.create(UserApi.class);
        api.getAll().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> list = new ArrayList<>();
                if (response.body() != null)
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i) != null) {
                            list.add(response.body().get(i));
                        }
                    }
                Resource<List<User>> success = new Success<>(list);
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("FETCH", "error : " + t.getMessage());
                Resource<List<User>> error = new Error<>(new ArrayList<>(), t.getMessage());
                apiResponse.onError(error);
            }
        });
    }

    public void post(User User) {
        Retrofit retrofit = RetrofitCall.create();
        UserApi api = retrofit.create(UserApi.class);
        api.post(User).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("FETCH", "success " + response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("FETCH", "error : " + t.getMessage());
            }
        });
    }

    public void delete(int id) {
        Retrofit retrofit = RetrofitCall.create();
        UserApi api = retrofit.create(UserApi.class);
        api.delete(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("FETCH", "delete success ");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("FETCH", "delete error : " + t.getMessage());
            }
        });
    }

    public void put(User User) {
        Retrofit retrofit = RetrofitCall.create();
        UserApi api = retrofit.create(UserApi.class);
        api.getAll().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                int maxId = 0;
                if (response.body() != null)
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i) != null) {
                            if (response.body().get(i).getId() > maxId)
                                maxId = response.body().get(i).getId();
                        }
                    }
                User.setId(maxId + 1);
                api.put(maxId + 1, User).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("FETCH", "error : " + t.getMessage());
            }
        });
    }

    public void update(User User) {
        Retrofit retrofit = RetrofitCall.create();
        UserApi api = retrofit.create(UserApi.class);
        api.update(User.getId(), User).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
