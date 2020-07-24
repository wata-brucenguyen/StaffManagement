package com.example.staffmanagement.Presenter;

import android.content.Context;

import com.example.staffmanagement.Admin.UserManagementActivity.AdminInformationInterface;
import com.example.staffmanagement.Admin.UserRequestActivity.UserRequestInterface;
import com.example.staffmanagement.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Database.Entity.User;
import com.example.staffmanagement.NonAdmin.UserProfileActivity.UserProfileNonAdminInterface;

public class UserPresenter {
    private Context mContext;
    private AdminInformationInterface mAdminInfoInterface;
    private UserRequestInterface mUserRequestInterface;
    private UserProfileNonAdminInterface userProfileNonAdminInterface;

    public UserPresenter(Context mContext, AdminInformationInterface mInterface) {
        this.mContext = mContext;
        this.mAdminInfoInterface = mInterface;
    }

    public UserPresenter(Context mContext, UserRequestInterface mInterface) {
        this.mContext = mContext;
        this.mUserRequestInterface = mInterface;
    }

    public UserPresenter(Context mContext, UserProfileNonAdminInterface userProfileNonAdminInterface) {
        this.mContext = mContext;
        this.userProfileNonAdminInterface = userProfileNonAdminInterface;
    }

    public void getUserList(){
        UserDbHandler db = new UserDbHandler(mContext);
        db.getAll();
    }
    public void resetPassword(int idUser){
        UserDbHandler db = new UserDbHandler(mContext);
        db.resetPassword(idUser);
    }

    public void changePassword(int idUser, String password) {
        UserDbHandler db = new UserDbHandler(mContext);
        db.changePassword(idUser,password);
    }

    public void updateUserProfile(User user){
        UserDbHandler db = new UserDbHandler(mContext);
        db.update(user);
    }
}
