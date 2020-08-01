package com.example.staffmanagement.Presenter.Admin;

import android.content.Context;

import com.example.staffmanagement.Model.BUS.AppDatabase;
import com.example.staffmanagement.Model.BUS.RequestBUS;
import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.StateRequestDbHandler;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.Presenter.Admin.Background.UserRequestActUiHandler;
import com.example.staffmanagement.Presenter.Staff.Background.MyMessage;
import com.example.staffmanagement.Presenter.Staff.Background.RequestActUiHandler;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestInterface;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

public class UserRequestPresenter {
    private Context mContext;
    private UserRequestInterface mInterface;
    private UserRequestActUiHandler mHandler;

    public UserRequestPresenter(Context mContext, UserRequestInterface mInterface) {
        this.mContext = mContext;
        this.mInterface = mInterface;
        mHandler =new UserRequestActUiHandler(mInterface);
        WeakReference<Context> weakReference=new WeakReference<>(mContext);
    }

    public void getAllRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mInterface.setRefresh(true);
                RequestBUS requestBUS = new RequestBUS();
                // RequestDbHandler db = new RequestDbHandler(mContext);
                ArrayList<Request> arrayList= (ArrayList<Request>) requestBUS.getAll(mContext);
                mInterface.setRefresh(false);

            }
        }).start();

    }

    public String getFullNameById(int idUser) {
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getFullNameById(idUser);
    }

    public String getTitleById(int idRequest) {
        RequestDbHandler db = new RequestDbHandler(mContext);
        return db.getTitleById(idRequest);
    }

    public long getDateTimeById(int idRequest) {
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

    public void getRequestForUser(int idUser, String searchString){
        RequestDbHandler db =new RequestDbHandler(mContext);
        ArrayList<Request> list = db.getRequestForUser(idUser,searchString);
        mInterface.onLoadMoreListSuccess(list);
    }
    public void getLimitListRequestForUser(final int idUser, final int offset, final int numRow,final AdminRequestFilter criteria){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestDbHandler db = new RequestDbHandler(mContext);
                ArrayList<Request> list = db.getLimitListRequestForUser1(idUser,offset,numRow,criteria);
                mHandler.sendMessage(MyMessage.getMessage(UserRequestActUiHandler.MSG_ADD_LOAD_MORE_LIST,list));
                mHandler.removeCallbacks(null);
            }
        }).start();
    }

}
