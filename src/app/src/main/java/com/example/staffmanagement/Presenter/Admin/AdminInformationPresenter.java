package com.example.staffmanagement.Presenter.Admin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.staffmanagement.Model.BUS.RequestBUS;
import com.example.staffmanagement.Model.BUS.RoleBUS;
import com.example.staffmanagement.Model.BUS.UserBUS;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AdminInformationInterface;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Ultils.ImageHandler;

import java.lang.ref.WeakReference;

public class AdminInformationPresenter {
    private Context mContext;
    private AdminInformationInterface mInterface;

    public AdminInformationPresenter(Context context, AdminInformationInterface mInterface) {
        WeakReference<Context> weakReference = new WeakReference<>(context);
        this.mContext = weakReference.get();
        this.mInterface = mInterface;
    }

    public void resetPassword(final int idUser) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserBUS bus = new UserBUS();
                bus.resetPassword(idUser);
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.showChangePassword("Reset password successfully");
                    }
                });
            }
        }).start();

    }

    public void changePassword(final int idUser, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserBUS bus = new UserBUS();
                User user = bus.getById(idUser);
                user.setPassword(password);
                bus.update( user);
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.showChangePassword("Change password successfully");
                    }
                });
            }
        }).start();

    }

    public void update(final User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserBUS bus = new UserBUS();
                bus.update(user);
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.onSuccessUpdateProfile();
                        mInterface.showMessage("Update profile successfully");
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
                final String roleName = bus.getRoleNameById(mContext, idRole);
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (idRole) {
                            case 1:
                                mInterface.loadAdminProfile(roleName);
                                break;
                            case 2:
                                mInterface.loadStaffProfile(roleName);
                                break;
                        }
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
                UserBUS bus = new UserBUS();
                bus.changeAvatar( UserSingleTon.getInstance().getUser());
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
