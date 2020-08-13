package com.example.staffmanagement.Presenter.Admin;

import android.content.Context;

import com.example.staffmanagement.Model.LocalDb.BUS.RequestBUS;
import com.example.staffmanagement.Model.LocalDb.BUS.StateRequestBUS;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.Request;
import com.example.staffmanagement.View.Admin.DetailRequestUser.DetailRequestUserActivity;

import java.lang.ref.WeakReference;

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
                bus.getIdStateByName(stateName);
                mInterface.getIdStateByName(stateName);
            }
        }).start();
    }

    public void getStateNameById(final int idState){
        new Thread(new Runnable() {
            @Override
            public void run() {
                bus=new StateRequestBUS();
                bus.getStateNameById(idState);
//                mInterface.getStateNameById(idState);
            }
        }).start();
    }

     public void update(final Request request){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBUS bus= new RequestBUS();
                bus.updateStateRequest(request);
            }
        }).start();

     }

}
