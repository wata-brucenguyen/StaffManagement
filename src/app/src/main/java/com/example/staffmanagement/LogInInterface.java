package com.example.staffmanagement;

import com.example.staffmanagement.Database.Entity.Role;
import com.example.staffmanagement.Database.Entity.User;

import java.util.ArrayList;

public interface LogInInterface {
    void createNewProgressDialog(String message);
    void setMessageProgressDialog(String message);
    void dismissProgressDialog();
    void prepareData();
    void showMessage(String message);
    void loginSuccess(User user);
}
