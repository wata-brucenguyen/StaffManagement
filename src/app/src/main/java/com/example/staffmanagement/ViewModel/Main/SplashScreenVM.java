package com.example.staffmanagement.ViewModel.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.ViewModel;

import com.example.staffmanagement.Model.Repository.DatabaseInitialization;
import com.example.staffmanagement.View.Main.LoginActivity;

public class SplashScreenVM extends ViewModel {

    public void prepareData(Context context) {
        new Thread(() -> {
            DatabaseInitialization.initialize();
            sleep(context,2500);
        }).start();
    }

    private void sleep(Context context,final int millis) {
        try {
            Thread.sleep(millis);
            Intent intent = new Intent(context, LoginActivity.class);
            ((Activity) context).startActivity(intent);
            ((Activity) context).finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
