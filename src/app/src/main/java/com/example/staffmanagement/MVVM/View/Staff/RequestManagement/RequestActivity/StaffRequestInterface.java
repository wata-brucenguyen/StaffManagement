package com.example.staffmanagement.MVVM.View.Staff.RequestManagement.RequestActivity;

import androidx.recyclerview.widget.RecyclerView;

import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Entity.StateRequest;
import com.example.staffmanagement.MVVM.View.Data.StaffRequestFilter;
import java.util.ArrayList;
import java.util.List;

public interface StaffRequestInterface {
    void showMessage(String message);
    void newProgressDialog(String message);
    void showProgressDialog();
    void setMessageProgressDialog(String message);
    void dismissProgressDialog();
    void onLoadMoreListSuccess(ArrayList<Request> list);
    void onApplyFilter(StaffRequestFilter filter);
    void onCancelFilter();
    void getAllStateRequest();
    void onSuccessGetAllStateRequest(List<StateRequest> list);
    void deleteRequest(RecyclerView.ViewHolder viewHolder,int position);
    void onSuccessDeleteRequest(Request request,int position);
}
