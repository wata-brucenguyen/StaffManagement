package com.example.staffmanagement.Presenter.Admin;

import android.content.Context;
import android.view.View;

import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.View.Admin.DetailRequestUser.DetailRequestUserActivity;
import com.example.staffmanagement.View.Ultils.Constant;

import java.lang.ref.WeakReference;
import java.security.PublicKey;

public class DetailRequestPresenter {
    private Context mContext;
    private DetailRequestUserActivity mInterface;

    public DetailRequestPresenter(Context context, DetailRequestUserActivity mInterface) {
        WeakReference<Context> weakReference = new WeakReference<>(context);
        this.mContext = weakReference.get();
        this.mInterface = mInterface;
    }

    public int getIdStateByName(String stateName){
        RequestDbHandler db =new RequestDbHandler(mContext);
        return db.getIdStateByName(stateName);
    }

    public String getStateNameById(int idState){
        RequestDbHandler db =new RequestDbHandler(mContext);
        return db.getStateNameById(idState);
    }

     public void update(Request request){
        RequestDbHandler db = new RequestDbHandler(mContext);
        db.update(request);
     }
}
