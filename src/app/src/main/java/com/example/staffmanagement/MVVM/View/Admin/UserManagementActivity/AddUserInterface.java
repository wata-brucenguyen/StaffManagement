package com.example.staffmanagement.MVVM.View.Admin.UserManagementActivity;

import com.example.staffmanagement.MVVM.Model.Entity.Role;
import com.example.staffmanagement.MVVM.Model.Entity.User;

import java.util.List;

public interface AddUserInterface {
    void onLoadRoleList(List<Role> list);
    void onCheckUserNameIsExist(boolean bool, User user);
}
