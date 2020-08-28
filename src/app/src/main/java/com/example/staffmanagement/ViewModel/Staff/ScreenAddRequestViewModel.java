package com.example.staffmanagement.ViewModel.Staff;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.Request;
import com.example.staffmanagement.Model.Entity.Rule;
import com.example.staffmanagement.Model.Repository.Request.RequestRepository;
import com.example.staffmanagement.ViewModel.CallBackFunc;

public class ScreenAddRequestViewModel extends ViewModel {

    private MutableLiveData<ERROR_ADD_REQUEST> mError;
    private RequestRepository mRepo;

    public ScreenAddRequestViewModel() {
        mRepo = new RequestRepository();
        this.mError = new MutableLiveData<>();
        mError.postValue(ERROR_ADD_REQUEST.NONE);
    }

    public MutableLiveData<ERROR_ADD_REQUEST> getError() {
        return mError;
    }

    public void checkRuleForAddRequest(int idUser){
        mRepo.checkRuleForAddRequest(idUser,new CallBackFunc<Boolean>() {
            @Override
            public void success(Boolean data) {
                if(data)
                    mError.postValue(ERROR_ADD_REQUEST.PASS);
                else
                    mError.postValue(ERROR_ADD_REQUEST.OVER_LIMIT);
            }

            @Override
            public void error(String message) {
                mError.postValue(ERROR_ADD_REQUEST.NETWORK_ERROR);
            }
        });
    }

    public enum ERROR_ADD_REQUEST{
        NONE, OVER_LIMIT, PASS, NETWORK_ERROR
    }
}
