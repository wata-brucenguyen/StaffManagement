package com.example.staffmanagement.Model.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.staffmanagement.Model.Ultils.ConstString;

import java.io.Serializable;

@Entity(tableName = ConstString.USER_TABLE_NAME)
public class User implements Serializable, Comparable{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ConstString.USER_COL_ID)
    private int Id;

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
    private String Avatar;

    @Ignore
    private Role Role;

    @Ignore
    private UserState UserState;

    public User() {
    }

    public User(int id, String fullName, String userName, String password, String phoneNumber, String email, String address, String avatar, Role role, UserState userState) {
        Id = id;
        FullName = fullName;
        UserName = userName;
        Password = password;
        PhoneNumber = phoneNumber;
        Email = email;
        Address = address;
        Avatar = avatar;
        UserState = UserState;
        Role = role;
        UserState = userState;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
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

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        this.Avatar = avatar;
    }

    public com.example.staffmanagement.Model.Entity.Role getRole() {
        return Role;
    }

    public void setRole(com.example.staffmanagement.Model.Entity.Role role) {
        Role = role;
    }

    public com.example.staffmanagement.Model.Entity.UserState getUserState() {
        return UserState;
    }

    public void setUserState(com.example.staffmanagement.Model.Entity.UserState userState) {
        UserState = userState;
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 1;
        if (o == null || getClass() != o.getClass()) return 0;
        User user = (User) o;
        if (Id == user.getId() &&
                Role.getId() == user.getRole().getId() &&
                UserState.getId() == user.getUserState().getId() &&
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
