package com.example.staffmanagement.MVVM.View.Main;


import com.example.staffmanagement.MVVM.Model.Entity.User;

public interface LogInInterface {
    void createNewProgressDialog(String message);

    void setMessageProgressDialog(String message);

    void dismissProgressDialog();

    void prepareData();

    void showMessage(String message);

    void onLoginSuccess(User user);

    void showFragment(int i);

    void executeLogin();
}
