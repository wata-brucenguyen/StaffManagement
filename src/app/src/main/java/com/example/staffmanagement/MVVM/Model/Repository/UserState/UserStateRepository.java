package com.example.staffmanagement.MVVM.Model.Repository.UserState;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.staffmanagement.MVVM.Model.Entity.StateRequest;
import com.example.staffmanagement.MVVM.Model.Entity.UserState;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.Base.ApiResponse;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.Base.NetworkBoundResource;
import com.example.staffmanagement.MVVM.Model.FirebaseDb.UserState.UserStateService;
import com.example.staffmanagement.MVVM.Model.Repository.AppDatabase;
import com.example.staffmanagement.Model.LocalDb.BUS.UserStateBUS;

import java.util.List;

public class UserStateRepository {
    private UserStateService service;
    private MutableLiveData<List<UserState>> mLiveData;
    public UserStateRepository() {
        service = new UserStateService();
        mLiveData = new MutableLiveData<>();
    }

    public List<UserState> getAll() {
        return null;
    }

    public void getAllService(){
        new NetworkBoundResource<List<UserState>, List<UserState>>() {
            @Override
            protected List<UserState> loadFromDb() {
                return AppDatabase.getDb().userStateDAO().getAll();
            }

            @Override
            protected boolean shouldFetchData(List<UserState> data) {
                return data == null || data.size() == 0;
            }

            @Override
            protected void createCall(ApiResponse apiResponse) {
                service.getAll(apiResponse);
            }

            @Override
            protected void saveCallResult(List<UserState> data) {
                int count = AppDatabase.getDb().userStateDAO().count();
                if(count != data.size()){
                    for(int i = 0;i<data.size();i++){
                        Log.i("FETCH","item -save " + data.get(i).getId());
                    }
                    AppDatabase.getDb().userStateDAO().deleteAll();
                    AppDatabase.getDb().userStateDAO().insertRange(data);
                }
            }

            @Override
            protected void onFetchFail(String message) {
                Log.i("FETCH", message);
            }

            @Override
            protected void onFetchSuccess(List<UserState> data) {
                mLiveData.postValue(data);
                Log.i("FETCH", "size : " + data.size());
            }
        }.run();
    }
}

