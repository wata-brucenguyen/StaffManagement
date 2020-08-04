package com.example.staffmanagement.Presenter.Admin;

import android.content.Context;

import com.example.staffmanagement.Model.BUS.UserBUS;
import com.example.staffmanagement.Model.Database.Entity.Role;
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
                UserBUS bus = new UserBUS();
                List<Role> list = bus.getAllRole(mContext);
                mInterface.onLoadRoleList(list);
            }
        }).start();

    }

    public boolean checkUserNameIsExisted(String userName){
        UserBUS bus = new UserBUS();
        return bus.checkUserNameIsExisted(mContext,userName);
    }
}
