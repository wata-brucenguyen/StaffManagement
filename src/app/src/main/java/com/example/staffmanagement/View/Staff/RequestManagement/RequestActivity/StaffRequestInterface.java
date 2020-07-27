package com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity;

import com.example.staffmanagement.Model.Database.Entity.Request;

import java.util.ArrayList;

public interface StaffRequestInterface {
    void showMessage(String message);
    void newProgressDialog(String message);
    void showProgressDialog();
    void setMessageProgressDialog(String message);
    void dismissProgressDialog();
    void onGetListSuccessfully(ArrayList<Request> list);
    void onAddNewRequestSuccessfully(Request newItem);
    void onUpdateRequestSuccessfully(Request item);
}
