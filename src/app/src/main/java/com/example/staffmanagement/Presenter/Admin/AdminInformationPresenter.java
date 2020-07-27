package com.example.staffmanagement.Presenter.Admin;

import android.content.Context;

import com.example.staffmanagement.Model.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AdminInformationInterface;

import java.util.ArrayList;

public class AdminInformationPresenter {
    private Context mContext;
    private AdminInformationInterface mInterface;

    public AdminInformationPresenter(Context mContext, AdminInformationInterface mInterface) {
        this.mContext = mContext;
        this.mInterface = mInterface;
    }

    public void resetPassword(int idUser) {
        UserDbHandler db = new UserDbHandler(mContext);
        mInterface.showChangePassword("Reset password successfully");
        db.resetPassword(idUser);
    }

    public void changePassword(int idUser, String password) {
        UserDbHandler db = new UserDbHandler(mContext);
        db.changePassword(idUser, password);
        mInterface.showChangePassword("Change password successfully");
    }

    public ArrayList<Role> getAllRole() {
        UserDbHandler db = new UserDbHandler(mContext);
        return db.getAllRole();
    }

    public void update(User user) {
        UserDbHandler db = new UserDbHandler(mContext);
        db.update(user);
    }
}
