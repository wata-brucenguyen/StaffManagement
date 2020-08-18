package com.example.staffmanagement.MVVM.View.Admin.MainAdminActivity;

import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Entity.User;
import com.example.staffmanagement.MVVM.Model.Entity.UserState;

import java.util.ArrayList;
import java.util.List;

public interface MainAdminInterface {
    void setupList();
    void newProgressDialog(String message);
    void showMessage(String message);
    void dismissProgressDialog();
    void onLoadMoreListSuccess(List<User> list, List<Integer> quantities);
    void onAddNewUserSuccessfully(User newItem);
    void onChangeUserState(int idUser, int idUserState);
    void getAllRoleAndUserState();
    void onSuccessGetAllRoleAndUserState(List<Role> roles , List<UserState> userStates);
}
