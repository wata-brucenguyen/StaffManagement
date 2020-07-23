package com.example.staffmanagement.Presenter;

import android.content.Context;
import android.util.Log;

import com.example.staffmanagement.Database.DAL.RoleDbHandler;
import com.example.staffmanagement.Database.Entity.Role;
import com.example.staffmanagement.LogInInterface;

import java.util.ArrayList;

public class LogInPresenter {

    private Context mContext;
    private LogInInterface mInterface;

    public LogInPresenter(Context mContext, LogInInterface mInterface) {
        this.mContext = mContext;
        this.mInterface = mInterface;
    }

    public void getAllRole(){
        RoleDbHandler db = new RoleDbHandler(mContext);
        ArrayList<Role> list = db.getAll();
        for(Role item : list){
            Log.i("DATABASE","item role : " + item.getId() + " " + item.getName());
        }
    }

    public void prepareData(){
        mInterface.createNewProgressDialog("Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoleDbHandler dbRole = new RoleDbHandler(mContext);
//                StateRequestDbHandler dbStateRequest = new StateRequestDbHandler(mContext);
//                UserDbHandler dbUser = new UserDbHandler(mContext);
//                RequestDbHandler dbRequest = new RequestDbHandler(mContext);
                mInterface.dismissProgressDialog();
            }
        }).start();
    }
}
