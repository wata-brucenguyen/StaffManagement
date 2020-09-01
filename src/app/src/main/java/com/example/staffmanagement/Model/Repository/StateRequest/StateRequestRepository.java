package com.example.staffmanagement.Model.Repository.StateRequest;

import androidx.lifecycle.MutableLiveData;

import com.example.staffmanagement.Model.Entity.StateRequest;
import com.example.staffmanagement.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.Model.FirebaseDb.Base.Resource;
import com.example.staffmanagement.Model.FirebaseDb.StateRequest.StateRequestService;
import com.example.staffmanagement.Model.FirebaseDb.Base.CallBackFunc;

import java.util.List;

public class StateRequestRepository {
    private StateRequestService service;
    private MutableLiveData<List<StateRequest>> mLiveData;

    public StateRequestRepository() {
        service = new StateRequestService();
        mLiveData = new MutableLiveData<>();
    }

    public void getAll() {
        new Thread(() -> service.getAll(new ApiResponse<List<StateRequest>>() {
            @Override
            public void onSuccess(Resource<List<StateRequest>> success) {
                mLiveData.postValue(success.getData());
            }

            @Override
            public void onLoading(Resource<List<StateRequest>> loading) {

            }

            @Override
            public void onError(Resource<List<StateRequest>> error) {

            }
        })).start();
    }

    public MutableLiveData<List<StateRequest>> getLiveData() {
        return mLiveData;
    }

    public void getStateNameById(int idState, CallBackFunc<String> callBackFunc) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                service.getById(idState, new ApiResponse<StateRequest>() {
                    @Override
                    public void onSuccess(Resource<StateRequest> success) {
                        callBackFunc.onSuccess(success.getData().getName());
                    }

                    @Override
                    public void onLoading(Resource<StateRequest> loading) {

                    }

                    @Override
                    public void onError(Resource<StateRequest> error) {

                    }
                });
            }
        }).start();
    }



}
