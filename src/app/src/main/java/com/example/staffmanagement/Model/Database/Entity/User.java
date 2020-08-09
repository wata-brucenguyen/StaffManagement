package com.example.staffmanagement.Model.Database.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


import com.example.staffmanagement.Model.Database.Ultils.ConstString;

import java.io.Serializable;

@Entity(tableName = ConstString.USER_TABLE_NAME)
public class User implements Serializable{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ConstString.USER_COL_ID)
    private int id;

    @ColumnInfo(name = ConstString.USER_COL_ID_ROLE)
    private int idRole;

    @ColumnInfo(name = ConstString.USER_COL_FULL_NAME)
    private String fullName;

    @ColumnInfo(name = ConstString.USER_COL_USERNAME)
    private String userName;

    @ColumnInfo(name = ConstString.USER_COL_PASSWORD)
    private String password;

    @ColumnInfo(name = ConstString.USER_COL_PHONE_NUMBER)
    private String phoneNumber;

    @ColumnInfo(name = ConstString.USER_COL_EMAIL)
    private String email;

    @ColumnInfo(name = ConstString.USER_COL_ADDRESS)
    private String address;

    @ColumnInfo(name = ConstString.USER_COL_AVATAR)
    private byte[] avatar;

    @ColumnInfo(name = ConstString.USER_COL_ID_USER_STATE)
    private int idUserState;

    public User() {
    }

    public User(int id, int idRole, String fullName, String userName, String password, String phoneNumber, String email, String address, byte[] avatar, int idUserState) {
        this.id = id;
        this.idRole = idRole;
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.avatar = avatar;
        this.idUserState = idUserState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
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

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public int getIdUserState() {
        return idUserState;
    }

    public void setIdUserState(int idUserState) {
        this.idUserState = idUserState;
    }
}
