package com.example.staffmanagement.ViewModel.Main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Entity.User;
import com.example.staffmanagement.Model.Repository.UserRepository;

public class LoginViewModel extends ViewModel {
    private boolean isCheckLogin = false;
    private String username = "";
    private String password = "";
    private boolean isRemember = false;
    public MutableLiveData<User> mUserLD;
    private UserRepository userRepository = new UserRepository();
    public MutableLiveData<ACTION> mAction = new MutableLiveData<>();
    public MutableLiveData<ERROR> mError = new MutableLiveData<>();

    public LoginViewModel() {
        this.mUserLD = userRepository.getUserLD();
        this.mAction.setValue(ACTION.NONE);
        this.mError.setValue(ERROR.NONE);
    }

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

    public void getUserForLogin(final int idUser) {
        userRepository.getUserForLogin(idUser);
    }

    public void getByLoginInformation() {
        userRepository.getByLoginInformation(username, password);
    }

    public enum ACTION {
        LOGIN, LOGGED_IN, NONE
    }

    public enum ERROR {
        LOGIN_FAIL, ACCOUNT_LOCKED, NONE, LOGIN_SUCCESS
    }
}
