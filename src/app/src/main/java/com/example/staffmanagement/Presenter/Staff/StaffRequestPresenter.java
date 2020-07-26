package com.example.staffmanagement.Presenter.Staff;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.staffmanagement.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Database.DAL.StateRequestDbHandler;
import com.example.staffmanagement.Database.Entity.Request;
import com.example.staffmanagement.Presenter.Staff.Background.MyMessage;
import com.example.staffmanagement.Presenter.Staff.Background.RequestActUiHandler;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestInterface;

import java.util.ArrayList;

public class StaffRequestPresenter {
    private RequestActUiHandler mHandler;
    private Context mContext;
    private StaffRequestInterface mInterface;

    public StaffRequestPresenter(Context mContext, StaffRequestInterface mInterface) {
        this.mContext = mContext;
        this.mInterface = mInterface;
        this.mHandler = new RequestActUiHandler(mInterface);
    }

    public void getAllRequestForUser(final int idUser){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
                RequestDbHandler db = new RequestDbHandler(mContext);
                ArrayList<Request> list = db.getAllRequestForUser(idUser);
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_UPDATE_LIST,list));
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
                mHandler.removeCallbacks(null);
            }
        }).start();
    }

    public void addNewRequest(final Request request){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
                RequestDbHandler db = new RequestDbHandler(mContext);
                Request req = db.insert(request);
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_ADD_NEW_REQUEST_SUCCESSFULLY,req));
                mHandler.removeCallbacks(null);
            }
        }).start();
    }

    public void updateRequest(final Request request){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
                RequestDbHandler db = new RequestDbHandler(mContext);
                db.update(request);
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_UPDATE_REQUEST_SUCCESSFULLY,request));
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
                mHandler.removeCallbacks(null);
            }
        }).start();
    }

    public void findRequest(final int idUSer, final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestDbHandler db = new RequestDbHandler(mContext);
                ArrayList<Request> list = db.findRequestByTitle(idUSer, title);
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_UPDATE_LIST,list));
            }
        }).start();
    }

    public String getStateNameById(int idState) {
        StateRequestDbHandler db = new StateRequestDbHandler(mContext);
        return db.getStateNameById(idState);
    }
}
