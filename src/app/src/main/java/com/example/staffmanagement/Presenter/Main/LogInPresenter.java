package com.example.staffmanagement.Presenter.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.RoleDbHandler;

import com.example.staffmanagement.Model.Database.DAL.StateRequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Model.Database.Entity.User;

import com.example.staffmanagement.View.Main.LogInActivity;
import com.example.staffmanagement.View.Main.LogInInterface;
import com.example.staffmanagement.View.Main.SplashSreeenActivity;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoleDbHandler dbRole = new RoleDbHandler(mContext);
                StateRequestDbHandler dbStateRequest = new StateRequestDbHandler(mContext);
                UserDbHandler dbUser = new UserDbHandler(mContext);
                RequestDbHandler dbRequest = new RequestDbHandler(mContext);
                sleep(2500);
            }
        }).start();
    }

    public void getUserForLogin(final int idUser){
        mInterface.createNewProgressDialog("Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserDbHandler db = new UserDbHandler(mContext);
                User user = db.getById(idUser);
                mInterface.dismissProgressDialog();
                mInterface.loginSuccess(user);
            }
        }).start();
    }

    private void sleep(final int millis){
        try {
            Thread.sleep(millis);
            Intent intent=new Intent(mContext, LogInActivity.class);
            ((Activity)mContext).startActivity(intent);
            ((Activity)mContext).finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
