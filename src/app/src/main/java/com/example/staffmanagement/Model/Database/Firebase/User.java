package com.example.staffmanagement.Model.Database.Firebase;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String idRole;
    private String idUserState;
    private String fullName;
    private String userName;
    private String password;
    private String phoneNumber;
    private String email;
    private String address;
    private String avatarLink;

    public User() {
    }

    public User(String id, String idRole, String idUserState, String fullName, String userName, String password, String phoneNumber, String email, String address, String avatarLink) {
        this.id = id;
        this.idRole = idRole;
        this.idUserState = idUserState;
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.avatarLink = avatarLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdRole() {
        return idRole;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public String getIdUserState() {
        return idUserState;
    }

    public void setIdUserState(String idUserState) {
        this.idUserState = idUserState;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }
}
