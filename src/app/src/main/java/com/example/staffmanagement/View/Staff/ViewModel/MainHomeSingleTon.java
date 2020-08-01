package com.example.staffmanagement.View.Staff.ViewModel;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.View.Data.CopyUserSingleTon;

import java.lang.ref.WeakReference;

public class MainHomeSingleTon {
    private static MainHomeSingleTon instance;
    private Context mContext;

    private MainHomeSingleTon(Context context){
        this.mContext = context;
        WeakReference<Context> weak = new WeakReference<>(this.mContext);
    }

    public static MainHomeSingleTon getInstance(Context context){
        if(instance == null)
            instance = new MainHomeSingleTon(context);
        return instance;
    }

    public void observeUserSingleTon(){
        CopyUserSingleTon.getInstance().getUserLiveData().observe((LifecycleOwner) mContext, new Observer<User>() {
            @Override
            public void onChanged(User user) {

            }
        });
    }

    public void destroy(){
        this.mContext = null;
        instance = null;
    }
}
