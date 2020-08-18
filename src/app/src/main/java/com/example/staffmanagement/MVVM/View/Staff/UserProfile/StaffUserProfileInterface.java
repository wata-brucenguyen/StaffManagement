package com.example.staffmanagement.MVVM.View.Staff.UserProfile;

public interface StaffUserProfileInterface {
    void onFailChangePassword(String message);
    void onSuccessChangePassword();
    void createNewProgressDialog(String message);
    void setMessageProgressDialog(String message);
    void dismissProgressDialog();
    void showProgressDialog();
    void onSuccessChangeAvatar();
    void onSuccessGetRoleName(String roleName);
}
