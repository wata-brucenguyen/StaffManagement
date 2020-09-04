package com.example.staffmanagement.ViewModel.Admin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.FirebaseDb.Base.CallBackFunc;
import com.example.staffmanagement.Model.Repository.Request.RequestRepository;

public class DetailRequestViewModel extends ViewModel {
    private RequestRepository mRepo;
    private MutableLiveData<Request> mRequest;

    public DetailRequestViewModel() {
        mRepo = new RequestRepository();
        mRequest = new MutableLiveData<>();
    }

    public MutableLiveData<Request> getRequest() {
        return mRequest;
    }

    public void getRequestById(int id){
        mRepo.getRequestById(id, new CallBackFunc<Request>() {
            @Override
            public void onSuccess(Request data) {
                mRequest.postValue(data);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void updateRequest(Request request) {
       mRepo.updateRequest(request,1);
    }

}
