package com.example.staffmanagement.View.Staff.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Database.Entity.Request;

import java.util.List;

public class RequestViewModel extends ViewModel {
    private MutableLiveData<List<Request>> mListRequest = new MutableLiveData<>();

    public MutableLiveData<List<Request>> getListRequest() {
        return mListRequest;
    }

    public void setListRequest(MutableLiveData<List<Request>> mListRequest) {
        this.mListRequest = mListRequest;
    }

    public void addRange(List<Request> list){
        List<Request> temp = mListRequest.getValue();
        temp.addAll(list);
        mListRequest.setValue(temp);
    }

    public void clearList(){
        List<Request> temp = mListRequest.getValue();
        temp.clear();
        mListRequest.setValue(temp);
    }

    public List<Request> getData(){
        return mListRequest.getValue();
    }

    public void insert(Request request){
        List<Request> temp = mListRequest.getValue();
        temp.add(request);
        mListRequest.setValue(temp);
    }

    public void update(Request item){
        List<Request> temp = mListRequest.getValue();
        for (int i = 0; i < temp.size(); i++) {
            if (item.getId() == temp.get(i).getId()) {
                temp.set(i, item);
                mListRequest.setValue(temp);
                return;
            }
        }
    }
}
