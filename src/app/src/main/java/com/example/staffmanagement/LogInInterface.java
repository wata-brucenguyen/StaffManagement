package com.example.staffmanagement;

public interface LogInInterface {
    void createNewProgressDialog(String message);
    void setMessageProgressDialog(String message);
    void dismissProgressDialog();
    void prepareData();
}
