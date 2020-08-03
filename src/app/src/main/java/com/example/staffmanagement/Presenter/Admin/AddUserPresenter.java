package com.example.staffmanagement.Presenter.Admin;

import android.content.Context;

import com.example.staffmanagement.Model.BUS.UserBUS;
import com.example.staffmanagement.Model.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AddUserInterface;

import java.lang.ref.WeakReference;
import java.util.List;

public class AddUserPresenter {
    private Context mContext;
    private AddUserInterface mInterface;

    public AddUserPresenter(Context context, AddUserInterface mInterface) {
        this.mContext = context;
        this.mInterface = mInterface;
        WeakReference<Context> wf = new WeakReference<>(this.mContext);
    }

    public List<Role> getAllRole() {
        UserBUS bus = new UserBUS();
        return bus.getAllRole(mContext);
    }

    public boolean checkUserNameIsExisted(String userName){
        UserBUS bus = new UserBUS();
        return bus.checkUserNameIsExisted(mContext,userName);
    }
}
