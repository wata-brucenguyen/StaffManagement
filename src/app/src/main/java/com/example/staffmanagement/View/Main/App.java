package com.example.staffmanagement.View.Main;

import android.app.Application;

import com.example.staffmanagement.Model.Repository.AppDatabase;
import com.example.staffmanagement.View.Ultils.CheckNetwork;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.getInstance(this);
//        CheckNetwork mCheckNetwork = new CheckNetwork(this);
//        mCheckNetwork.registerCheckingNetwork();
//        mCheckNetwork.unRegisterCheckingNetwork();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
     //   mCheckNetwork.unRegisterCheckingNetwork();
    }
}
