package com.example.staffmanagement.MVVM.Model.Repository.User;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.staffmanagement.Model.LocalDb.Database.Ultils.ConstString;

import java.io.Serializable;

public class UserClone{

    private int Id;

    private int IdRole;

    private String FullName;

    private String UserName;

    private String Password;

    private String PhoneNumber;

    private String Email;

    private String Address;

    private byte[] Avatar;

    private int IdUserState;

    public UserClone() {
    }

    public UserClone(int Id, int IdRole, String FullName, String UserName, String Password, String PhoneNumber, String Email, String Address, byte[] Avatar, int IdUserState) {
        this.Id = Id;
        this.IdRole = IdRole;
        this.FullName = FullName;
        this.UserName = UserName;
        this.Password = Password;
        this.PhoneNumber = PhoneNumber;
        this.Email = Email;
        this.Address = Address;
        this.Avatar = Avatar;
        this.IdUserState = IdUserState;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIdRole() {
        return IdRole;
    }

    public void setIdRole(int idRole) {
        IdRole = idRole;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public byte[] getAvatar() {
        return Avatar;
    }

    public void setAvatar(byte[] avatar) {
        Avatar = avatar;
    }

    public int getIdUserState() {
        return IdUserState;
    }

    public void setIdUserState(int idUserState) {
        IdUserState = idUserState;
    }
}
