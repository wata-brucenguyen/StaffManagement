package com.example.staffmanagement.Presenter.Staff;

import android.app.Activity;
import android.content.Context;

import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.Model.Entity.StateRequest;
import com.example.staffmanagement.Model.LocalDb.BUS.RequestBUS;
import com.example.staffmanagement.Model.LocalDb.BUS.StateRequestBUS;
import com.example.staffmanagement.Presenter.Staff.Background.MyMessage;
import com.example.staffmanagement.Presenter.Staff.Background.RequestActUiHandler;
import com.example.staffmanagement.MVVM.View.Data.StaffRequestFilter;
import com.example.staffmanagement.MVVM.View.Staff.RequestManagement.RequestActivity.StaffRequestInterface;

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

    public void getAllStateRequest() {
        new Thread(() -> {
            StateRequestBUS bus = new StateRequestBUS();
            final List<StateRequest> list = bus.getAllStateRequest();
            ((Activity) mContext).runOnUiThread(() ->
                    mInterface.onSuccessGetAllStateRequest(list));
        }).start();
    }
}
