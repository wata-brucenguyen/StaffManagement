package com.example.staffmanagement.Presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.staffmanagement.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Database.DAL.RoleDbHandler;

import com.example.staffmanagement.Database.DAL.StateRequestDbHandler;
import com.example.staffmanagement.Database.DAL.UserDbHandler;
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

    public void checkLoginInformation(final String userName, final String password){
        mInterface.createNewProgressDialog("Logging in ...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserDbHandler db = new UserDbHandler(mContext);
                final User user = db.getByLoginInformation(userName,password);

                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if( user == null ) {
                            mInterface.showMessage("Login failed");
                        } else
                            mInterface.loginSuccess(user);

                        mInterface.dismissProgressDialog();
                    }
                });
            }
        }).start();
    }



    public void prepareData() {
        mInterface.createNewProgressDialog("Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoleDbHandler dbRole = new RoleDbHandler(mContext);
                StateRequestDbHandler dbStateRequest = new StateRequestDbHandler(mContext);
                UserDbHandler dbUser = new UserDbHandler(mContext);
                RequestDbHandler dbRequest = new RequestDbHandler(mContext);
                mInterface.dismissProgressDialog();
            }
        }).start();
    }
}
