package com.example.staffmanagement.View.Main;
import android.content.Context;

import androidx.fragment.app.Fragment;

import com.example.staffmanagement.Model.Database.Entity.User;

public interface LogInInterface {
    void createNewProgressDialog(String message);
    void setMessageProgressDialog(String message);
    void dismissProgressDialog();
    void prepareData();
    void showMessage(String message);
    void onLoginSuccess(User user);
    void showFragment(int i);
}
