package com.example.staffmanagement.Presenter;

import android.content.Context;

import com.example.staffmanagement.Admin.MainAdminActivity.MainAdminInterface;
import com.example.staffmanagement.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Database.Entity.Request;

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
    public String getTitleById(int idRequest){
        RequestDbHandler db=new RequestDbHandler(mContext);
        return db.getTitleById(idRequest);
    }
    public String getDateTimeById(int idRequest){
        RequestDbHandler db=new RequestDbHandler(mContext);
        return db.getDateTimeById(idRequest);
    }
    public String getFullNameById(int idUser){
        RequestDbHandler db=new RequestDbHandler(mContext);
        return db.getFullNameById(idUser);
    }
    public int getIdStateById(int idRequest){
        RequestDbHandler db=new RequestDbHandler(mContext);
        return db.getIdStateById(idRequest);
    }
}
