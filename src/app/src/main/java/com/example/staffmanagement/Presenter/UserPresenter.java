package com.example.staffmanagement.Presenter;

import android.content.Context;

import com.example.staffmanagement.Admin.MainAdminActivity.MainAdminInterface;
import com.example.staffmanagement.Admin.UserManagementActivity.AddUserInterface;
import com.example.staffmanagement.Admin.UserManagementActivity.AdminInformationInterface;
import com.example.staffmanagement.Admin.UserRequestActivity.UserRequestInterface;
import com.example.staffmanagement.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Database.Entity.Role;
import com.example.staffmanagement.Database.Entity.User;
import com.example.staffmanagement.NonAdmin.UserProfileActivity.UserProfileNonAdminInterface;

import java.util.ArrayList;

public class UserPresenter {
    private Context mContext;
    private AdminInformationInterface mAdminInfoInterface;
    private UserRequestInterface mUserRequestInterface;
    private UserProfileNonAdminInterface userProfileNonAdminInterface;
    private MainAdminInterface mainAdminInterface;

    private AddUserInterface mAddUserInterface;

    public UserPresenter(Context mContext, AdminInformationInterface mInterface) {
        this.mContext = mContext;
        this.mAdminInfoInterface = mInterface;
    }

    public UserPresenter(Context mContext, AddUserInterface mInterface){
        this.mContext = mContext;
        this.mAddUserInterface = mInterface;
    }
    public UserPresenter(Context mContext, MainAdminInterface mainAdminInterface) {
        this.mContext = mContext;
        this.mainAdminInterface = mainAdminInterface;
    }

    public UserPresenter(Context mContext, UserRequestInterface mInterface) {
        this.mContext = mContext;
        this.mUserRequestInterface = mInterface;
    }



    public UserPresenter(Context mContext, UserProfileNonAdminInterface userProfileNonAdminInterface) {
        this.mContext = mContext;
        this.userProfileNonAdminInterface = userProfileNonAdminInterface;
    }


    public ArrayList<User> getUserList(){
        mainAdminInterface.setRefresh(true);
        UserDbHandler db = new UserDbHandler(mContext);
        mainAdminInterface.setRefresh(false);
        return db.getAll();
    }
  
    public void resetPassword(int idUser){
        UserDbHandler db = new UserDbHandler(mContext);
        mAdminInfoInterface.showChangePassword("Reset password successfully");
        db.resetPassword(idUser);
    }

    public void changePassword(int idUser, String password) {
        UserDbHandler db = new UserDbHandler(mContext);
        db.changePassword(idUser,password);
        mAdminInfoInterface.showChangePassword("Change password successfully");
    }

    public void update(User user)
    {
        UserDbHandler db = new UserDbHandler(mContext);
        db.update(user);
    }

    public ArrayList<Role> getAllRole(){
        UserDbHandler db = new UserDbHandler(mContext);
        return db.getAllRole();
    }

    public int getIdRole(int id){
        UserDbHandler db = new UserDbHandler(mContext);
        return db.getIdRole(id);
    }
    public void deleteUser(int idUser){
        UserDbHandler db = new UserDbHandler(mContext);
        db.delete(idUser);
    }


    public void insertUser(User user){
        UserDbHandler db = new UserDbHandler(mContext);
        db.insert(user);

    public void updateUserProfile(User user){
        UserDbHandler db = new UserDbHandler(mContext);
        db.update(user);

    }
}
