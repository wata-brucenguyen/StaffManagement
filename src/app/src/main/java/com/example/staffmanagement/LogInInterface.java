package com.example.staffmanagement;

import com.example.staffmanagement.Database.Entity.Role;

import java.util.ArrayList;

public interface LogInInterface {
    void createNewProgressDialog(String message);
    void setMessageProgressDialog(String message);
    void dismissProgressDialog();
    void prepareData();
}