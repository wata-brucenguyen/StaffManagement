package com.example.staffmanagement.Model.FirebaseDb.Notification;

import android.util.Log;

import com.example.staffmanagement.Model.Data.SeedData;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.Base.RetrofitCall;
import com.example.staffmanagement.Model.FirebaseDb.Base.Success;
import com.example.staffmanagement.Model.FirebaseDb.Notification.Sender.MyResponse;
import com.example.staffmanagement.Model.FirebaseDb.Notification.Sender.NotificationSender;
import com.example.staffmanagement.Model.FirebaseDb.Notification.Sender.NotificationSenderWithRequest;
import com.example.staffmanagement.Model.FirebaseDb.StringApi;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationService {

    public void populateData() {
        Retrofit retrofit = RetrofitCall.create();
        NotificationApi api = retrofit.create(NotificationApi.class);
        for (int i = 0; i < SeedData.getUserList().size(); i++) {
            User user = SeedData.getUserList().get(i);
            String data = "abcdefh" + user.getId();
            api.post(user.getRole().getId(), user.getId(), data).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Gson gson = new Gson();
                    if (response.body() != null) {
                        try {
                            String json = gson.toJson(response.body());
                            JSONObject jsonObjectRoot = new JSONObject(json);
                            Iterator<String> keyList = jsonObjectRoot.keys();
                            while (keyList.hasNext()) {
                                String key = keyList.next();
                                String token = jsonObjectRoot.getString(key);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.i("TEST_TOKEN", "error : " + t.getMessage());
                }
            });
        }
    }

    public void sendNotificationWithRequest(NotificationSenderWithRequest notificationSender) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StringApi.FCM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NotificationApi api = retrofit.create(NotificationApi.class);
        api.sendNotificationWithRequest(notificationSender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    public void sendAdminToStaff(NotificationSender notificationSender) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StringApi.FCM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NotificationApi api = retrofit.create(NotificationApi.class);
        api.sendNotificationAdminToStaff(notificationSender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
            }
        });
    }

    public void getAll(ApiResponse<List<String>> apiResponse) {
        List<String> list = new ArrayList<>();
        Retrofit retrofit = RetrofitCall.create();
        NotificationApi api = retrofit.create(NotificationApi.class);
        api.getAllToken().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson = new Gson();
                if (response.body() != null) {
                    try {
                        String json = gson.toJson(response.body());
                        JSONObject rootObject = new JSONObject(json);
                        Iterator<String> rootKeys = rootObject.keys();
                        while (rootKeys.hasNext()) {
                            String itemRootKey = rootKeys.next();
                            JSONObject itemObject = new JSONObject(rootObject.getJSONObject(itemRootKey).toString());
                            Iterator<String> itemKeys = itemObject.keys();
                            while (itemKeys.hasNext()) {
                                String userKey = itemKeys.next();
                                JSONObject userObject = new JSONObject(itemObject.getJSONObject(userKey).toString());
                                Iterator<String> keyList = userObject.keys();
                                while (keyList.hasNext()) {
                                    String key = keyList.next();
                                    String token = userObject.getString(key);
                                    list.add(token);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Resource<List<String>> success = new Success<>(list);
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    public void getListTokenByRole(int idRole, ApiResponse<List<String>> apiResponse) {
        List<String> list = new ArrayList<>();
        Retrofit retrofit = RetrofitCall.create();
        NotificationApi api = retrofit.create(NotificationApi.class);
        api.getListTokenByRole(idRole).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson = new Gson();
                if (response.body() != null) {
                    try {
                        String json = gson.toJson(response.body());
                        JSONObject rootObject = new JSONObject(json);
                        Iterator<String> rootKeys = rootObject.keys();
                        while (rootKeys.hasNext()) {
                            String itemRootKey = rootKeys.next();
                            JSONObject userObject = new JSONObject(rootObject.getJSONObject(itemRootKey).toString());
                            Iterator<String> keyList = userObject.keys();
                            while (keyList.hasNext()) {
                                String key = keyList.next();
                                String token = userObject.getString(key);
                                list.add(token);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Resource<List<String>> success = new Success<>(list);
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    public void getListTokenOfUser(int idRole, int idUser, ApiResponse<List<KeyToken>> apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        NotificationApi api = retrofit.create(NotificationApi.class);
        api.getListTokenOfUser(idRole, idUser).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                List<KeyToken> list = new ArrayList<>();
                Gson gson = new Gson();
                if (response.body() != null) {
                    try {
                        String json = gson.toJson(response.body());
                        JSONObject rootObject = new JSONObject(json);
                        Iterator<String> rootKeys = rootObject.keys();
                        while (rootKeys.hasNext()) {
                            String key = rootKeys.next();
                            String token = rootObject.getString(key);
                            list.add(new KeyToken(key, token));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Resource<List<KeyToken>> success = new Success<>(list);
                apiResponse.onSuccess(success);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    public void post(User user, String token, ApiResponse<String> apiResponse) {
        Retrofit retrofit = RetrofitCall.create();
        NotificationApi api = retrofit.create(NotificationApi.class);
        api.post(user.getRole().getId(), user.getId(), token).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson = new Gson();
                if (response.body() != null) {

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    public void delete(User user, String token, ApiResponse<String> apiResponse) {
        getListTokenOfUser(user.getRole().getId(), user.getId(), new ApiResponse<List<KeyToken>>() {
            @Override
            public void onSuccess(Resource<List<KeyToken>> success) {
                for (int i = 0; i < success.getData().size(); i++) {
                    if (success.getData().get(i).token.equals(token)) {
                        Retrofit retrofit = RetrofitCall.create();
                        NotificationApi api = retrofit.create(NotificationApi.class);
                        api.delete(user.getRole().getId(),
                                user.getId(),
                                success.getData().get(i).key).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.i("TEST_TOKEN", "error : " + t.getMessage());
                            }
                        });

                        break;
                    }
                }
            }

            @Override
            public void onLoading(Resource<List<KeyToken>> loading) {

            }

            @Override
            public void onError(Resource<List<KeyToken>> error) {

            }
        });
    }
}
