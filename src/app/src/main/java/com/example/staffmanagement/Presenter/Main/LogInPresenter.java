package com.example.staffmanagement.Presenter.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.staffmanagement.Model.BUS.DatabaseInitialization;
import com.example.staffmanagement.Model.BUS.UserBUS;

import com.example.staffmanagement.Model.Database.Entity.User;

import com.example.staffmanagement.View.Main.LogInActivity;
import com.example.staffmanagement.View.Main.LogInInterface;
import com.example.staffmanagement.View.Staff.ViewModel.LoginViewModel;
import com.example.staffmanagement.View.Ultils.Constant;

import java.lang.ref.WeakReference;

public class LogInPresenter {

    private Context mContext;
    private LogInInterface mInterface;

    public LogInPresenter(Context context, LogInInterface mInterface) {
        WeakReference<Context> weak = new WeakReference<>(context);
        this.mContext = weak.get();
        this.mInterface = mInterface;
    }

    public void checkLoginInformation(final String userName, final String password) {
        mInterface.showFragment(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final User user = new UserBUS().getByLoginInformation(mContext, userName, password);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user == null) {
                            mInterface.showMessage("Login failed");
                            mInterface.showFragment(1);
                        } else if (user.getIdUserState() != 1) {
                            mInterface.showMessage("Account is locked");
                            mInterface.showFragment(1);
                        } else
                            mInterface.onLoginSuccess(user);
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

    public void getUserForLogin(final int idUser) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new UserBUS().getById(mContext, idUser);
                mInterface.onLoginSuccess(user);
            }
        }).start();
    }

    private void sleep(final int millis) {
        try {
            Thread.sleep(millis);
            Intent intent = new Intent(mContext, LogInActivity.class);
            ((Activity) mContext).startActivity(intent);
            ((Activity) mContext).finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void checkIsLogin(final LoginViewModel viewModel, final int mode){
        if (!viewModel.isCheckLogin())
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        viewModel.setCheckLogin(true);
                        mInterface.showFragment(0);
                        Thread.sleep(1000);
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME,mode);
                        boolean b = sharedPreferences.getBoolean(Constant.SHARED_PREFERENCE_IS_LOGIN, false);
                        if (b) {
                            int idUser = sharedPreferences.getInt(Constant.SHARED_PREFERENCE_ID_USER, -1);
                            getUserForLogin(idUser);
                        } else {
                            mInterface.showFragment(1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

    }
}
