package com.example.staffmanagement.Presenter.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.staffmanagement.Model.BUS.AppDatabase;
import com.example.staffmanagement.Model.BUS.DatabaseInitialization;
import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.RoleDbHandler;

import com.example.staffmanagement.Model.Database.DAL.StateRequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.UserDbHandler;
import com.example.staffmanagement.Model.Database.Entity.User;

import com.example.staffmanagement.View.Main.LogInActivity;
import com.example.staffmanagement.View.Main.LogInInterface;
import com.example.staffmanagement.View.Main.LoginTransData;

import java.lang.ref.WeakReference;

public class LogInPresenter {

    private Context mContext;
    private LogInInterface mInterface;
    private LoginTransData mLoginInterface;
    //private AppDatabase mDatabase;
    public LogInPresenter(Context mContext, LogInInterface mInterface) {
        this.mContext = mContext;
        this.mInterface = mInterface;
        //mDatabase = AppDatabase.getInstance(mContext);
        WeakReference<Context> weak = new WeakReference<>(this.mContext);
    }

    public void checkLoginInformation(final String userName, final String password){
//        mInterface.createNewProgressDialog("Logging in ...");
        mInterface.showFragment(0);
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
                            mInterface.showFragment(1);
                        } else
                            mInterface.onLoginSuccess(user);

                       // mInterface.dismissProgressDialog();
                    }
                });
            }
        }).start();
    }

    public void prepareData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseInitialization.initialize(mContext);
                sleep(2500);
            }
        }).start();
    }

    public void getUserForLogin(final int idUser){
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = AppDatabase.getInstance(mContext).userDAO().getById(idUser);
                mInterface.onLoginSuccess(user);
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
