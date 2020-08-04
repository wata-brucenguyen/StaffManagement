package com.example.staffmanagement.Presenter.Admin;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.staffmanagement.Model.BUS.AppDatabase;
import com.example.staffmanagement.Model.BUS.RequestBUS;
import com.example.staffmanagement.Model.BUS.StateRequestBUS;
import com.example.staffmanagement.Model.Database.DAL.RequestDbHandler;
import com.example.staffmanagement.Model.Database.DAL.StateRequestDbHandler;
import com.example.staffmanagement.Model.Database.Entity.Request;
import com.example.staffmanagement.Model.Database.Entity.StateRequest;
import com.example.staffmanagement.Presenter.Admin.Background.UserRequestActUiHandler;
import com.example.staffmanagement.Presenter.Staff.Background.MyMessage;
import com.example.staffmanagement.Presenter.Staff.Background.RequestActUiHandler;
import com.example.staffmanagement.View.Admin.UserRequestActivity.UserRequestInterface;
import com.example.staffmanagement.View.Admin.ViewModel.UserRequestViewModel;
import com.example.staffmanagement.View.Data.AdminRequestFilter;
import com.example.staffmanagement.View.Data.StaffRequestFilter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRequestPresenter {
    private Context mContext;
    private UserRequestInterface mInterface;
    private UserRequestActUiHandler mHandler;
    private RequestBUS bus;

    public UserRequestPresenter(Context mContext, UserRequestInterface mInterface) {
        this.mContext = mContext;
        this.mInterface = mInterface;
        mHandler = new UserRequestActUiHandler(mInterface);
        WeakReference<Context> weakReference = new WeakReference<>(mContext);
    }

    public void destroyBus() {
        bus = null;
    }

    public void getAllRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mInterface.setRefresh(true);
                RequestBUS requestBUS = new RequestBUS();
                // RequestDbHandler db = new RequestDbHandler(mContext);
                ArrayList<Request> arrayList = (ArrayList<Request>) requestBUS.getAll(mContext);
                mInterface.setRefresh(false);

            }
        }).start();
    }

    public void getFullNameById(final int idUser) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bus=new RequestBUS();
                String name=bus.getFullNameById(mContext,idUser);
            }
        }).start();

    }

    public void getTitleById(final UserRequestViewModel vm, final int idRequest) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bus =new RequestBUS();
                String title = bus.getTitleById(mContext,idRequest);
                vm.setTitle(title);
                destroyBus();
            }
        }).start();
    }

    public long getDateTimeById(int idRequest) {
        bus =new RequestBUS();
        long dateTime= bus.getDateTimeById(mContext, idRequest);
        return dateTime;
    }

    public int getIdStateById(int idRequest) {
        bus =new RequestBUS();
        int idState= bus.getIdStateById(mContext,idRequest);
        return idState;
    }

    public void update(Request request) {
        RequestBUS bus =new RequestBUS();
        bus.update(mContext,request);

    }

    public void getAllStateRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StateRequestBUS bus = new StateRequestBUS();
                final List<StateRequest> list = bus.getAllStateRequest(mContext);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterface.onSuccessGetAllStateRequest(list);
                    }
                });

            }
        }).start();
    }

    public void getRequestForUser(int idUser, String searchString) {
        RequestBUS bus = new RequestBUS();
        List<Request> list= bus.getRequestForUser(mContext,idUser,searchString);
        mInterface.onLoadMoreListSuccess(list);
    }

    public void getLimitListRequestForUser(final int idUser, final int offset, final int numRow, final AdminRequestFilter criteria) {
        bus = new RequestBUS();
        bus.getLimitListRequestForUser1(mContext, idUser, offset, numRow, criteria);
        bus.getListLiveData().observe((LifecycleOwner) mContext, new Observer<List<Request>>() {
            @Override
            public void onChanged(List<Request> requests) {
                if (requests != null && requests.size() > 0)
                    Log.i("GG", requests.get(0).getTitle());
                mHandler.sendMessage(MyMessage.getMessage(UserRequestActUiHandler.MSG_ADD_LOAD_MORE_LIST, bus.getListLiveData().getValue()));
            }
        });

    }
}

