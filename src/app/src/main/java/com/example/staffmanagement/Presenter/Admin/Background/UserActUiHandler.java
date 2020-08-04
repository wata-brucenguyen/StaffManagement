package com.example.staffmanagement.Presenter.Admin.Background;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.staffmanagement.Model.Database.Entity.User;
import com.example.staffmanagement.View.Admin.MainAdminActivity.MainAdminInterface;

import java.util.ArrayList;

public class UserActUiHandler extends Handler {
    private MainAdminInterface mInterface;
    public static final int MSG_SHOW_PROGRESS_DIALOG = 1;
    public static final int MSG_DISMISS_PROGRESS_DIALOG = 2;
    public static final int MSG_ADD_NEW_USER_SUCCESSFULLY = 4;
    public static final int MSG_DELETE_USER_SUCCESSFULLY = 5;
    public static final int MSG_ADD_LOAD_MORE_LIST = 6;

    public UserActUiHandler(MainAdminInterface mInterface) {
        this.mInterface = mInterface;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case MSG_SHOW_PROGRESS_DIALOG:
                mInterface.newProgressDialog("Loading");
                mInterface.dismissProgressDialog();
                break;
            case MSG_DISMISS_PROGRESS_DIALOG:
                mInterface.dismissProgressDialog();
                break;
            case MSG_ADD_NEW_USER_SUCCESSFULLY:
                mInterface.onAddNewUserSuccessfully((User) msg.obj);
                break;
            case MSG_ADD_LOAD_MORE_LIST:
                mInterface.onLoadMoreListSuccess((ArrayList<User>) msg.obj);
                break;
        }

    }
}
