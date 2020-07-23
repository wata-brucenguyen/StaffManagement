package com.example.staffmanagement.Presenter;

import android.content.Context;

import com.example.staffmanagement.Admin.MainAdminActivity.MainAdminInterface;
import com.example.staffmanagement.Database.DAL.RequestDbHandler;

public class RequestPresenter {
    private Context mContext;
    private MainAdminInterface mainAdminInterface;

    public RequestPresenter(Context mContext, MainAdminInterface mainAdminInterface) {
        this.mContext = mContext;
        this.mainAdminInterface = mainAdminInterface;
    }

    public int getCountWaitingForRequest(int idUser){
        RequestDbHandler db=new RequestDbHandler(mContext);
        return db.getCountWaitingForUser(idUser);
    }

    public String getRoleNameById(int idRole) {
        RequestDbHandler db=new RequestDbHandler(mContext);
        return db.getRoleNameById(idRole);
    }
}
