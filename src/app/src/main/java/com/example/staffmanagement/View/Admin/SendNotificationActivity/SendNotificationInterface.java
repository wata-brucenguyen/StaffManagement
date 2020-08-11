package com.example.staffmanagement.View.Admin.SendNotificationActivity;

import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.Model.Database.Entity.UserState;

import java.util.ArrayList;
import java.util.List;

public interface SendNotificationInterface {
    void setupList();
    void newProgressDialog(String message);
    void showMessage(String message);
    void dismissProgressDialog();
    void onLoadMoreListSuccess(ArrayList<User> list);
    void getAllRole();
    void onSuccessGetAllRole(List<Role> roles);
}
