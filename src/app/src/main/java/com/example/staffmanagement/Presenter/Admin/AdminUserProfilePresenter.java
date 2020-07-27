package com.example.staffmanagement.Presenter.Admin;

import android.content.Context;
import android.text.TextUtils;

import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.View.Admin.AdminProfile.AdminProfileInterface;
import com.example.staffmanagement.View.Data.UserSingleTon;

public class AdminUserProfilePresenter {
    Context context;
    AdminProfileInterface mInterface;

    public AdminUserProfilePresenter(Context context, AdminProfileInterface adminProfileInterface) {
        this.context = context;
        this.mInterface = adminProfileInterface;
    }
    public void checkInfoChangePassword(String oldPass, String newPass, String confirmNewPass) {
        if (TextUtils.isEmpty(oldPass) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(confirmNewPass)) {
            mInterface.onFailChangePassword("Some field is empty");
            return;
        }
        if (!UserSingleTon.getInstance().getUser().getPassword().equals(oldPass)) {
            mInterface.onFailChangePassword("Old password is wrong");
            return;
        }
        if (newPass.length() < 6) {
            mInterface.onFailChangePassword("New password must more 6 characters");
            return;
        }
        if (!newPass.equals(confirmNewPass)) {
            mInterface.onFailChangePassword("Confirm password is wrong");
            return;
        }

        UserSingleTon.getInstance().getUser().setPassword(newPass);
        updateUserProfile(UserSingleTon.getInstance().getUser());
        mInterface.onSuccessChangePassword();
    }

    public void updateUserProfile(User user) {
        UserDbHandler db = new UserDbHandler(context);
        db.update(user);
    }

    public String getRoleNameById(int idRole) {
        RequestDbHandler db = new RequestDbHandler(context);
        return db.getRoleNameById(idRole);
    }
}
