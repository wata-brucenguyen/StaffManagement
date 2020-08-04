package com.example.staffmanagement.View.Admin.UserRequestActivity;

import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestListAdapter;

import java.util.ArrayList;
import java.util.List;

public interface UserRequestInterface {
    void setRefresh(Boolean b);
    void onLoadMoreListSuccess(List<Request> arrayList);
    void showMessage(String message);
    void newProgressDialog(String message);
    void showProgressDialog();
    void setMessageProgressDialog(String message);
    void dismissProgressDialog();
    void onAddNewRequestSuccessfully(Request newItem);
    void onUpdateRequestSuccessfully(Request item);
    void onApplyFilter(AdminRequestFilter filter);
    void readListStateRequest();
    void onSuccessGetAllStateRequest(List<StateRequest> list);
    void getFullNameById(int idUser, UserRequestApdater.ViewHolder holder);
    void onSuccessFullNameById(int idUser, String fullName,UserRequestApdater.ViewHolder holder);
    void update(Request request);
}
