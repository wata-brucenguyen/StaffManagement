package com.example.staffmanagement.View.Admin.MainAdminActivity;

import com.example.staffmanagement.Model.Database.Entity.User;

import java.util.ArrayList;

public interface MainAdminInterface {
    void setRefresh(Boolean b);
    void setupList();
    void newProgressDialog(String message);
    void showMessage(String message);
    void dismissProgressDialog();
    void onLoadMoreListSuccess(ArrayList<User> list);
    void onAddNewUserSuccessfully(User newItem);
    void onChangeUserState(int idUser, int idUserState);
}
