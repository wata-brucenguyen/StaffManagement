package com.example.staffmanagement.Presenter.Staff.Background;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.staffmanagement.MVVM.Model.Entity.Request;
import com.example.staffmanagement.MVVM.View.Staff.RequestManagement.RequestActivity.StaffRequestInterface;

import java.util.ArrayList;

public class RequestActUiHandler extends Handler {

    private StaffRequestInterface mInterface;
    public static final int MSG_SHOW_PROGRESS_DIALOG = 1;
    public static final int MSG_DISMISS_PROGRESS_DIALOG = 2;
    public static final int MSG_ADD_NEW_REQUEST_SUCCESSFULLY = 4;
    public static final int MSG_UPDATE_REQUEST_SUCCESSFULLY = 5;
    public static final int MSG_ADD_LOAD_MORE_LIST = 6;

    public RequestActUiHandler(StaffRequestInterface mInterface) {
        this.mInterface = mInterface;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case MSG_SHOW_PROGRESS_DIALOG:
                mInterface.newProgressDialog("Loading...");
                mInterface.showProgressDialog();
                break;
            case MSG_DISMISS_PROGRESS_DIALOG:
                mInterface.dismissProgressDialog();
                break;
            case MSG_ADD_NEW_REQUEST_SUCCESSFULLY:
                break;
            case MSG_UPDATE_REQUEST_SUCCESSFULLY:
                break;
            case MSG_ADD_LOAD_MORE_LIST:
                mInterface.onLoadMoreListSuccess((ArrayList<Request>) msg.obj);
                break;
        }
    }

}
