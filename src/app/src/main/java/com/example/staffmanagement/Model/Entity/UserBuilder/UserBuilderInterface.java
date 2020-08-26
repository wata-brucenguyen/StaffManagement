package com.example.staffmanagement.Model.Entity.UserBuilder;

import com.example.staffmanagement.Model.Entity.User;

public interface UserBuilderInterface {
    UserBuilderInterface buildId(int id);
    UserBuilderInterface buildIdRole(int idRole);
    UserBuilderInterface buildFullName(String fullName);
    UserBuilderInterface buildUserName(String userName);
    UserBuilderInterface buildPassword(String password);
    UserBuilderInterface buildPhoneNumber(String phoneNumber);
    UserBuilderInterface buildEmail(String email);
    UserBuilderInterface buildAddress(String address);
    UserBuilderInterface buildAvatar(String avatar);
    UserBuilderInterface buildIdUserState(int idUserState);
    User build();
}
