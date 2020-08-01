package com.example.staffmanagement.View.Data;

import androidx.lifecycle.MutableLiveData;

import com.example.staffmanagement.Model.Database.Entity.User;

public class CopyUserSingleTon {

    private static CopyUserSingleTon instance;
    private MutableLiveData<User> user;

    private CopyUserSingleTon(){
        user = new MutableLiveData<>();
        user.setValue(new User());
    }

    public static CopyUserSingleTon getInstance(){
        if(instance == null)
            instance = new CopyUserSingleTon();
        return instance;
    }

    public MutableLiveData<User> getUserLiveData() {
        return user;
    }

    public User getUser(){
        return this.user.getValue();
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }
}
