package com.example.staffmanagement.Model.FirebaseDb.User;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;


import com.example.staffmanagement.Model.Data.SeedData;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Base.RetrofitCall;
import com.example.staffmanagement.Model.FirebaseDb.Base.Success;
import com.example.staffmanagement.Model.FirebaseDb.Base.Error;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserService {

    public void initialize() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("database")
                .child("User");
        ref.setValue(SeedData.getUserList());
    }

    public void populateData() {
        Retrofit retrofit = RetrofitCall.create();
        UserApi api = retrofit.create(UserApi.class);
        List<User> list = SeedData.getUserList();
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
    }

    public void getAll(final ApiResponse<List<User>> apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        UserApi api = retrofit.create(UserApi.class);
        api.getAll().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                List<User> list = new ArrayList<>();
                if (response.body() != null) {
                    try {
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        JSONObject rootObject = new JSONObject(json);
                        Iterator<String> rootKeys = rootObject.keys();
                        while (rootKeys.hasNext()) {
                            String key = rootKeys.next();
                            JSONObject itemObject = new JSONObject(rootObject.getJSONObject(key).toString());
                            User user = gson.fromJson(itemObject.toString(), User.class);
                            list.add(user);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Resource<List<User>> success = new Success<>(list);
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Resource<List<User>> error = new Error<>(new ArrayList<>(), t.getMessage());
                apiResponse.onError(error);
            }
        });
    }

    public void delete(int id) {
        Retrofit retrofit = RetrofitCall.create();
        UserApi api = retrofit.create(UserApi.class);
        api.delete(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public void put(User User, ApiResponse<User> apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        UserApi api = retrofit.create(UserApi.class);

        getAll(new ApiResponse<List<User>>() {
            @Override
            public void onSuccess(Resource<List<User>> success) {
                int maxId = 0;
                if (success.getData() != null)
                    for (int i = 0; i < success.getData().size(); i++) {
                        if (success.getData().get(i) != null) {
                            if (success.getData().get(i).getId() > maxId)
                                maxId = success.getData().get(i).getId();
                        }
                    }
                User.setId(maxId + 1);
                api.put(maxId + 1, User).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Resource<User> success = new Success<>(User);
                        apiResponse.onSuccess(success);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Resource<User> err1 = new Error<>(null, t.getMessage());
                        apiResponse.onSuccess(err1);

                        Resource<User> err2 = new Error<>(null, t.getMessage());
                        apiResponse.onError(err2);
                    }
                });
            }

            @Override
            public void onLoading(Resource<List<User>> loading) {

            }

            @Override
            public void onError(Resource<List<User>> error) {
                Resource<User> err1 = new Error<>(null, error.getMessage());
                apiResponse.onSuccess(err1);

                Resource<User> err2 = new Error<>(null, error.getMessage());
                apiResponse.onError(err2);
            }
        });
    }

    public void update(User User, ApiResponse<User> apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        UserApi api = retrofit.create(UserApi.class);
        api.update(User.getId(), User).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Resource<User> success = new Success<>(response.body());
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Resource<User> error = new Error<>(null, t.getMessage());
                apiResponse.onSuccess(error);
            }
        });
    }

    public void getById(int id, final ApiResponse<User> apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        UserApi api = retrofit.create(UserApi.class);
        api.getById(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Resource<User> success = new Success<>(response.body());
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Resource<User> err = new Error<>(null, t.getMessage());
                apiResponse.onSuccess(err);
            }
        });
    }

    public void changeAvatar(User user, Bitmap bitmap, ApiResponse<User> apiResponse) {

        StorageReference rootRef = FirebaseStorage.getInstance().getReference();
        StorageReference ref = rootRef.child("HinhAnh/Avatar/avatar_" + user.getId() + ".png");

        // Get the data from an ImageView as bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> download = taskSnapshot.getStorage().getDownloadUrl();
            download.addOnCompleteListener(task -> {
                String url = task.getResult().toString();
                user.setAvatar(url);
                update(user, new ApiResponse<User>() {
                    @Override
                    public void onSuccess(Resource<User> success) {
                        Resource<User> successRes = new Success<>(user);
                        apiResponse.onSuccess(successRes);
                    }

                    @Override
                    public void onLoading(Resource<User> loading) {

                    }

                    @Override
                    public void onError(Resource<User> error) {

                    }
                });
            });


        });
    }

}
