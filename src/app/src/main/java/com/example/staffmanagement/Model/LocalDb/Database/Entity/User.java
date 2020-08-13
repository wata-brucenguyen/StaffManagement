package com.example.staffmanagement.Model.LocalDb.Database.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import com.example.staffmanagement.Model.LocalDb.Database.Ultils.ConstString;

import java.io.Serializable;

@Entity(tableName = ConstString.USER_TABLE_NAME)
public class User implements Serializable, Comparable{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ConstString.USER_COL_ID)
    private int Id;

    @ColumnInfo(name = ConstString.USER_COL_ID_ROLE)
    private int IdRole;

    @ColumnInfo(name = ConstString.USER_COL_FULL_NAME)
    private String FullName;

    @ColumnInfo(name = ConstString.USER_COL_USERNAME)
    private String UserName;

    @ColumnInfo(name = ConstString.USER_COL_PASSWORD)
    private String Password;

    @ColumnInfo(name = ConstString.USER_COL_PHONE_NUMBER)
    private String PhoneNumber;

    @ColumnInfo(name = ConstString.USER_COL_EMAIL)
    private String Email;

    @ColumnInfo(name = ConstString.USER_COL_ADDRESS)
    private String Address;

    @ColumnInfo(name = ConstString.USER_COL_AVATAR)
    private byte[] Avatar;

    @ColumnInfo(name = ConstString.USER_COL_ID_USER_STATE)
    private int IdUserState;

    public User() {
    }

    public User(int id, int idRole, String fullName, String userName, String password, String phoneNumber, String email, String address, byte[] avatar, int idUserState) {
        this.Id = id;
        this.IdRole = idRole;
        this.FullName = fullName;
        this.UserName = userName;
        this.Password = password;
        this.PhoneNumber = phoneNumber;
        this.Email = email;
        this.Address = address;
        this.Avatar = avatar;
        this.IdUserState = idUserState;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getIdRole() {
        return IdRole;
    }

    public void setIdRole(int idRole) {
        this.IdRole = idRole;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        this.FullName = fullName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public byte[] getAvatar() {
        return Avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.Avatar = avatar;
    }

    public int getIdUserState() {
        return IdUserState;
    }

    public void setIdUserState(int idUserState) {
        this.IdUserState = idUserState;
    }


    @Override
    public int compareTo(Object o) {
        if (this == o) return 1;
        if (o == null || getClass() != o.getClass()) return 0;
        User user = (User) o;
        if (Id == user.getId() &&
                IdRole == user.getIdRole() &&
                IdUserState == user.getIdUserState() &&
                FullName.equals(user.getFullName()) &&
                UserName.equals(user.UserName) &&
                Password.equals(user.Password) &&
                PhoneNumber.equals(user.PhoneNumber) &&
                Email.equals(user.getEmail()) &&
                Address.equals(user.getAddress()) &&
                Avatar.equals(user.getAvatar()))
            return 1;
        return 0;
    }
}
