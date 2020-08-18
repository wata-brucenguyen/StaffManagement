package com.example.staffmanagement.Presenter.Admin.Background;

import android.os.Message;

import androidx.annotation.NonNull;

import com.example.staffmanagement.MVVM.View.Admin.SendNotificationActivity.SendNotificationInterface;

import android.os.Handler;

public class SendNotificationUIHandler extends Handler {
    private SendNotificationInterface mInterface;
    public static final int MSG_SHOW_PROGRESS_DIALOG = 1;
    public static final int MSG_DISMISS_PROGRESS_DIALOG = 2;
    public static final int MSG_ADD_NEW_USER_SUCCESSFULLY = 4;

    public SendNotificationUIHandler(SendNotificationInterface mInterface) {
        this.mInterface = mInterface;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case MSG_SHOW_PROGRESS_DIALOG:
                mInterface.newProgressDialog("Loading");
                mInterface.dismissProgressDialog();
                break;
            case MSG_DISMISS_PROGRESS_DIALOG:
                mInterface.dismissProgressDialog();
                break;
        }

    }
}
