package com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity;

import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.View.Data.StaffRequestFilter;
import java.util.ArrayList;
import java.util.List;

public interface StaffRequestInterface {
    void showMessage(String message);
    void newProgressDialog(String message);
    void showProgressDialog();
    void setMessageProgressDialog(String message);
    void dismissProgressDialog();
    void onAddNewRequestSuccessfully(Request newItem);
    void onUpdateRequestSuccessfully(Request item);
    void onLoadMoreListSuccess(ArrayList<Request> list);
    void onApplyFilter(StaffRequestFilter filter);
    void onCancelFilter();
    void getAllStateRequest();
    void onSuccessGetAllStateRequest(List<StateRequest> list);
    void deleteRequest(int position);
    void onSuccessDeleteRequest(Request request,int position);
}
