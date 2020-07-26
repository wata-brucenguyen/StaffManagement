package com.example.staffmanagement.View.Main;
import com.example.staffmanagement.Database.Entity.User;

public interface LogInInterface {
    void createNewProgressDialog(String message);
    void setMessageProgressDialog(String message);
    void dismissProgressDialog();
    void prepareData();
    void showMessage(String message);
    void loginSuccess(User user);
}
