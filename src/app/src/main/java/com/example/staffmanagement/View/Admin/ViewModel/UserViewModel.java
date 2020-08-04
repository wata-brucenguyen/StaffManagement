package com.example.staffmanagement.View.Admin.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Database.Entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {
    private List<User> mListUser = new ArrayList<>();
    private MutableLiveData<List<User>> mListUserObserver = new MutableLiveData<>();

    public List<User> getListUser() {
        return mListUser;
    }

    public MutableLiveData<List<User>> getListUserObserver() {
        return mListUserObserver;
    }

    public void clearList(){
        mListUser.clear();
        mListUserObserver.setValue(mListUser);
    }

    public void insert(User user){
        mListUser.add(user);
        mListUserObserver.setValue(mListUser);
    }

    public void addRange(List<User> list){
        mListUser.addAll(list);
        mListUserObserver.setValue(mListUser);
    }

    public void delete(int position){
        mListUser.remove(position);
        mListUserObserver.setValue(mListUser);
    }

}
