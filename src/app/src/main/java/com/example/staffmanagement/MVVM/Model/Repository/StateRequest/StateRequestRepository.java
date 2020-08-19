package com.example.staffmanagement.MVVM.Model.Repository.StateRequest;

import androidx.lifecycle.MutableLiveData;

import com.example.staffmanagement.MVVM.Model.Entity.StateRequest;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.StateRequestService;
import com.example.staffmanagement.MVVM.Model.Repository.AppDatabase;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StateRequestRepository {
    private StateRequestService service;
    private MutableLiveData<List<StateRequest>> mLiveData;

    public StateRequestRepository() {
        service = new StateRequestService();
        mLiveData = new MutableLiveData<>();
    }

    public void getAll(){
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(()->{
            List<StateRequest> list = AppDatabase.getDb().stateRequestDAO().getAll();
            return list;
        }).thenAcceptAsync(stateRequests -> {
            mLiveData.postValue(stateRequests);
        });
    }

    public MutableLiveData<List<StateRequest>> getLiveData() {
        return mLiveData;
    }
}
