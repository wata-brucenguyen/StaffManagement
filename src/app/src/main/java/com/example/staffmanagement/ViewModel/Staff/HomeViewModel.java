package com.example.staffmanagement.ViewModel.Staff;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Repository.Request.RequestRepository;
import com.example.staffmanagement.ViewModel.CallBackFunc;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private RequestRepository repository;
    private MutableLiveData<Integer> totalRequestLD, waitingRequestLD, acceptRequestLD, declineRequestLD;
    private MutableLiveData<String> mTime;
    private MutableLiveData<List<Float>> pieChartListLD;
    public HomeViewModel() {
        this.repository = new RequestRepository();
        this.totalRequestLD = new MutableLiveData<>();
        this.waitingRequestLD = new MutableLiveData<>();
        this.acceptRequestLD = new MutableLiveData<>();
        this.declineRequestLD = new MutableLiveData<>();
        mTime = new MutableLiveData<>();
        pieChartListLD =new MutableLiveData<>();
    }

    public void TotalRequestForUser(int idUser) {
        repository.TotalRequestForUser(idUser, new CallBackFunc<Integer>() {
            @Override
            public void success(Integer data) {
                totalRequestLD.postValue(data);
            }

            @Override
            public void error(String message) {

            }
        });
    }

    public void StateRequestForUser(int idUser, int idStateRequest) {
        repository.StateRequestForUser(idUser, idStateRequest, new CallBackFunc<Integer>() {
            @Override
            public void success(Integer data) {
                switch (idStateRequest) {
                    case 1: {
                        waitingRequestLD.postValue(data);
                        break;
                    }
                    case 2: {
                        acceptRequestLD.postValue(data);
                        break;
                    }
                    case 3: {
                        declineRequestLD.postValue(data);
                        break;
                    }

                }
            }

            @Override
            public void error(String message) {

            }
        });
    }
    public void PieChart(int idUser){
        repository.PieChart(idUser, new CallBackFunc<List<Float>>() {
            @Override
            public void success(List<Float> data) {
                pieChartListLD.postValue(data);
            }

            @Override
            public void error(String message) {

            }
        });
    }

    public MutableLiveData<List<Float>> getPieChartListLD() {
        return pieChartListLD;
    }

    public MutableLiveData<Integer> getWaitingRequestLD() {
        return waitingRequestLD;
    }

    public MutableLiveData<Integer> getAcceptRequestLD() {
        return acceptRequestLD;
    }

    public MutableLiveData<Integer> getDeclineRequestLD() {
        return declineRequestLD;
    }

    public MutableLiveData<Integer> getTotalRequestLD() {
        return totalRequestLD;
    }

    public MutableLiveData<String> getTime() {
        return mTime;
    }
}
