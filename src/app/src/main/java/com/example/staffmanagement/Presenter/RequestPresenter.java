package com.example.staffmanagement.Presenter;

import android.content.Context;

import com.example.staffmanagement.View.Admin.MainAdminActivity.MainAdminInterface;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestInterface;
import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.StateRequestDbHandler;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestInterface;
import com.example.staffmanagement.View.Staff.UserProfile.StaffUserProfileInterface;

import java.util.ArrayList;

public class RequestPresenter {
    private Context mContext;
    private MainAdminInterface mainAdminInterface;
    private UserRequestInterface userRequestInterface;

    public RequestPresenter(Context mContext, MainAdminInterface mainAdminInterface) {
        this.mContext = mContext;
        this.mainAdminInterface = mainAdminInterface;
    }

    public RequestPresenter(Context mContext, UserRequestInterface userRequestInterface) {
        this.mContext = mContext;
        this.userRequestInterface = userRequestInterface;
    }

    public int getCountWaitingForRequest(int idUser){
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getCountWaitingForUser(idUser);
    }

    public String getRoleNameById(int idRole) {
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getRoleNameById(idRole);
    }

    public String getTitleById(int idRequest){
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getTitleById(idRequest);
    }

    public String getDateTimeById(int idRequest){
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getDateTimeById(idRequest);
    }

    public String getFullNameById(int idUser){
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getFullNameById(idUser);
    }

    public int getIdStateById(int idRequest){
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getIdStateById(idRequest);
    }

    public ArrayList<Request> getAllRequest(){
        userRequestInterface.setRefresh(true);
        RequestDbHandler db=new RequestDbHandler(mContext);
        userRequestInterface.setRefresh(false);
        return db.getAll();
    }
    public ArrayList<StateRequest> getAllStateRequest(){
        StateRequestDbHandler db=new StateRequestDbHandler(mContext);
        return db.getAll();
    }

    public void update(Request request){
        RequestDbHandler db=new RequestDbHandler(mContext);
        db.update(request);
    }
}
