package com.example.staffmanagement.MVVM.View.Admin.UserManagementActivity;

public interface AdminInformationInterface {
    void showChangePassword(String message);
    void showMessage(String message);
    void dismissProgressDialog();
    void showProgressDialog();
    void createNewProgressDialog(String message);
    void onSuccessChangeAvatar();
    void onSuccessUpdateProfile();
    void loadAdminProfile(String roleName);
    void loadStaffProfile(String roleName);
}
