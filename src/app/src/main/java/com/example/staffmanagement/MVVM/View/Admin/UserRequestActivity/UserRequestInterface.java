package com.example.staffmanagement.MVVM.View.Admin.UserRequestActivity;
import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Entity.StateRequest;
import com.example.staffmanagement.MVVM.View.Data.AdminRequestFilter;

import java.util.List;

public interface UserRequestInterface {
    void setRefresh(Boolean b);
    void onLoadMoreListSuccess(List<Request> arrayList, List<String> userNameList);
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
}
