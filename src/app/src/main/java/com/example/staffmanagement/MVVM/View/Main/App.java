package com.example.staffmanagement.MVVM.View.Main;

import android.app.Application;

import com.example.staffmanagement.MVVM.Model.Repository.AppDatabase;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.getInstance(this);
    }

}
