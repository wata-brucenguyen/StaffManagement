package com.example.staffmanagement.View.Main;

import android.app.Application;

import com.example.staffmanagement.Model.Repository.AppDatabase;
import com.example.staffmanagement.View.Ultils.CheckNetwork;

public class App extends Application {

    private CheckNetwork mCheckNetwork;
    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.getInstance(this);
        mCheckNetwork = new CheckNetwork(this);
        mCheckNetwork.registerCheckingNetwork();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mCheckNetwork.unRegisterCheckingNetwork();
    }
}
