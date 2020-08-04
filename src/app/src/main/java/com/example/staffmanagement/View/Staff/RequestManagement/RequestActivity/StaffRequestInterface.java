package com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity;

import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.View.Data.StaffRequestFilter;
import java.util.ArrayList;

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
    void getStateNameById(int idRequest, int idState, StaffRequestListAdapter.ViewHolder holder);
    void onSuccessGetStateNameById(int idRequest, String stateName,StaffRequestListAdapter.ViewHolder holder);
}
