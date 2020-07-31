package com.example.staffmanagement.Presenter.Admin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AdminInformationInterface;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Ultils.ImageHandler;

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
        User user = db.getById(idUser);
        user.setPassword(password);
        db.update(user);
        mInterface.showChangePassword("Change password successfully");
    }

    public void update(User user) {
        UserDbHandler db = new UserDbHandler(mContext);
        db.update(user);
        mInterface.onSuccessUpdateProfile();
        mInterface.showMessage("Update profile successfully");
    }

    public String getRoleNameById(int idRole) {
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getRoleNameById(idRole);
    }

    public void changeAvatar(final Bitmap bitmap){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.createNewProgressDialog("Loading...");
                        mInterface.showProgressDialog();
                    }
                });
                byte[] bytes = ImageHandler.getByteArrayFromBitmap(bitmap);
                UserSingleTon.getInstance().getUser().setAvatar(bytes);
                UserDbHandler db = new UserDbHandler(mContext);
                db.changeAvatar(UserSingleTon.getInstance().getUser());
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.onSuccessChangeAvatar();
                        mInterface.dismissProgressDialog();
                    }
                });
            }
        }).start();
    }
}
