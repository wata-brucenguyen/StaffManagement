package com.example.staffmanagement.View.Admin.ViewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Database.Entity.Request;

import java.util.ArrayList;
import java.util.List;

public class UserRequestViewModel extends ViewModel {

    private List<Request> mRequestList = new ArrayList<>();
    private MutableLiveData<List<Request>> requestListObserver = new MutableLiveData<>();
    private String title = "", dateTime = "";

    public void clearList() {
        mRequestList.clear();
        requestListObserver.setValue(mRequestList);
    }

    public void addRange(List<Request> list) {
        mRequestList.addAll(list);

        requestListObserver.setValue(mRequestList);
    }

    public void insert(Request item) {
        mRequestList.add(item);
        requestListObserver.setValue(mRequestList);
    }

    public void update(Request item) {
        for (int i = 0; i < mRequestList.size(); i++) {
            if (item.getId() == mRequestList.get(i).getId()) {
                mRequestList.set(i, item);
                requestListObserver.setValue(mRequestList);
                return;
            }
        }
    }

    public void delete(int position){
        mRequestList.remove(position);
        requestListObserver.setValue(mRequestList);
    }

    public List<Request> getRequestList() {
        return mRequestList;
    }

    public MutableLiveData<List<Request>> getRequestListObserver() {
        return requestListObserver;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
