package com.example.staffmanagement.Presenter.Admin;

import android.content.Context;

import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.StateRequestDbHandler;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestInterface;

import java.util.ArrayList;

public class UserRequestPresenter {
    private Context mContext;
    private UserRequestInterface mInterface;

    public UserRequestPresenter(Context mContext, UserRequestInterface mInterface) {
        this.mContext = mContext;
        this.mInterface = mInterface;
    }

    public ArrayList<Request> getAllRequest() {
        mInterface.setRefresh(true);
        RequestDbHandler db = new RequestDbHandler(mContext);
        mInterface.setRefresh(false);
        return db.getAll();
    }

    public String getFullNameById(int idUser) {
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getFullNameById(idUser);
    }

    public String getTitleById(int idRequest) {
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getTitleById(idRequest);
    }

    public String getDateTimeById(int idRequest) {
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getDateTimeById(idRequest);
    }

    public int getIdStateById(int idRequest) {
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getIdStateById(idRequest);
    }

    public void update(Request request) {
        RequestDbHandler db = new RequestDbHandler(mContext);
        db.update(request);
    }

    public ArrayList<StateRequest> getAllStateRequest() {
        StateRequestDbHandler db = new StateRequestDbHandler(mContext);
        return db.getAll();
    }

}
