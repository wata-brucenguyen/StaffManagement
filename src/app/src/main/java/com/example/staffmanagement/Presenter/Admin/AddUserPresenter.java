package com.example.staffmanagement.Presenter.Admin;

import android.content.Context;

import com.example.staffmanagement.Model.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Model.Database.Entity.Role;
import com.example.staffmanagement.View.Admin.UserManagementActivity.AddUserInterface;

import java.util.ArrayList;

public class AddUserPresenter {
    private Context mContext;
    private AddUserInterface mInterface;

    public AddUserPresenter(Context context, AddUserInterface mInterface) {
        this.mContext = context;
        this.mInterface = mInterface;
    }

    public ArrayList<Role> getAllRole() {
        UserDbHandler db = new UserDbHandler(mContext);
        return db.getAllRole();
    }
}
