package com.example.staffmanagement.Presenter.Staff;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.staffmanagement.Model.BUS.RequestBUS;
import com.example.staffmanagement.Model.BUS.StateRequestBUS;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
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
    private StaffRequestInterface mInterface;

    public StaffRequestPresenter(Context context, StaffRequestInterface mInterface) {
        WeakReference<Context> weakContext = new WeakReference<>(context);
        this.mContext = weakContext.get();
        this.mHandler = new RequestActUiHandler(mInterface);
        this.mInterface = mInterface;
    }

    public void getLimitListRequestForUser(final int idUser, final int offset, final int numRow, final StaffRequestFilter criteria) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBUS bus = new RequestBUS();
                List<Request> list = bus.getLimitListRequestForStaff(idUser, offset, numRow, criteria);
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_ADD_LOAD_MORE_LIST, list));
            }
        }).start();
    }

    public void addNewRequest(final Request request) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
                RequestBUS bus = new RequestBUS();
                Request req = bus.insert(request);
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_ADD_NEW_REQUEST_SUCCESSFULLY, req));
            }
        }).start();
    }

    public void updateRequest(final Request request) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
                RequestBUS bus = new RequestBUS();
                bus.updateStateRequest(request);
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_UPDATE_REQUEST_SUCCESSFULLY, request));
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
            }
        }).start();
    }

    public void getAllStateRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StateRequestBUS bus = new StateRequestBUS();
                final List<StateRequest> list = bus.getAllStateRequest();
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.onSuccessGetAllStateRequest(list);
                    }
                });
            }
        }).start();
    }
}
