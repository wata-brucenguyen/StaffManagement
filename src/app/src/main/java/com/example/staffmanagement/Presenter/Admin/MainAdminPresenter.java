package com.example.staffmanagement.Presenter.Admin;

import android.content.Context;

import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.View.Admin.MainAdminActivity.MainAdminInterface;

import java.util.ArrayList;

public class MainAdminPresenter {
    private Context mContext;
    private MainAdminInterface mInterface;

    public MainAdminPresenter(Context mContext, MainAdminInterface mInterface) {
        this.mContext = mContext;
        this.mInterface = mInterface;
    }

    public ArrayList<User> getUserList() {
        mInterface.setRefresh(true);
        UserDbHandler db = new UserDbHandler(mContext);
        mInterface.setRefresh(false);
        return db.getAll();
    }

    public ArrayList<User> findFullName(int idUser, String name) {
        UserDbHandler db = new UserDbHandler(mContext);
        return db.findRequestByFulName(idUser, name);
    }

    public void insertUser(User user) {
        UserDbHandler db = new UserDbHandler(mContext);
        db.insert(user);
    }

    public String getRoleNameById(int idRole) {
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getRoleNameById(idRole);
    }

    public int getCountWaitingForRequest(int idUser){
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getCountWaitingForUser(idUser);
    }

    public void deleteUser(int idUser) {
        UserDbHandler db = new UserDbHandler(mContext);
        db.delete(idUser);
    }
}
