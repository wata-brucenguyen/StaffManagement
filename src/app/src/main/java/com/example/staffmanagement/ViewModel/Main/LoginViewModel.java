package com.example.staffmanagement.ViewModel.Main;

import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Repository.User.UserRepository;

public class LoginViewModel extends ViewModel {
    private boolean isCheckLogin = false;
    private String username = "";
    private String password = "";
    private boolean isRemember = false;


    public void setAllData(String username, String password, boolean isRemember) {
        this.username = username;
        this.password = password;
        this.isRemember = isRemember;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsRemember() {
        return isRemember;
    }

    public void setIsRemember(boolean isRemember) {
        this.isRemember = isRemember;
    }

    public boolean isCheckLogin() {
        return isCheckLogin;
    }

    public void setCheckLogin(boolean checkLogin) {
        isCheckLogin = checkLogin;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        username = null;
        password = null;
        isRemember = false;
    }

    public User getUserForLogin(final int idUser) {
        return new UserRepository().getUserForLogin(idUser);
    }

    public User getByLoginInformation(){
        return new UserRepository().getByLoginInformation(username,password);
    }
}
