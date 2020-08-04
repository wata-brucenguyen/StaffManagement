package com.example.staffmanagement.Presenter.Staff;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.staffmanagement.Model.BUS.RequestBUS;
import com.example.staffmanagement.Model.BUS.StateRequestBUS;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Presenter.Staff.Background.MyMessage;
import com.example.staffmanagement.Presenter.Staff.Background.RequestActUiHandler;
import com.example.staffmanagement.View.Data.StaffRequestFilter;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestInterface;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestListAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

public class StaffRequestPresenter {
    private RequestActUiHandler mHandler;
    private Context mContext;
    private RequestBUS bus;
    private StaffRequestInterface mInterface;

    public StaffRequestPresenter(Context context, StaffRequestInterface mInterface) {
        WeakReference<Context> weakContext = new WeakReference<>(context);
        this.mContext = weakContext.get();
        this.mHandler = new RequestActUiHandler(mInterface);
        this.mInterface = mInterface;
    }

    public void destroyBus(){
        bus = null;
    }

    public void getLimitListRequestForUser(final int idUser, final int offset, final int numRow, final StaffRequestFilter criteria) {
        bus = new RequestBUS();
        bus.getLimitListRequestForUser(mContext, idUser, offset, numRow, criteria);
        bus.getListLiveData().observe((LifecycleOwner) mContext, new Observer<List<Request>>() {
            @Override
            public void onChanged(List<Request> requests) {
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_ADD_LOAD_MORE_LIST,requests));
            }
        });
    }

    public void addNewRequest(final Request request) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
                RequestBUS bus = new RequestBUS();
                Request req = bus.insert(mContext, request);
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_ADD_NEW_REQUEST_SUCCESSFULLY, req));
                destroyBus();
            }
        }).start();
    }

    public void updateRequest(final Request request) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
                RequestBUS bus = new RequestBUS();
                bus.updateStateRequest(mContext, request);
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_UPDATE_REQUEST_SUCCESSFULLY, request));
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
                destroyBus();
            }
        }).start();
    }

    public void getStateNameById(final int idRequest, final int idState, final StaffRequestListAdapter.ViewHolder holder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StateRequestBUS bus = new StateRequestBUS();
                String stateName = bus.getStateNameById(mContext, idState);
                mInterface.onSuccessGetStateNameById(idRequest,stateName,holder);
                destroyBus();
            }
        }).start();
    }
}
