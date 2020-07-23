package com.example.staffmanagement.Database.Data;

import com.example.staffmanagement.Database.Entity.User;

public class UserSingleTon {

    private static UserSingleTon instance;
    private User user;

    private UserSingleTon(){
        user = new User();
    }

    public static UserSingleTon getInstance(){
        if(instance == null)
            instance = new UserSingleTon();
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
