package com.example.staffmanagement.Presenter;

import android.content.Context;

import com.example.staffmanagement.Admin.MainAdminActivity.MainAdminInterface;
import com.example.staffmanagement.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Database.DAL.StateRequestDbHandler;
import com.example.staffmanagement.Database.Entity.Request;
import com.example.staffmanagement.NonAdmin.RequestActivity.RequestAcInterface;

import java.util.ArrayList;

public class RequestPresenter {
    private Context mContext;
    private MainAdminInterface mainAdminInterface;
    private RequestAcInterface requestAcInterface;

    public RequestPresenter(Context mContext, MainAdminInterface mainAdminInterface) {
        this.mContext = mContext;
        this.mainAdminInterface = mainAdminInterface;
    }

    public RequestPresenter(Context mContext, RequestAcInterface requestAcInterface) {
        this.mContext = mContext;
        this.requestAcInterface = requestAcInterface;
    }

    public ArrayList<Request> getAllRequestForUser(int idUser){
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getAllRequestForUser(idUser);
    }

    public int getCountWaitingForRequest(int idUser){
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getCountWaitingForUser(idUser);
    }

    public String getRoleNameById(int idRole) {
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getRoleNameById(idRole);
    }

    public String getStateNameById(int idState) {
        StateRequestDbHandler db = new StateRequestDbHandler(mContext);
        return db.getStateNameById(idState);
    }

    public void addNewRequest(Request request){
        RequestDbHandler db = new RequestDbHandler(mContext);
        db.insert(request);
        requestAcInterface.showMessage("Add successfully");
    }

    public ArrayList<Request> findRequest(int idUSer, String title){
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.findRequestByTitle(idUSer,title);
    }
}
