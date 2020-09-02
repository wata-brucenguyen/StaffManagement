package com.example.staffmanagement.Model.Entity.UserBuilder;

import com.example.staffmanagement.Model.Entity.Role;
import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Entity.UserState;

public class UserBuilder implements UserBuilderInterface {

    private int id;
    private Role role;
    private UserState userState;
    private String fullName,userName,password,phoneNumber,email,address;
    private String avatar;

    @Override
    public UserBuilderInterface buildId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public UserBuilderInterface buildFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @Override
    public UserBuilderInterface buildUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @Override
    public UserBuilderInterface buildPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public UserBuilderInterface buildPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @Override
    public UserBuilderInterface buildEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public UserBuilderInterface buildAddress(String address) {
        this.address = address;
        return this;
    }

    @Override
    public UserBuilderInterface buildAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    @Override
    public UserBuilderInterface buildRole(Role Role) {
        this.role = role;
        return this;
    }

    @Override
    public UserBuilderInterface buildUserState(UserState UserState) {
        this.userState = UserState;
        return this;
    }

    @Override
    public User build() {
        return new User(id,fullName,userName,password,phoneNumber,email,address,avatar,role,userState);
    }
}
