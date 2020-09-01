package com.example.staffmanagement.Model.Repository.UserState;

import androidx.lifecycle.MutableLiveData;

import com.example.staffmanagement.Model.Entity.UserState;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.UserState.UserStateService;
import com.example.staffmanagement.Model.FirebaseDb.Base.CallBackFunc;

import java.util.List;

public class UserStateRepository {
    private UserStateService service;
    private MutableLiveData<List<UserState>> mLiveData;

    public UserStateRepository() {
        service = new UserStateService();
        mLiveData = new MutableLiveData<>();
    }

    public void getAll(CallBackFunc<List<UserState>> callBackFunc) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                service.getAll(new ApiResponse<List<UserState>>() {
                    @Override
                    public void onSuccess(Resource<List<UserState>> success) {
                        callBackFunc.onSuccess(success.getData());
                    }

                    @Override
                    public void onLoading(Resource<List<UserState>> loading) {

                    }

                    @Override
                    public void onError(Resource<List<UserState>> error) {

                    }
                });
            }
        }).start();
    }
}

