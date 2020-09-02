package com.example.staffmanagement.Model.Entity.UserBuilder;

import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Entity.UserState;

public interface UserBuilderInterface {
    UserBuilderInterface buildId(int id);
    UserBuilderInterface buildFullName(String fullName);
    UserBuilderInterface buildUserName(String userName);
    UserBuilderInterface buildPassword(String password);
    UserBuilderInterface buildPhoneNumber(String phoneNumber);
    UserBuilderInterface buildEmail(String email);
    UserBuilderInterface buildAddress(String address);
    UserBuilderInterface buildAvatar(String avatar);
    UserBuilderInterface buildRole(Role role);
    UserBuilderInterface buildUserState(UserState userState);
    User build();
}
