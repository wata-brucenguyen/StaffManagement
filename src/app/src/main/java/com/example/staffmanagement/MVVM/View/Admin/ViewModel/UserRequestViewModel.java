package com.example.staffmanagement.MVVM.View.Admin.ViewModel;

import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Entity.StateRequest;

import java.util.ArrayList;
import java.util.List;

public class UserRequestViewModel extends ViewModel {

    private List<Request> mRequestList = new ArrayList<>();
    private List<String> mNameUserList = new ArrayList<>();
    private List<StateRequest> mStateRequestList = new ArrayList<>();
    private List<String> mStateRequestNameList = new ArrayList<>();

    public void clearList() {
        mRequestList.clear();
    }

    public void addRange(List<Request> list) {
        mRequestList.addAll(list);
    }

    public void insert(Request item) {
        mRequestList.add(item);
    }

    public int update(Request item) {
        for (int i = 0; i < mRequestList.size(); i++) {
            if (item.getId() == mRequestList.get(i).getId()) {
                mRequestList.set(i, item);
                return i;
            }
        }
        return -1;
    }

    public void updateState(Request item) {
        for (int i = 0; i < mRequestList.size(); i++) {
            if (item.getId() == mRequestList.get(i).getId()) {
                mRequestList.set(i, item);
                return;
            }
        }
    }

    public void delete(int position){
        mRequestList.remove(position);
    }

    public List<Request> getRequestList() {
        return mRequestList;
    }

    public List<String> getNameUserList() {
        return mNameUserList;
    }

    public void addRangeNameUserList(List<String> mNameUserList) {
        this.mNameUserList.addAll(mNameUserList);
    }

    public List<StateRequest> getStateRequestList() {
        return mStateRequestList;
    }

    public void addRangeStateRequestList(List<StateRequest> mStateRequestList) {
        this.mStateRequestList.addAll(mStateRequestList);
        this.mStateRequestNameList.clear();
        for(StateRequest s : mStateRequestList)
            mStateRequestNameList.add(s.getName());
    }

    public List<String> getStateRequestNameList() {
        return mStateRequestNameList;
    }
}
