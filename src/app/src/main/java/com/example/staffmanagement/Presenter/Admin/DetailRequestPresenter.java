package com.example.staffmanagement.Presenter.Admin;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.example.staffmanagement.Model.BUS.RequestBUS;
import com.example.staffmanagement.Model.BUS.StateRequestBUS;
import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.View.Admin.DetailRequestUser.DetailRequestUserActivity;
import com.example.staffmanagement.View.Ultils.Constant;

import java.lang.ref.WeakReference;
import java.security.PublicKey;
import java.util.List;

public class DetailRequestPresenter {
    private Context mContext;
    private DetailRequestUserActivity mInterface;
    private StateRequestBUS bus;
    public DetailRequestPresenter(Context context, DetailRequestUserActivity mInterface) {
        WeakReference<Context> weakContext = new WeakReference<>(context);
        this.mContext = weakContext.get();
        this.mInterface = mInterface;
    }

    public void getIdStateByName(final String stateName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                bus=new StateRequestBUS();
                bus.getIdStateByName(mContext,stateName);
                mInterface.getIdStateByName(stateName);
            }
        }).start();
    }

    public void getStateNameById(final int idState){
        new Thread(new Runnable() {
            @Override
            public void run() {
                bus=new StateRequestBUS();
                bus.getStateNameById(mContext,idState);
//                mInterface.getStateNameById(idState);
            }
        }).start();
    }

     public void update(final Request request){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBUS bus= new RequestBUS();
                bus.updateStateRequest(mContext,request);
            }
        }).start();

     }

}
