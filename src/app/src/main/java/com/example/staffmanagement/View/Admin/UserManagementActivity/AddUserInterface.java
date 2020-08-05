package com.example.staffmanagement.View.Admin.UserManagementActivity;

import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.User;

import java.util.List;

public interface AddUserInterface {
    void onLoadRoleList(List<Role> list);
    void onCheckUserNameIsExist(boolean bool, User user);
}
