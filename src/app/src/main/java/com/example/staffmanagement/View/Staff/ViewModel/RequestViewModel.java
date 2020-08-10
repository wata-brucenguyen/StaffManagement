package com.example.staffmanagement.View.Staff.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;

import java.util.ArrayList;
import java.util.List;

public class RequestViewModel extends ViewModel {

    private List<StateRequest> mStateRequestList = new ArrayList<>();
    private List<Request> mListRequest = new ArrayList<>();

    public void setNewListRequest() {
        mListRequest = new ArrayList<>();
    }

    public List<StateRequest> getStateRequestList() {
        return mStateRequestList;
    }

    public void insertNewStateRequestList(List<StateRequest> list){
        mStateRequestList.clear();
        mStateRequestList.addAll(list);
    }

    public void addRange(List<Request> list) {
        mListRequest.addAll(list);
    }

    public void clearList() {
        mListRequest.clear();
    }

    public void insert(Request request) {
        mListRequest.add(request);
    }

    public int update(Request item) {

        for (int i = 0; i < mListRequest.size(); i++) {
            if (item.getId() == mListRequest.get(i).getId()) {
                mListRequest.set(i, item);
                return i;
            }
        }
        return -1;
    }

    public void delete (int position){
        mListRequest.remove(position);
    }

    public List<Request> getListRequest() {
        return mListRequest;
    }

}
