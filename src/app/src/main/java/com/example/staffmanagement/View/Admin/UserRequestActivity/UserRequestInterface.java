package com.example.staffmanagement.View.Admin.UserRequestActivity;

import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;

import java.util.ArrayList;

public interface UserRequestInterface {
    void setRefresh(Boolean b);
    void onLoadMoreListSuccess(ArrayList<Request> arrayList);
    void showMessage(String message);
    void newProgressDialog(String message);
    void showProgressDialog();
    void setMessageProgressDialog(String message);
    void dismissProgressDialog();
    void onAddNewRequestSuccessfully(Request newItem);
    void onUpdateRequestSuccessfully(Request item);
    void onApplyFilter(AdminRequestFilter filter);
}
