package com.example.staffmanagement.Presenter.Staff;

import android.content.Context;
import android.text.TextUtils;

import com.example.staffmanagement.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Database.Entity.User;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Staff.UserProfile.StaffUserProfileInterface;

public class StaffUserProfilePresenter {
    private Context mContext;
    private StaffUserProfileInterface mInterface;

    public StaffUserProfilePresenter(Context mContext, StaffUserProfileInterface mInterface) {
        this.mContext = mContext;
        this.mInterface = mInterface;
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
        UserDbHandler db = new UserDbHandler(mContext);
        db.update(user);
    }
}
