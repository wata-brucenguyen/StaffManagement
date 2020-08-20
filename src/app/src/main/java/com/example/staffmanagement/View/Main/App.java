package com.example.staffmanagement.View.Main;

import android.app.Application;

import com.example.staffmanagement.Model.Repository.AppDatabase;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.getInstance(this);
    }

}
