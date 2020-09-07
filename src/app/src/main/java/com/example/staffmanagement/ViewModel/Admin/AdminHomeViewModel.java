package com.example.staffmanagement.ViewModel.Admin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.Rule;
import com.example.staffmanagement.Model.Repository.NotificationRepository;
import com.example.staffmanagement.Model.Repository.RequestRepository;
import com.example.staffmanagement.Model.Repository.StateRequestRepository;
import com.example.staffmanagement.Model.Repository.UserRepository;
import com.example.staffmanagement.Model.FirebaseDb.Base.CallBackFunc;
import com.example.staffmanagement.View.Data.UserSingleTon;

public class AdminHomeViewModel extends ViewModel {
    private UserRepository mRepoUser;
    private RequestRepository mRepoRequest;
    private StateRequestRepository mRepoStateRequest;

    private MutableLiveData<Integer> mWaitingRequestLD;
    private MutableLiveData<Integer> mResponseRequestLD;
    private MutableLiveData<Integer> mRecentRequestLD;
    private MutableLiveData<Integer> mAllRequestLD;
    private MutableLiveData<Integer> mStaffLD;
    private MutableLiveData<Integer> mAdminLD;
    private MutableLiveData<Rule> mNumRequestOfRule;

    private MutableLiveData<String> mMostSendingLD;
    private MutableLiveData<String> mLeastSendingLD;

    public AdminHomeViewModel() {
        this.mRepoUser = new UserRepository();
        this.mRepoRequest = new RequestRepository();
        mRepoStateRequest = new StateRequestRepository();
        mWaitingRequestLD = new MutableLiveData<>();
        mResponseRequestLD = new MutableLiveData<>();
        mRecentRequestLD = new MutableLiveData<>();
        mAllRequestLD = new MutableLiveData<>();
        mMostSendingLD = new MutableLiveData<>();
        mLeastSendingLD = new MutableLiveData<>();
        mStaffLD = new MutableLiveData<>();
        mAdminLD = new MutableLiveData<>();
        mNumRequestOfRule = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getStateRequestLD() {
        return mWaitingRequestLD;
    }

    public MutableLiveData<Integer> getResponseRequestLD() {
        return mResponseRequestLD;
    }

    public MutableLiveData<Integer> getRecentRequestLD() {
        return mRecentRequestLD;
    }

    public MutableLiveData<Integer> getAllRequestLD() {
        return mAllRequestLD;
    }

    public MutableLiveData<Integer> getStaffLD() {
        return mStaffLD;
    }

    public MutableLiveData<Integer> getAdminLD() {
        return mAdminLD;
    }

    public MutableLiveData<String> getMostSendingLD() {
        return mMostSendingLD;
    }

    public MutableLiveData<String> getLeastSendingLD() {
        return mLeastSendingLD;
    }

    public MutableLiveData<Rule> getNumRequestOfRule() {
        return mNumRequestOfRule;
    }

    public void countRequestWaiting() {
        mRepoRequest.countRequestWaiting(new CallBackFunc<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                mWaitingRequestLD.postValue(data);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void countRequestResponse() {
        mRepoRequest.countRequestResponse(new CallBackFunc<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                mResponseRequestLD.postValue(data);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void countRecentRequest() {
        mRepoRequest.countRecentRequest(new CallBackFunc<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                mRecentRequestLD.postValue(data);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void countAllRequest() {
        mRepoRequest.countAllRequest(new CallBackFunc<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                mAllRequestLD.postValue(data);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void countStaff() {
        mRepoUser.countStaff(new CallBackFunc<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                mStaffLD.postValue(data);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void countAdmin() {
        mRepoUser.countAdmin(new CallBackFunc<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                mAdminLD.postValue(data);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void countMostUserSendingRequest(){
        mRepoRequest.countMostUserSendingRequest(new CallBackFunc<String>() {
            @Override
            public void onSuccess(String data) {
                mMostSendingLD.postValue(data);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void countLeastUserSendingRequest(){
        mRepoRequest.countLeastUserSendingRequest(new CallBackFunc<String>() {
            @Override
            public void onSuccess(String data) {
                mLeastSendingLD.postValue(data);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void getRuleFromNetwork(){
        mRepoRequest.getRule(new CallBackFunc<Rule>() {
            @Override
            public void onSuccess(Rule data) {
                mNumRequestOfRule.postValue(data);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void updateRule(int num,int period){
        Rule newRule = new Rule(num,period);
        mRepoRequest.updateRule(newRule, new CallBackFunc<Rule>() {
            @Override
            public void onSuccess(Rule data) {
                mNumRequestOfRule.postValue(data);
            }

            @Override
            public void onError(String message) {
                mNumRequestOfRule.postValue(null);
            }
        });
    }

    public void saveToken(String token){
        new NotificationRepository().saveToken(UserSingleTon.getInstance().getUser(),token);
    }
}
