package com.example.staffmanagement.View.Staff.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Database.Entity.Request;

import java.util.ArrayList;
import java.util.List;

public class RequestViewModel extends ViewModel {

    private List<Request> mListRequest = new ArrayList<>();
    private MutableLiveData<List<Request>> mListRequestObserver = new MutableLiveData<>();

    public void setNewListRequest() {
        mListRequest = new ArrayList<>();
    }

    public void addRange(List<Request> list) {
        mListRequest.addAll(list);
        mListRequestObserver.setValue(mListRequest);
    }

    public void clearList() {
        mListRequest.clear();
        mListRequestObserver.setValue(mListRequest);
    }

    public void insert(Request request) {
        mListRequest.add(request);
        mListRequestObserver.setValue(mListRequest);
    }

    public void update(Request item) {

        for (int i = 0; i < mListRequest.size(); i++) {
            if (item.getId() == mListRequest.get(i).getId()) {
                mListRequest.set(i, item);
                mListRequestObserver.setValue(mListRequest);
                return;
            }
        }
    }

    public void delete (int position){
        mListRequest.remove(position);
        mListRequestObserver.setValue(mListRequest);
    }

    public MutableLiveData<List<Request>> getListRequestObserver() {
        return mListRequestObserver;
    }

    public List<Request> getListRequest() {
        return mListRequest;
    }

}
