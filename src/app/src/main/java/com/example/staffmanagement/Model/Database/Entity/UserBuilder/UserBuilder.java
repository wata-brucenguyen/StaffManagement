package com.example.staffmanagement.Model.Database.Entity.UserBuilder;

import com.example.staffmanagement.Model.Database.Entity.User;

public class UserBuilder implements UserBuilderInterface {

    private int id,idRole,idUserState;
    private String fullName,userName,password,phoneNumber,email,address;
    private byte[] avatar;

    @Override
    public UserBuilderInterface buildId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public UserBuilderInterface buildIdRole(int idRole) {
        this.idRole = idRole;
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
    public UserBuilderInterface buildAvatar(byte[] avatar) {
        this.avatar = avatar;
        return this;
    }

    @Override
    public UserBuilderInterface buildIdUserState(int idUserState) {
        this.idUserState = idUserState;
        return this;
    }

    @Override
    public User build() {
        return new User(id,idRole,fullName,userName,password,phoneNumber,email,address,avatar,idUserState);
    }
}
