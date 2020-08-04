package com.example.staffmanagement.Presenter.Staff;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.example.staffmanagement.Model.BUS.RoleBUS;
import com.example.staffmanagement.Model.BUS.UserBUS;
import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Staff.UserProfile.StaffUserProfileInterface;
import com.example.staffmanagement.View.Ultils.ImageHandler;

import java.lang.ref.WeakReference;

public class StaffUserProfilePresenter {
    private Context mContext;
    private StaffUserProfileInterface mInterface;

    public StaffUserProfilePresenter(Context context, StaffUserProfileInterface mInterface) {
        WeakReference<Context> weak = new WeakReference<>(context);
        this.mContext = context;
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
    }

    public void updateUserProfile(final User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.createNewProgressDialog("Loading...");
                        mInterface.showProgressDialog();
                    }
                });
                new UserBUS().update(mContext, user);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.dismissProgressDialog();
                    }
                });
            }
        }).start();

    }

    public void getRoleNameById(final int idRole) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoleBUS bus = new RoleBUS();
                final String name = bus.getRoleNameById(mContext,idRole);
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.onSuccessGetRoleName(name);
                    }
                });

            }
        }).start();
    }

    public void changeAvatar(final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.createNewProgressDialog("Loading...");
                        mInterface.showProgressDialog();
                    }
                });
                byte[] bytes = ImageHandler.getByteArrayFromBitmap(bitmap);
                UserSingleTon.getInstance().getUser().setAvatar(bytes);
                new UserBUS().update(mContext,UserSingleTon.getInstance().getUser());
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
