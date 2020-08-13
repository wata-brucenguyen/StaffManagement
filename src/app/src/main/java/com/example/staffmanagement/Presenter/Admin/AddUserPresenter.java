package com.example.staffmanagement.Presenter.Admin;

import android.app.Activity;
import android.content.Context;

import com.example.staffmanagement.Model.LocalDb.BUS.RoleBUS;
import com.example.staffmanagement.Model.LocalDb.BUS.UserBUS;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.Role;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.User;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AddUserInterface;

import java.lang.ref.WeakReference;
import java.util.List;

public class AddUserPresenter {
    private Context mContext;
    private AddUserInterface mInterface;

    public AddUserPresenter(Context context, AddUserInterface mInterface) {
        WeakReference<Context> weakReference = new WeakReference<>(context);
        this.mContext = weakReference.get();
        this.mInterface = mInterface;
    }

    public void getAllRole() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoleBUS bus = new RoleBUS();
                final List<Role> list = bus.getAll();
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.onLoadRoleList(list);
                    }
                });
            }
        }).start();

    }

    public void checkUserNameIsExisted(final User user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserBUS bus = new UserBUS();
                final boolean b = bus.checkUserNameIsExisted(user.getUserName());
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.onCheckUserNameIsExist(b,user);
                    }
                });
            }
        }).start();

    }
}
