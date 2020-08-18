package com.example.staffmanagement.MVVM.View.Admin.SendNotificationActivity;

import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Entity.User;

import java.util.ArrayList;
import java.util.List;

public interface SendNotificationInterface {
    void setupList();
    void newProgressDialog(String message);
    void showMessage(String message);
    void dismissProgressDialog();
    void onLoadMoreListSuccess(List<User> list);
    void getAllRole();
    void onSuccessGetAllRole(List<Role> roles);
    void onCancelDialog();
    void setCheckAll(boolean b);
    void loadBottomSheetDialog(User user);
    void changeQuantity();
}
