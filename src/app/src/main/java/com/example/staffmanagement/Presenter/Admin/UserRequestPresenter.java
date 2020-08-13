package com.example.staffmanagement.Presenter.Admin;

import android.app.Activity;
import android.content.Context;

import com.example.staffmanagement.Model.LocalDb.BUS.RequestBUS;
import com.example.staffmanagement.Model.LocalDb.BUS.StateRequestBUS;
import com.example.staffmanagement.Model.LocalDb.BUS.UserBUS;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.Request;
import com.example.staffmanagement.Model.LocalDb.Database.Entity.StateRequest;
import com.example.staffmanagement.Presenter.Admin.Background.UserRequestActUiHandler;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestInterface;
import com.example.staffmanagement.View.Data.AdminRequestFilter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class UserRequestPresenter {
    private Context mContext;
    private UserRequestInterface mInterface;
    private UserRequestActUiHandler mHandler;

    public UserRequestPresenter(Context context, UserRequestInterface mInterface) {
        WeakReference<Context> weakReference = new WeakReference<>(context);
        this.mContext = weakReference.get();
        this.mInterface = mInterface;
        mHandler = new UserRequestActUiHandler(mInterface);

    }

    public void updateRequest(final Request request) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBUS bus = new RequestBUS();
                bus.updateStateRequest(request);
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

    public void getLimitListRequestForUser(final int idUser, final int offset, final int numRow, final AdminRequestFilter criteria) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBUS bus = new RequestBUS();
                final List<Request> list = bus.getLimitListRequestForUser( idUser, offset, numRow, criteria);
                final List<String> userNameList = new ArrayList<String>();
                for(Request r : list){
                    String name = new UserBUS().getFullNameById(r.getIdUser());
                    userNameList.add(name);
                }
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.onLoadMoreListSuccess(list,
                                userNameList);
                    }
                });
            }
        }).start();

    }
}

