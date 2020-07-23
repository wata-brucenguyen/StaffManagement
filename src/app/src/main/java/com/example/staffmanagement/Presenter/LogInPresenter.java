package com.example.staffmanagement.Presenter;

import android.content.Context;
import android.util.Log;

import com.example.staffmanagement.Database.DAL.RoleDbHandler;
import com.example.staffmanagement.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Database.Entity.Role;
import com.example.staffmanagement.Database.Entity.User;
import com.example.staffmanagement.LogInInterface;

import java.util.ArrayList;

public class LogInPresenter {

    private Context mContext;
    private LogInInterface mInterface;

    public LogInPresenter(Context mContext, LogInInterface mInterface) {
        this.mContext = mContext;
        this.mInterface = mInterface;
    }

    public void getAllUser() {
        UserDbHandler db = new UserDbHandler(mContext);
        ArrayList<User> list = db.getAll();
        for (User item : list) {
            Log.i("DATABASE", "item user : " + item.getId() + " " + item.getFullName());
        }
    }

    public void prepareData() {
        mInterface.createNewProgressDialog("Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoleDbHandler dbRole = new RoleDbHandler(mContext);
//                StateRequestDbHandler dbStateRequest = new StateRequestDbHandler(mContext);
                UserDbHandler dbUser = new UserDbHandler(mContext);
//                RequestDbHandler dbRequest = new RequestDbHandler(mContext);
                mInterface.dismissProgressDialog();
            }
        }).start();
    }
}
