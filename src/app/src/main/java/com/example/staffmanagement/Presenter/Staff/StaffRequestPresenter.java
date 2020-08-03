package com.example.staffmanagement.Presenter.Staff;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.staffmanagement.Model.BUS.RequestBUS;
import com.example.staffmanagement.Model.BUS.StateRequestBUS;
import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.StateRequestDbHandler;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.Presenter.Staff.Background.MyMessage;
import com.example.staffmanagement.Presenter.Staff.Background.RequestActUiHandler;
import com.example.staffmanagement.View.Data.StaffRequestFilter;
import com.example.staffmanagement.View.Data.UserSingleTon;
import com.example.staffmanagement.View.Staff.RequestManagement.RequestActivity.StaffRequestInterface;
import com.example.staffmanagement.View.Ultils.ImageHandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffRequestPresenter {
    private RequestActUiHandler mHandler;
    private Context mContext;
    private RequestBUS bus;
    public StaffRequestPresenter(Context mContext, StaffRequestInterface mInterface) {
        this.mContext = mContext;
        this.mHandler = new RequestActUiHandler(mInterface);
        WeakReference<Context> weak = new WeakReference<>(this.mContext);
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
                if ( bus.getListLiveData() != null &&  bus.getListLiveData().getValue() != null) {
                    for(int i= 0 ; i< bus.getListLiveData().getValue().size(); i++){
                        Log.i("GETDATA",bus.getListLiveData().getValue().get(i).getTitle());
                    }
                }
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_ADD_LOAD_MORE_LIST, bus.getListLiveData().getValue()));
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
                mHandler.removeCallbacks(null);
            }
        }).start();
    }

    public void updateRequest(final Request request) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_SHOW_PROGRESS_DIALOG));
                RequestBUS bus = new RequestBUS();
                bus.update(mContext, request);
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_UPDATE_REQUEST_SUCCESSFULLY, request));
                mHandler.sendMessage(MyMessage.getMessage(RequestActUiHandler.MSG_DISMISS_PROGRESS_DIALOG));
                mHandler.removeCallbacks(null);
            }
        }).start();
    }

    public String getStateNameById(int idState) {
        StateRequestBUS bus = new StateRequestBUS();
        return bus.getStateNameById(mContext, idState);
    }

    public void loadHeaderDrawerNavigation(final Context context, final ImageView avatar, final TextView txtName, final TextView txtEmail) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageHandler.loadImageFromBytes(mContext, UserSingleTon.getInstance().getUser().getAvatar(), avatar);
                        txtName.setText(UserSingleTon.getInstance().getUser().getFullName());
                        txtEmail.setText(UserSingleTon.getInstance().getUser().getEmail());
                    }
                });
            }
        }).start();
    }
}
